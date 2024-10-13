package com.brian.nekoo.controller;

import com.brian.nekoo.dto.MessageWrapper;
import com.brian.nekoo.dto.PageWrapper;
import com.brian.nekoo.dto.PostDTO;
import com.brian.nekoo.dto.req.PostReqDTO;
import com.brian.nekoo.dto.req.UploadPostReqDTO;
import com.brian.nekoo.entity.mysql.User;
import com.brian.nekoo.service.FriendshipService;
import com.brian.nekoo.service.PostService;
import com.brian.nekoo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostController {

    private final FriendshipService friendshipService;
    private final PostService postService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping(value = "/post/{postId}")
    public ResponseEntity<Object> getPost(HttpServletRequest request, @PathVariable String postId) {
        User user = userService.checkLoginValid(request);
        PostDTO postDTO = null;
        if (user != null) {
            postDTO = postService.findPostById(postId, user);
        }
        return MessageWrapper.toResponseEntityOk(postDTO);
    }

//    @PostMapping(value = "/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<Object> createPost(HttpServletRequest request, @ModelAttribute UploadPostReqDTO dto) {
//        User user = userService.checkLoginValid(request);
//        PostDTO postDTO = null;
//        if (user != null) {
//            dto.setUserId(user.getId());
//            postDTO = postService.createPost(dto);
//            if (postDTO != null) messagingTemplate.convertAndSend("/topic/post/new", postDTO);
//        }
//        return MessageWrapper.toResponseEntityOk(postDTO);
//    }

    @PostMapping(value = "/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<Object>> createPost(HttpServletRequest request, @ModelAttribute UploadPostReqDTO dto) {
        return Mono.fromCallable(() -> userService.checkLoginValid(request))
            .flatMap(user -> {
                if (user != null) {
                    dto.setUserId(user.getId());
                    return Mono.fromCallable(() -> postService.createPost(dto))
                        .doOnNext(postDTO -> {
                            if (postDTO != null) {
                                messagingTemplate.convertAndSend("/topic/post/new", postDTO);
                            }
                        })
                        .map(MessageWrapper::toResponseEntityOk);
                } else {
                    return Mono.just(MessageWrapper.toResponseEntityOk(null));
                }
            });
    }

//    @PostMapping(value = "/post/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<Object> updatePost(HttpServletRequest request, @ModelAttribute UploadPostReqDTO dto) {
//        User user = userService.checkLoginValid(request);
//        PostDTO postDTO = null;
//        if (user != null) {
//            dto.setUserId(user.getId());
//            postDTO = postService.updatePost(dto);
//            if (postDTO != null) messagingTemplate.convertAndSend("/topic/post/update", postDTO);
//        }
//        return MessageWrapper.toResponseEntityOk(postDTO);
//    }

    @PostMapping(value = "/post/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<Object>> updatePost(HttpServletRequest request, @ModelAttribute UploadPostReqDTO dto) {
        return Mono.fromCallable(() -> userService.checkLoginValid(request))
            .flatMap(user -> {
                if (user != null) {
                    dto.setUserId(user.getId());
                    return Mono.fromCallable(() -> postService.updatePost(dto))
                        .doOnNext(postDTO -> {
                            if (postDTO != null) {
                                messagingTemplate.convertAndSend("/topic/post/update", postDTO);
                            }
                        })
                        .map(MessageWrapper::toResponseEntityOk);
                } else {
                    return Mono.just(MessageWrapper.toResponseEntityOk(null));
                }
            });
    }

    @PostMapping("/post/delete")
    public ResponseEntity<Object> deletePost(HttpServletRequest request, @RequestBody UploadPostReqDTO dto) {
        User user = userService.checkLoginValid(request);
        PostDTO postDTO = null;
        if (user != null) {
            postDTO = postService.deletePost(dto);
            messagingTemplate.convertAndSend("/topic/post/delete", postDTO);
        }
        return MessageWrapper.toResponseEntityOk(postDTO);
    }

    @PostMapping(value = "/postPage")
    public ResponseEntity<Object> postPage(HttpServletRequest request, @RequestBody PostReqDTO dto) {
        User user = userService.checkLoginValid(request);
        PageWrapper<PostDTO> postDTOs = null;
        if (user != null) {
            postDTOs = postService.findAllPublicPostByPage(dto);
        }
        return MessageWrapper.toResponseEntityOk(postDTOs);
    }

    @PostMapping(value = "/post/searchContent")
    public ResponseEntity<Object> searchPostContent(HttpServletRequest request, @RequestBody PostReqDTO dto) {
        User user = userService.checkLoginValid(request);
        PageWrapper<PostDTO> postDTOs = null;
        if (user != null) {
            postDTOs = postService.searchPublicPostByContentAndPage(dto);
        }
        return MessageWrapper.toResponseEntityOk(postDTOs);
    }

    @PostMapping(value = "/post/searchTag")
    public ResponseEntity<Object> searchPostTag(HttpServletRequest request, @RequestBody PostReqDTO dto) {
        User user = userService.checkLoginValid(request);
        PageWrapper<PostDTO> postDTOs = null;
        if (user != null) {
            postDTOs = postService.searchPublicPostByTagAndPage(dto);
        }
        return MessageWrapper.toResponseEntityOk(postDTOs);
    }

    @PostMapping(value = "/profilePostPage/{profileUserId}")
    public ResponseEntity<Object> profilePostPage(HttpServletRequest request, @RequestBody PostReqDTO dto, @PathVariable long profileUserId) {
        User user = userService.checkLoginValid(request);
        PageWrapper<PostDTO> postDTOs = null;
        if (user != null) {
            postDTOs = postService.findPostByPageWithUser(dto, user.getId(), profileUserId);
        }
        return MessageWrapper.toResponseEntityOk(postDTOs);
    }
}
