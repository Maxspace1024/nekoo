package com.brian.nekoo.controller.test;

import com.brian.nekoo.dto.req.UploadPostReqDTO;
import com.brian.nekoo.service.PostService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/test/v1")
@Log4j2
public class PostTestController {

    @Autowired
    private PostService postService;

    @PostMapping(value = "/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createPost(@ModelAttribute UploadPostReqDTO dto) {
        return ResponseEntity.ok(
            postService.createPost(dto)
        );
    }

    @DeleteMapping("/post")
    public ResponseEntity<Object> deletePost(@RequestBody UploadPostReqDTO dto) {
        return ResponseEntity.ok(
            postService.deletePost(dto)
        );
    }

    @PatchMapping("/post/privacy")
    public ResponseEntity<Object> updatePrivacy(@RequestBody UploadPostReqDTO dto) {
        return ResponseEntity.ok(
            postService.updatePost(dto)
        );
    }

    @PatchMapping("/post")
    public ResponseEntity<Object> updateContent(@RequestBody UploadPostReqDTO dto) {
        return ResponseEntity.ok(
            postService.updatePost(dto)
        );
    }
}
