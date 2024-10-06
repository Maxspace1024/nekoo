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

    @PostMapping(value = "/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createPost(HttpServletRequest request, @ModelAttribute UploadPostReqDTO dto) {
        User user = userService.checkLoginValid(request);
        PostDTO postDTO = null;
        if (user != null) {
            dto.setUserId(user.getId());
            postDTO = postService.createPost(dto);
            if (postDTO != null) messagingTemplate.convertAndSend("/topic/post/new", postDTO);
        }
        return MessageWrapper.toResponseEntityOk(postDTO);
    }

    @PostMapping(value = "/post/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updatePost(HttpServletRequest request, @ModelAttribute UploadPostReqDTO dto) {
        User user = userService.checkLoginValid(request);
        PostDTO postDTO = null;
        if (user != null) {
            postDTO = postService.updatePost(dto);
            if (postDTO != null) messagingTemplate.convertAndSend("/topic/post/update", postDTO);
        }
        return MessageWrapper.toResponseEntityOk(postDTO);
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
            postDTOs = postService.findPostByPage(dto);
        }
        return MessageWrapper.toResponseEntityOk(postDTOs);
    }
}
