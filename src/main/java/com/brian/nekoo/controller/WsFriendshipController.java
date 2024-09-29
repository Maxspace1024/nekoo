package com.brian.nekoo.controller;

import com.brian.nekoo.dto.FriendshipDTO;
import com.brian.nekoo.dto.req.FriendshipReqDTO;
import com.brian.nekoo.entity.mysql.User;
import com.brian.nekoo.service.FriendshipService;
import com.brian.nekoo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class WsFriendshipController {

    private final UserService userService;
    private final FriendshipService friendshipService;
    private final SimpMessagingTemplate messagingTemplate;
    private final SimpUserRegistry simpUserRegistry;

    @MessageMapping("/friendship")
    public void friendship(@Payload FriendshipReqDTO dto, SimpMessageHeaderAccessor accessor) {
        User user = userService.checkLoginValid(accessor);
        if (user != null) {
            long userId = user.getId();
            messagingTemplate.convertAndSend("/topic/friendship/" + userId, List.of(FriendshipDTO.builder().build()));
        }
    }
}
