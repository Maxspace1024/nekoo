package com.brian.nekoo.controller;

import com.brian.nekoo.dto.MessageWrapper;
import com.brian.nekoo.dto.PostDTO;
import com.brian.nekoo.dto.req.PostReqDTO;
import com.brian.nekoo.entity.mysql.User;
import com.brian.nekoo.service.PostService;
import com.brian.nekoo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping(value = "/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createPost(HttpServletRequest request, @ModelAttribute PostReqDTO dto) {
        String authToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        MessageWrapper<Object> mw = userService.checkLoginValidation(authToken);
        if (mw.isSuccess()) {
            User user = (User) mw.getData();
            dto.setUserId(user.getId());

            PostDTO postDTO = postService.createPost(dto);
            messagingTemplate.convertAndSend("/topic/post/new", postDTO);
        }

        mw.setData(null);
        return ResponseEntity.ok(
            mw
        );
    }
}
