package com.brian.nekoo.controller.test;

import com.brian.nekoo.dto.req.UploadPostReqDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/test/v1")
@Log4j2
public class WebfluxTestController {
    @GetMapping("/webflux/hello")
    public Mono<Object> hello() {
//        return Mono.just("Hello, World!");    //prints Hello, World!
        return Mono.just(ResponseEntity.badRequest());
    }

    @PostMapping(value = "/webflux/upload2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> uploadFile2(@ModelAttribute UploadPostReqDTO dto) {
        log.info(dto.getContent());
        log.info(dto.getPrivacy());
        for (MultipartFile file : dto.getFiles()) {
            log.info(file.getOriginalFilename());
        }
        return Mono.just("asdf");
    }

    @GetMapping("/webflux/numbers")
    public Flux<Integer> numbers() {
        return Flux.range(1, 10)  // 返回 1 到 10 的數字
            .delayElements(Duration.ofSeconds(1));  // 每個數字間隔 1 秒
    }

}
