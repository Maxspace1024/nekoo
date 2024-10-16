package com.brian.nekoo.controller;

import com.brian.nekoo.entity.mysql.User;
import com.brian.nekoo.service.ChatService;
import com.brian.nekoo.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;

@Controller
@Log4j2
public class WsChatroomController {

    private final UserService userService;
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    private final SimpUserRegistry simpUserRegistry;

    public WsChatroomController(UserService userService, ChatService chatService, SimpMessagingTemplate messagingTemplate, SimpUserRegistry simpUserRegistry) {
        this.userService = userService;
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
        this.simpUserRegistry = simpUserRegistry;
    }

    @MessageMapping("/myChatroom")
    public void getPost(String s, SimpMessageHeaderAccessor accessor) {
        User user = userService.checkLoginValid(accessor);
        if (user != null) {
            long userId = user.getId();
            messagingTemplate.convertAndSend("/topic/myChatroom/" + userId, chatService.findChatroomsByUserId(userId));
        }
    }
}
