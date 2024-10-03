package com.brian.nekoo.controller;

import com.brian.nekoo.dto.ChatroomDTO;
import com.brian.nekoo.dto.FriendshipDTO;
import com.brian.nekoo.dto.MessageWrapper;
import com.brian.nekoo.dto.req.FriendshipReqDTO;
import com.brian.nekoo.entity.mysql.User;
import com.brian.nekoo.service.ChatService;
import com.brian.nekoo.service.FriendshipService;
import com.brian.nekoo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Log4j2
public class FriendshipController {

    private final ChatService chatService;
    private final FriendshipService friendshipService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/friendship/findFriendship")
    public ResponseEntity<Object> findFriendship(HttpServletRequest request, @RequestBody FriendshipReqDTO dto) {
        User user = userService.checkLoginValid(request);
        FriendshipDTO friendshipDTO = null;
        if (user != null) {
            friendshipDTO = friendshipService.findFriendship(dto.getSenderUserId(), dto.getReceiverUserId());
        }
        return MessageWrapper.toResponseEntityOk(friendshipDTO);
    }

    @PostMapping("/friendship/invite")
    public ResponseEntity<Object> invite(HttpServletRequest request, @RequestBody FriendshipReqDTO dto) {
        User user = userService.checkLoginValid(request);
        FriendshipDTO friendshipDTO = null;
        if (user != null) {
            friendshipDTO = friendshipService.invite(dto.getSenderUserId(), dto.getReceiverUserId());
            if (friendshipDTO != null)
                messagingTemplate.convertAndSend("/topic/friendship/new/" + user.getId(), friendshipDTO);
        }
        return MessageWrapper.toResponseEntityOk(friendshipDTO);
    }

    @PostMapping("/friendship/pending")
    public ResponseEntity<Object> pending(HttpServletRequest request, @RequestBody FriendshipReqDTO dto) {
        User user = userService.checkLoginValid(request);
        FriendshipDTO friendshipDTO = null;
        if (user != null) {
            friendshipDTO = friendshipService.pending(dto.getFriendshipId());
            if (friendshipDTO != null)
                messagingTemplate.convertAndSend("/topic/friendship/new/" + user.getId(), friendshipDTO);
        }
        return MessageWrapper.toResponseEntityOk(friendshipDTO);
    }

    @PostMapping("/friendship/approve")
    public ResponseEntity<Object> approve(HttpServletRequest request, @RequestBody FriendshipReqDTO dto) {
        User user = userService.checkLoginValid(request);
        FriendshipDTO friendshipDTO = null;
        ChatroomDTO chatroomDTO = null;
        ChatroomDTO senderChatroomDTO = null;
        ChatroomDTO receiverChatroomDTO = null;
        if (user != null) {
            Map<String, Object> map = friendshipService.approve(dto.getFriendshipId());
            friendshipDTO = (FriendshipDTO) map.get("friendshipDTO");
            chatroomDTO = (ChatroomDTO) map.get("chatroomDTO");
            senderChatroomDTO = chatService.findChatroomByUserIdAndChatroomId(friendshipDTO.getSenderUserId(), chatroomDTO.getChatroomId());
            receiverChatroomDTO = chatService.findChatroomByUserIdAndChatroomId(friendshipDTO.getReceiverUserId(), chatroomDTO.getChatroomId());
            if (senderChatroomDTO != null && receiverChatroomDTO != null) {
                messagingTemplate.convertAndSend("/topic/chatroom/new/" + friendshipDTO.getSenderUserId(), senderChatroomDTO);
                messagingTemplate.convertAndSend("/topic/chatroom/new/" + friendshipDTO.getReceiverUserId(), receiverChatroomDTO);
            }
        }
        return MessageWrapper.toResponseEntityOk(friendshipDTO);
    }

    @PostMapping("/friendship/reject")
    public ResponseEntity<Object> reject(HttpServletRequest request, @RequestBody FriendshipReqDTO dto) {
        User user = userService.checkLoginValid(request);
        FriendshipDTO friendshipDTO = null;
        if (user != null) {
            friendshipDTO = friendshipService.reject(dto.getFriendshipId());
        }
        return MessageWrapper.toResponseEntityOk(friendshipDTO);
    }

    @PostMapping("/friendship/search")
    public ResponseEntity<Object> searchFriendships(HttpServletRequest request, @RequestBody FriendshipReqDTO dto) {
        User user = userService.checkLoginValid(request);
        List<FriendshipDTO> friendshipDTOs = new ArrayList<>();
        if (user != null) {
            friendshipDTOs = friendshipService.findAllFriendshipsWithName(user.getId(), dto.getSearchName());
        }
        return MessageWrapper.toResponseEntityOk(friendshipDTOs);
    }

    @PostMapping("/friendship/searchNotification")
    public ResponseEntity<Object> searchFriendshipsNotification(HttpServletRequest request, @RequestBody FriendshipReqDTO dto) {
        User user = userService.checkLoginValid(request);
        List<FriendshipDTO> friendshipDTOs = new ArrayList<>();
        if (user != null) {
            friendshipDTOs = friendshipService.findFriendshipsNotification(user.getId());
        }
        return MessageWrapper.toResponseEntityOk(friendshipDTOs);
    }
}
