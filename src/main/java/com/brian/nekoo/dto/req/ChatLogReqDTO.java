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
public class ChatLogReqDTO {
    private String chatLogId;
    private Long chatroomId;
    private String chatroomUuid;
    private Long userId;
    private String content;
    private List<MultipartFile> files;
}
