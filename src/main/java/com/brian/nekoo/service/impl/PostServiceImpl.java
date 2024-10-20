package com.brian.nekoo.service.impl;

import com.brian.nekoo.dto.PageWrapper;
import com.brian.nekoo.dto.PostDTO;
import com.brian.nekoo.dto.req.PostReqDTO;
import com.brian.nekoo.dto.req.UploadPostReqDTO;
import com.brian.nekoo.entity.mongo.Asset;
import com.brian.nekoo.entity.mongo.Post;
import com.brian.nekoo.entity.mysql.Friendship;
import com.brian.nekoo.entity.mysql.User;
import com.brian.nekoo.enumx.AssetTypeEnum;
import com.brian.nekoo.enumx.FriendshipStateEnum;
import com.brian.nekoo.enumx.PostPrivacyEnum;
import com.brian.nekoo.enumx.Topix;
import com.brian.nekoo.repository.mongo.AssetRepository;
import com.brian.nekoo.repository.mongo.DanmakuRepository;
import com.brian.nekoo.repository.mongo.PostRepository;
import com.brian.nekoo.repository.mysql.FriendshipRepository;
import com.brian.nekoo.repository.mysql.UserRepository;
import com.brian.nekoo.service.PostService;
import com.brian.nekoo.service.S3Service;
import com.brian.nekoo.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final S3Service s3Service;
    private final FriendshipRepository friendshipRepository;
    private final DanmakuRepository danmakuRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AssetRepository assetRepository;

    @Override
    public PostDTO findPostById(String postId, User reqUser) {
        Optional<Post> oPost = postRepository.findById(postId);
        Friendship friendship = null;
        PostDTO postDTO = null;
        if (oPost.isPresent()) {
            Post post = oPost.get();
            List<Asset> assets = post.getAssets();
            long totalCount = 0L;
            if (!assets.isEmpty()) {
                totalCount = danmakuRepository.countByAssetIdAndRemoveAtIsNull(assets.get(0).getId());
            }

            User ownUser = userRepository.findById(post.getUserId()).get();
            long reqUserId = reqUser.getId();
            long ownUserId = ownUser.getId();
            friendship = friendshipRepository.findFriendship(reqUserId, ownUserId).orElse(null);
            boolean isFriend = (reqUserId == ownUserId) || (friendship != null && friendship.getState().equals(FriendshipStateEnum.APPROVED.ordinal()));

            if (isFriend || post.getPrivacy().equals(PostPrivacyEnum.PUBLIC.ordinal())) {
                postDTO = PostDTO.getDTO(post, ownUser);
                postDTO.setTotalDanmakuCount(totalCount);
                return postDTO;
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public PostDTO createPost(UploadPostReqDTO dto) {
        List<String> uuidFilenames = new ArrayList<>();
        List<Asset> assets = new ArrayList<>();
        PostDTO postDTO = null;
        try {
            if (dto.getFiles() != null) {
                for (MultipartFile file : dto.getFiles()) {
                    String uuidFilename = FileUtil.generateUuidFileName(file.getOriginalFilename());
                    String fileExtension = FileUtil.extractFileExtension(uuidFilename);
                    int assetType = AssetTypeEnum.fromExtension(fileExtension).ordinal();

                    uuidFilenames.add(uuidFilename);
                    s3Service.uploadFileWithProgress(file, uuidFilename, Topix.POST_PROGRESS + dto.getUserId());
                    Asset asset = Asset.builder()
                        .path(uuidFilename)
                        .type(assetType)
                        .size(file.getSize())
                        .build();
                    assets.add(asset);
                }
                assets = assetRepository.saveAll(assets);
            }

            Instant now = Instant.now();
            Post post = Post.builder()
                .userId(dto.getUserId())
                .privacy(dto.getPrivacy())
                .content(dto.getContent())
                .assets(assets)
                .hashtags(dto.getHashtags())
                .createAt(now)
                .modifyAt(now)
                .build();
            post = postRepository.insert(post);

            User user = userRepository.findById(post.getUserId()).get();
            postDTO = PostDTO.getDTO(post, user);
            postDTO.setTotalDanmakuCount(0L);
        } catch (Exception e) {
            try {
                for (String uuidFilename : uuidFilenames) {
                    s3Service.deleteFile(uuidFilename);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return postDTO;
    }

    @Override
    public PostDTO deletePost(UploadPostReqDTO dto) {
        Optional<Post> oPost = postRepository.findById(dto.getPostId());
        Post post = null;
        if (oPost.isPresent()) {
            Instant now = Instant.now();
            post = oPost.get();
            post.setModifyAt(now);
            post.setRemoveAt(now);
            post = postRepository.save(post);

            User user = userRepository.findById(post.getUserId()).get();
            return PostDTO.getDTO(post, user);
        } else {
            return null;
        }
    }

    @Override
    public PostDTO updatePost(UploadPostReqDTO dto) {
        List<String> uuidFilenames = new ArrayList<>();
        List<Asset> assets = new ArrayList<>();
        Optional<Post> oPost = postRepository.findById(dto.getPostId());
        Post post = null;
        if (oPost.isPresent()) {
            try {
                if (dto.getFiles() != null) {
                    for (MultipartFile file : dto.getFiles()) {
                        String uuidFilename = FileUtil.generateUuidFileName(file.getOriginalFilename());
                        String fileExtension = FileUtil.extractFileExtension(uuidFilename);
                        int assetType = AssetTypeEnum.fromExtension(fileExtension).ordinal();

                        uuidFilenames.add(uuidFilename);
//                        s3Service.uploadFile(file, uuidFilename);
                        s3Service.uploadFileWithProgress(file, uuidFilename, Topix.POST_PROGRESS + dto.getUserId());
                        Asset asset = Asset.builder()
                            .path(uuidFilename)
                            .type(assetType)
                            .size(file.getSize())
                            .build();
                        assets.add(asset);
                    }
                    assets = assetRepository.saveAll(assets);
                }
            } catch (Exception e) {
                try {
                    for (String uuidFilename : uuidFilenames) {
                        s3Service.deleteFile(uuidFilename);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }


            Instant now = Instant.now();
            post = oPost.get();
            Integer privacy = dto.getPrivacy();
            if (privacy != null) {
                post.setPrivacy(privacy);
            }
            String content = dto.getContent();
            if (!Strings.isBlank(content)) {
                post.setContent(content);
            }
            List<String> tags = dto.getHashtags();
            if (tags != null) {
                post.setHashtags(tags);
            }
            if (!assets.isEmpty()) {
                post.setAssets(assets);
            }
            post.setModifyAt(now);
            post = postRepository.save(post);

            User user = userRepository.findById(post.getUserId()).get();
            PostDTO postDTO = PostDTO.getDTO(post, user);
            List<Asset> postAssets = post.getAssets();
            if (!postAssets.isEmpty()) {
                postDTO.setTotalDanmakuCount(danmakuRepository.countByAssetIdAndRemoveAtIsNull(postAssets.get(0).getId()));
            }
            return postDTO;
        } else {
            return null;
        }
    }

    @Override
    public List<PostDTO> findPost() {
        Page<Post> posts = postRepository.findAllByRemoveAtIsNullOrderByCreateAtDesc(null);
        List<PostDTO> postDTOs = posts.stream().map(post -> {
            User user = userRepository.findById(post.getUserId()).get();
            PostDTO dto = PostDTO.getDTO(post, user);
            String assetId = dto.getAssets().get(0).getId(); // need optimiz
            dto.setTotalDanmakuCount(danmakuRepository.countByAssetIdAndRemoveAtIsNull(assetId));
            return dto;
        }).toList();
        return postDTOs;
    }

    @Override
    public PageWrapper<PostDTO> searchPublicPostByContentAndPage(PostReqDTO dto) {
        Pageable pageable = PageRequest.of(dto.getPage(), 4);
        Page<Post> posts = postRepository.findByRemoveAtIsNullAndContentContainingAndPrivacyAndCreateAtBeforeOrderByCreateAtDesc(
            pageable,
            dto.getQuery(),
            PostPrivacyEnum.PUBLIC.ordinal(),
            dto.getQueryAt()
        );
        List<PostDTO> postDTOs = posts.stream().map(post -> {
            User user = userRepository.findById(post.getUserId()).get();
            PostDTO postDTO = PostDTO.getDTO(post, user);
            List<Asset> assets = postDTO.getAssets();
            if (!assets.isEmpty()) {
                String assetId = assets.get(0).getId(); // need optimiz
                postDTO.setTotalDanmakuCount(danmakuRepository.countByAssetIdAndRemoveAtIsNull(assetId));
            } else {
                postDTO.setTotalDanmakuCount(0L);
            }
            return postDTO;
        }).toList();
        return PageWrapper.<PostDTO>builder()
            .page(postDTOs)
            .totalPages(posts.getTotalPages())
            .build();
    }

    @Override
    public PageWrapper<PostDTO> searchPublicPostByTagAndPage(PostReqDTO dto) {
        Pageable pageable = PageRequest.of(dto.getPage(), 4);
        Page<Post> posts = postRepository.findByRemoveAtIsNullAndHashtagsInAndPrivacyAndCreateAtBeforeOrderByCreateAtDesc(
            pageable,
            dto.getQuery(),
            PostPrivacyEnum.PUBLIC.ordinal(),
            dto.getQueryAt()
        );
        List<PostDTO> postDTOs = posts.stream().map(post -> {
            User user = userRepository.findById(post.getUserId()).get();
            PostDTO postDTO = PostDTO.getDTO(post, user);
            List<Asset> assets = postDTO.getAssets();
            if (!assets.isEmpty()) {
                String assetId = assets.get(0).getId(); // need optimiz
                postDTO.setTotalDanmakuCount(danmakuRepository.countByAssetIdAndRemoveAtIsNull(assetId));
            } else {
                postDTO.setTotalDanmakuCount(0L);
            }
            return postDTO;
        }).toList();
        return PageWrapper.<PostDTO>builder()
            .page(postDTOs)
            .totalPages(posts.getTotalPages())
            .build();
    }

    @Override
    public PageWrapper<PostDTO> findAllPublicPostByPage(PostReqDTO dto) {
        // public post only
        Pageable pageable = PageRequest.of(dto.getPage(), 4);
        Page<Post> posts = postRepository.findByRemoveAtIsNullAndPrivacyInOrderByCreateAtDesc(
            pageable,
            List.of(PostPrivacyEnum.PUBLIC.ordinal())
        );
        List<PostDTO> postDTOs = posts.stream().map(post -> {
            User user = userRepository.findById(post.getUserId()).get();
            PostDTO postDTO = PostDTO.getDTO(post, user);
            List<Asset> assets = postDTO.getAssets();
            if (!assets.isEmpty()) {
                String assetId = assets.get(0).getId(); // need optimiz
                postDTO.setTotalDanmakuCount(danmakuRepository.countByAssetIdAndRemoveAtIsNull(assetId));
            } else {
                postDTO.setTotalDanmakuCount(0L);
            }
            return postDTO;
        }).toList();
        return PageWrapper.<PostDTO>builder()
            .page(postDTOs)
            .totalPages(posts.getTotalPages())
            .build();
    }

    @Override
    public PageWrapper<PostDTO> findPostByPageWithUser(PostReqDTO dto, long reqUserId, long profileUserId) {
        // check friendship permission
        List<Integer> privacyList = new ArrayList<>();
        if (reqUserId == profileUserId) {
            privacyList = List.of(
                PostPrivacyEnum.PUBLIC.ordinal(),
                PostPrivacyEnum.FRIEND.ordinal()
            );
        } else {
            Optional<Friendship> oFriendship = friendshipRepository.findFriendship(reqUserId, profileUserId);
            if (oFriendship.isPresent()) {
                Friendship friendship = oFriendship.get();
                if (friendship.getState().equals(FriendshipStateEnum.APPROVED.ordinal())) {
                    // 是朋友
                    privacyList = List.of(
                        PostPrivacyEnum.PUBLIC.ordinal(),
                        PostPrivacyEnum.FRIEND.ordinal()
                    );
                } else {
                    // 不是朋友
                    privacyList = List.of(PostPrivacyEnum.PUBLIC.ordinal());
                }
            } else {
                // 不是朋友
                privacyList = List.of(PostPrivacyEnum.PUBLIC.ordinal());
            }
        }

        Pageable pageable = PageRequest.of(dto.getPage(), 4);
        Page<Post> posts = postRepository.findByRemoveAtIsNullAndUserIdAndPrivacyInOrderByCreateAtDesc(
            pageable,
            profileUserId,
            privacyList
        );
        List<PostDTO> postDTOs = posts.stream().map(post -> {
            User user = userRepository.findById(post.getUserId()).get();
            PostDTO postDTO = PostDTO.getDTO(post, user);
            List<Asset> assets = postDTO.getAssets();
            if (!assets.isEmpty()) {
                String assetId = assets.get(0).getId(); // need optimiz
                postDTO.setTotalDanmakuCount(danmakuRepository.countByAssetIdAndRemoveAtIsNull(assetId));
            } else {
                postDTO.setTotalDanmakuCount(0L);
            }
            return postDTO;
        }).toList();
        return PageWrapper.<PostDTO>builder()
            .page(postDTOs)
            .totalPages(posts.getTotalPages())
            .build();
    }
}
