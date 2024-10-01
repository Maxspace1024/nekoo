package com.brian.nekoo.controller;

import com.brian.nekoo.dto.req.ChatLogReqDTO;
import com.brian.nekoo.entity.mysql.Chatroom;
import com.brian.nekoo.entity.mysql.User;
import com.brian.nekoo.service.ChatService;
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
public class WsChatroomController {

    private final UserService userService;
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    private final SimpUserRegistry simpUserRegistry;

    @MessageMapping("/chatroom")
    public void chat(@Payload ChatLogReqDTO dto, SimpMessageHeaderAccessor accessor) {
        User user = userService.checkLoginValid(accessor);
        if (user != null) {
            long userId = user.getId();
            dto.setUserId(userId);
            long chatroomId = dto.getChatroomId();
            Chatroom chatroom = chatService.findChatroomByChatroomId(chatroomId);
            String uuid = chatroom.getUuid();
            messagingTemplate.convertAndSend("/topic/chatroom/" + uuid, chatService.createChatLog(dto));
        }
    }

    @MessageMapping("/message/notification")
    public void messageNotification(SimpMessageHeaderAccessor accessor) {
        User user = userService.checkLoginValid(accessor);
        if (user != null) {
            long userId = user.getId();
            messagingTemplate.convertAndSend("/topic/message/notification/" + userId, chatService.findUnreadChatroomsByUserId(userId));
        }
    }
}
