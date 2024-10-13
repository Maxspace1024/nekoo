package com.brian.nekoo.controller.test;

import com.brian.nekoo.service.AsyncService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/test/v1")
@Log4j2
public class AsyncTestController {

    @Autowired
    AsyncService asyncService;

    @GetMapping("/async/basic")
    public ResponseEntity<Object> getAsyncBasic() {
        asyncService.asyncBasic();
        log.info("endpoint thread name: " + Thread.currentThread().getName());
        return ResponseEntity.ok("async hello");
    }

    @GetMapping("/sync/basic")
    public ResponseEntity<Object> getSyncBasic() {
        asyncService.syncBasic();
        log.info("endpoint thread name: " + Thread.currentThread().getName());
        return ResponseEntity.ok("sync hello");
    }

    @PostMapping("/async/upload.html")
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file) throws IOException {
        log.info("endpoint thread name: " + Thread.currentThread().getName());
        return ResponseEntity.ok("file upload.html 2");
    }
}
