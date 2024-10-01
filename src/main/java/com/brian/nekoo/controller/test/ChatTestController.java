package com.brian.nekoo.controller.test;

import com.brian.nekoo.dto.req.ChatroomReqDTO;
import com.brian.nekoo.service.ChatService;
import com.brian.nekoo.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test/v1")
@Log4j2
public class ChatTestController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @PostMapping("/chatroom")
    public ResponseEntity<Object> createChatroom(@RequestBody ChatroomReqDTO dto) {
        return ResponseEntity.ok(
            chatService.createChatroom(dto)
        );
    }
}
