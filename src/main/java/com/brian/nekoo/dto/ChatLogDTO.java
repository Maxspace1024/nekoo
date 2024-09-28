package com.brian.nekoo.dto;

import com.brian.nekoo.entity.mongo.Asset;
import com.brian.nekoo.entity.mongo.ChatLog;
import com.brian.nekoo.entity.mysql.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatLogDTO {
    private String chatLogId;
    private Long chatroomId;
    // 發訊者
    private Long userId;
    private String userName;
    private String userAvatarPath;
    // 內容
    private String content;
    private List<Asset> assets;
    // 時間
    private Instant createAt;
    private Instant modifyAt;

    public static ChatLogDTO getDTO(ChatLog chatLog, User user) {
        if (chatLog == null || user == null) {
            return null;
        }

        return ChatLogDTO.builder()
            .chatLogId(chatLog.getId())
            .chatroomId(chatLog.getChatroomId())
            .userId(user.getId())
            .userName(user.getName())
            .userAvatarPath(user.getAvatarPath())
            .content(chatLog.getContent())
            .assets(chatLog.getAssets())
            .createAt(chatLog.getCreateAt())
            .modifyAt(chatLog.getModifyAt())
            .build();
    }
}
