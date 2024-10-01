package com.brian.nekoo.controller;

import com.brian.nekoo.dto.FriendshipDTO;
import com.brian.nekoo.dto.MessageWrapper;
import com.brian.nekoo.dto.req.FriendshipReqDTO;
import com.brian.nekoo.entity.mysql.User;
import com.brian.nekoo.service.FriendshipService;
import com.brian.nekoo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Log4j2
public class FriendshipController {

    private final FriendshipService friendshipService;
    private final UserService userService;

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
        }
        return MessageWrapper.toResponseEntityOk(friendshipDTO);
    }

    @PostMapping("/friendship/pending")
    public ResponseEntity<Object> pending(HttpServletRequest request, @RequestBody FriendshipReqDTO dto) {
        User user = userService.checkLoginValid(request);
        FriendshipDTO friendshipDTO = null;
        if (user != null) {
            friendshipDTO = friendshipService.pending(dto.getFriendshipId());
        }
        return MessageWrapper.toResponseEntityOk(friendshipDTO);
    }

    @PostMapping("/friendship/approve")
    public ResponseEntity<Object> approve(HttpServletRequest request, @RequestBody FriendshipReqDTO dto) {
        User user = userService.checkLoginValid(request);
        FriendshipDTO friendshipDTO = null;
        if (user != null) {
            friendshipDTO = friendshipService.approve(dto.getFriendshipId());
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
