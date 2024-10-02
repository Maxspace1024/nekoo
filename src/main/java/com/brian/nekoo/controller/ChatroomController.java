package com.brian.nekoo.controller;

import com.brian.nekoo.dto.ChatLogDTO;
import com.brian.nekoo.dto.ChatroomDTO;
import com.brian.nekoo.dto.MessageWrapper;
import com.brian.nekoo.dto.PageWrapper;
import com.brian.nekoo.dto.req.ChatLogReqDTO;
import com.brian.nekoo.dto.req.ChatroomReqDTO;
import com.brian.nekoo.entity.mysql.User;
import com.brian.nekoo.service.ChatService;
import com.brian.nekoo.service.PostService;
import com.brian.nekoo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ChatroomController {

    private final PostService postService;
    private final UserService userService;
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/chatroomPage")
    public ResponseEntity<Object> chatroomPage(HttpServletRequest request, @RequestBody ChatroomReqDTO dto) {
        User user = userService.checkLoginValid(request);
        PageWrapper<ChatroomDTO> chatroomDTOs = null;
        if (user != null) {
            long userId = user.getId();
            chatroomDTOs = chatService.findChatroomsByUserId(userId, dto);
        }
        return MessageWrapper.toResponseEntityOk(chatroomDTOs);
    }

    @PostMapping(value = "/chat", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> chat(HttpServletRequest request, @ModelAttribute ChatLogReqDTO dto) {
        User user = userService.checkLoginValid(request);
        ChatLogDTO chatLogDTO = null;
        if (user != null) {
            long userId = user.getId();
            dto.setUserId(userId);
            chatLogDTO = chatService.createChatLog(dto);
            if (chatLogDTO != null)
                messagingTemplate.convertAndSend("/topic/chatroom/" + dto.getChatroomUuid(), chatLogDTO);
        }
        return MessageWrapper.toResponseEntityOk(chatLogDTO);
    }
}
