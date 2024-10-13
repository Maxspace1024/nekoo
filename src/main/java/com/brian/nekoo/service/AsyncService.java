package com.brian.nekoo.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Log4j2
public class AsyncService {

    @Async("taskExecutor")
    public void asyncBasic() {
        try {
            Thread.sleep(3000);
            log.info("method thread name: " + Thread.currentThread().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async("taskExecutor")
    public void asyncUpload(MultipartFile file) {
        try {
            log.info("file name: " + file.getOriginalFilename());
            if (!file.isEmpty()) {
            }
            log.info("method thread name: " + Thread.currentThread().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void syncBasic() {
        try {
            Thread.sleep(3000);
            log.info("method thread name: " + Thread.currentThread().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
