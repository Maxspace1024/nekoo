package com.brian.nekoo.controller;

import com.brian.nekoo.dto.ChatLogDTO;
import com.brian.nekoo.dto.ChatroomDTO;
import com.brian.nekoo.dto.MessageWrapper;
import com.brian.nekoo.dto.PageWrapper;
import com.brian.nekoo.dto.req.ChatLogReqDTO;
import com.brian.nekoo.dto.req.ChatroomReqDTO;
import com.brian.nekoo.entity.mysql.Chatroom;
import com.brian.nekoo.entity.mysql.ChatroomUser;
import com.brian.nekoo.entity.mysql.User;
import com.brian.nekoo.enumx.ReadStateEnum;
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

import java.util.ArrayList;
import java.util.List;

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
            chatLogDTO = chatService.createChatLog(dto);                                                // 留下對話紀錄
            Chatroom chatroom = chatService.updateModifyAtByChatroomId(chatLogDTO.getChatroomId());     // 更新聊天室修改時間
            List<ChatroomUser> chatroomUsers = chatService.updateReadState(userId, chatroom.getId(), ReadStateEnum.UNREAD.ordinal()); // 設定未讀
            if (chatLogDTO != null && chatroom != null)
                messagingTemplate.convertAndSend("/topic/chatroom/" + dto.getChatroomUuid(), chatLogDTO);
        }
        return MessageWrapper.toResponseEntityOk(chatLogDTO);
    }

    @PostMapping(value = "/chat/seen")
    public ResponseEntity<Object> chatSeen(HttpServletRequest request, @RequestBody ChatLogReqDTO dto) {
        User user = userService.checkLoginValid(request);
        ChatroomDTO chatroomDTO = null;
        if (user != null) {
            long userId = user.getId();
            dto.setUserId(userId);
            Chatroom chatroom = chatService.updateModifyAtByChatroomId(dto.getChatroomId());     // 更新聊天室修改時間
            ChatroomUser chatroomUser = chatService.updateSelfReadState(userId, chatroom.getId(), ReadStateEnum.SEEN.ordinal()); // 設定已讀
        }
        return MessageWrapper.toResponseEntityOk(chatroomDTO);
    }

    @PostMapping(value = "/chat/log")
    public ResponseEntity<Object> chatLog(HttpServletRequest request, @RequestBody ChatLogReqDTO dto) {
        User user = userService.checkLoginValid(request);
        List<ChatLogDTO> chatLogDTOs = new ArrayList<>();
        if (user != null) {
            chatLogDTOs = chatService.findChatLogsByChatroomId(dto);
        }
        return MessageWrapper.toResponseEntityOk(chatLogDTOs);
    }
}
