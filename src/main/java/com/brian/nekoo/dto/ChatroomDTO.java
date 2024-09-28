package com.brian.nekoo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatroomDTO {
    private Long chatroomId;
    private String chatroomUuid;
    private String chatroomName;
    private String chatroomAvatarPath;
    private String lastContent;
    private Instant lastCreateAt;
}
