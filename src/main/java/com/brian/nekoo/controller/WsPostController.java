package com.brian.nekoo.controller;

import com.brian.nekoo.dto.req.PostReqDTO;
import com.brian.nekoo.entity.mysql.User;
import com.brian.nekoo.service.PostService;
import com.brian.nekoo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Log4j2
public class WsPostController {

    private final UserService userService;
    private final PostService postService;
    private final SimpMessagingTemplate messagingTemplate;
    private final SimpUserRegistry simpUserRegistry;

    @MessageMapping("/post")
    public void getPost(SimpMessageHeaderAccessor accessor) {
        messagingTemplate.convertAndSend("/topic/post", postService.findPost());
    }

    @MessageMapping("/post/delete")
    public void deletePost(@Payload PostReqDTO dto, SimpMessageHeaderAccessor accessor) {
        User user = userService.checkLoginValid(accessor);
        if (user != null) {
            long userId = user.getId();
            messagingTemplate.convertAndSend("/topic/post/delete", postService.deletePost(dto));
        }
    }
}
