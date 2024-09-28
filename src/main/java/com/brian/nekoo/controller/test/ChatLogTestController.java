package com.brian.nekoo.controller.test;

import com.brian.nekoo.dto.MessageWrapper;
import com.brian.nekoo.dto.req.ChatLogReqDTO;
import com.brian.nekoo.entity.mysql.User;
import com.brian.nekoo.service.ChatService;
import com.brian.nekoo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/test/v1")
@Log4j2
public class ChatLogTestController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/chatLog", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createChatLog(HttpServletRequest request, @ModelAttribute ChatLogReqDTO dto) {
        String authToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        MessageWrapper<Object> mw = userService.checkLoginValidation(authToken);
        if (mw.isSuccess()) {
            User user = (User) mw.getData();
            dto.setUserId(user.getId());
            return ResponseEntity.ok(
                chatService.createChatLog(dto)
            );
        } else {
            return ResponseEntity.ok(
                mw
            );
        }
    }

    @DeleteMapping("/chatLog")
    public ResponseEntity<Object> deleteChatLog(@RequestBody ChatLogReqDTO dto) {
        return ResponseEntity.ok(
            chatService.deleteChatLog(dto)
        );
    }

    @PatchMapping("/chatLog")
    public ResponseEntity<Object> updateChatLog(@RequestBody ChatLogReqDTO dto) {
        return ResponseEntity.ok(
            chatService.updateChatLog(dto)
        );
    }
}
