package com.brian.nekoo.controller;

import com.brian.nekoo.service.PostService;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;

@Controller
@Log4j2
public class WsPostController {

    private final PostService postService;
    private final SimpMessagingTemplate messagingTemplate;
    private final SimpUserRegistry simpUserRegistry;

    public WsPostController(PostService postService, SimpMessagingTemplate messagingTemplate, SimpUserRegistry simpUserRegistry) {
        this.postService = postService;
        this.messagingTemplate = messagingTemplate;
        this.simpUserRegistry = simpUserRegistry;
    }

    @MessageMapping("/post")
    public void getPost(String s, SimpMessageHeaderAccessor accessor) {
        log.info(s);
        messagingTemplate.convertAndSend("/topic/post", postService.findPost());
    }
}
