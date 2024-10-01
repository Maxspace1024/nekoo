package com.brian.nekoo.controller;

import com.brian.nekoo.dto.DanmakuDTO;
import com.brian.nekoo.dto.MessageWrapper;
import com.brian.nekoo.dto.req.DanmakuReqDTO;
import com.brian.nekoo.entity.mysql.User;
import com.brian.nekoo.service.DanmakuService;
import com.brian.nekoo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Log4j2
public class DanmakuController {

    private final DanmakuService danmakuService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping(value = "/danmaku", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadDanmaku(HttpServletRequest request, @ModelAttribute DanmakuReqDTO dto) {
        User user = userService.checkLoginValid(request);
        DanmakuDTO danmakuDTO = null;
        if (user != null) {
            long userId = user.getId();
            dto.setUserId(userId);
            danmakuDTO = danmakuService.createDanmaku(dto);
            if (danmakuDTO != null) {
                log.info("/topic/asset/" + dto.getAssetId());
                messagingTemplate.convertAndSend("/topic/asset/" + dto.getAssetId(), danmakuDTO);
//                還要在總計數通知彈幕數量加一
            }
        }
        return MessageWrapper.toResponseEntityOk(danmakuDTO);
    }

    @PostMapping(value = "/danmaku/log")
    public ResponseEntity<Object> findDanmakuLog(HttpServletRequest request, @ModelAttribute DanmakuReqDTO dto) {
        User user = userService.checkLoginValid(request);
        List<DanmakuDTO> danmakuDTOs = null;
        if (user != null) {
            long userId = user.getId();
            dto.setUserId(userId);
            danmakuDTOs = danmakuService.findDanmakusByAssetId(dto);
        }
        return MessageWrapper.toResponseEntityOk(danmakuDTOs);
    }
}
