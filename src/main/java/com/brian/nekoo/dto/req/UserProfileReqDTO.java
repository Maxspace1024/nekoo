package com.brian.nekoo.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileReqDTO {
    private Long userId;
    private String userName;
    private Instant birthday;
    private String gender;
    private String location;
    private String content;
    private MultipartFile image;
}
