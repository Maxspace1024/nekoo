package com.brian.nekoo.controller.test;

import com.brian.nekoo.dto.req.DanmakuReqDTO;
import com.brian.nekoo.service.DanmakuService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/test/v1")
@Log4j2
public class DanmakuTestController {

    @Autowired
    private DanmakuService danmakuService;

    @PostMapping(value = "/danmaku", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createDanmaku(@ModelAttribute DanmakuReqDTO dto) {
        return ResponseEntity.ok(
            danmakuService.createDanmaku(dto)
        );
    }

    @DeleteMapping(value = "/danmaku", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> deleteDanmaku(@ModelAttribute DanmakuReqDTO dto) {
        return ResponseEntity.ok(
            danmakuService.deleteDanmaku(dto)
        );
    }

    @PatchMapping("/danmaku")
    public ResponseEntity<Object> updateDanmaku(@RequestBody DanmakuReqDTO dto) {
        return ResponseEntity.ok(
            danmakuService.updateDanmaku(dto)
        );
    }
}
