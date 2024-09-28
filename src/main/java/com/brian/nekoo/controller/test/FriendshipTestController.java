package com.brian.nekoo.controller.test;

import com.brian.nekoo.dto.req.FriendshipReqDTO;
import com.brian.nekoo.service.FriendshipService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test/v1")
@Log4j2
public class FriendshipTestController {

    @Autowired
    private FriendshipService friendshipService;

    @PostMapping("/friendship/findFriendship")
    public ResponseEntity<Object> findFriendship(@RequestBody FriendshipReqDTO dto) {
        log.info(dto);
        return ResponseEntity.ok(
            friendshipService.findFriendship(dto.getSenderUserId(), dto.getReceiverUserId())
        );
    }

    @PostMapping("/friendship/invite")
    public ResponseEntity<Object> invite(@RequestBody FriendshipReqDTO dto) {
        log.info(dto);
        return ResponseEntity.ok(
            friendshipService.invite(dto.getSenderUserId(), dto.getReceiverUserId())
        );
    }

    @PatchMapping("/friendship/pending")
    public ResponseEntity<Object> pending(@RequestBody FriendshipReqDTO dto) {
        log.info(dto);
        return ResponseEntity.ok(
            friendshipService.pending(dto.getFriendshipId())
        );
    }

    @PatchMapping("/friendship/approve")
    public ResponseEntity<Object> approve(@RequestBody FriendshipReqDTO dto) {
        log.info(dto);
        return ResponseEntity.ok(
            friendshipService.approve(dto.getFriendshipId())
        );
    }

    @PatchMapping("/friendship/reject")
    public ResponseEntity<Object> reject(@RequestBody FriendshipReqDTO dto) {
        log.info(dto);
        return ResponseEntity.ok(
            friendshipService.reject(dto.getFriendshipId())
        );
    }
}
