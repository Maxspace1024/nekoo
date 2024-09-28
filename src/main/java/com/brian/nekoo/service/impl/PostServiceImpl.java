package com.brian.nekoo.service.impl;

import com.brian.nekoo.dto.PostDTO;
import com.brian.nekoo.dto.req.PostReqDTO;
import com.brian.nekoo.entity.mongo.Asset;
import com.brian.nekoo.entity.mongo.Post;
import com.brian.nekoo.entity.mysql.User;
import com.brian.nekoo.enumx.AssetTypeEnum;
import com.brian.nekoo.repository.mongo.AssetRepository;
import com.brian.nekoo.repository.mongo.PostRepository;
import com.brian.nekoo.repository.mysql.UserRepository;
import com.brian.nekoo.service.PostService;
import com.brian.nekoo.service.S3Service;
import com.brian.nekoo.util.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final S3Service s3Service;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AssetRepository assetRepository;

    public PostServiceImpl(S3Service s3Service, PostRepository postRepository, UserRepository userRepository, AssetRepository assetRepository) {
        this.s3Service = s3Service;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.assetRepository = assetRepository;
    }

    @Override
    public PostDTO createPost(PostReqDTO dto) {
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
                    s3Service.uploadFile(file, uuidFilename);
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
    public PostDTO deletePost(PostReqDTO dto) {
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
    public PostDTO updatePost(PostReqDTO dto) {
        Optional<Post> oPost = postRepository.findById(dto.getPostId());
        Post post = null;
        if (oPost.isPresent()) {
            Instant now = Instant.now();
            post = oPost.get();
            Integer privacy = dto.getPrivacy();
            if (privacy != null) {
                post.setPrivacy(privacy);
            }
            String content = dto.getContent();
            if (privacy != null) {
                post.setContent(content);
            }
            post.setModifyAt(now);
            post = postRepository.save(post);

            User user = userRepository.findById(post.getUserId()).get();
            return PostDTO.getDTO(post, user);
        } else {
            return null;
        }
    }

    @Override
    public List<PostDTO> findPost() {
        List<Post> posts = postRepository.findAllByOrderByCreateAtDesc();
        List<PostDTO> postDTOs = posts.stream().map(post -> {
            User user = userRepository.findById(post.getUserId()).get();
            PostDTO dto = PostDTO.getDTO(post, user);
            dto.setTotalDanmakuCount(5L);
            return dto;
        }).toList();
        return postDTOs;
    }
}
