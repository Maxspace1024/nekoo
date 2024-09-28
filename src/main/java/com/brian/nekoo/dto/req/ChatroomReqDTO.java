package com.brian.nekoo.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatroomReqDTO {
    private Long chatroomId;
    private String chatroomName;
    private int chatroomType;
    private String avatarPath;
    private List<Long> userIds;
}
