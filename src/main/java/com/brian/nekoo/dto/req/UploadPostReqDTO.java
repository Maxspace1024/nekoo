package com.brian.nekoo.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostReqDTO {
    private String postId;
    private Long userId;
    private Integer privacy;
    private String content;
    private List<String> hashtags;
    private List<MultipartFile> files;
}
