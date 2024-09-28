package com.brian.nekoo.entity.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "chat_logs")
public class ChatLog {
    @Id
    private String id;

    // 聊天室id
    private long chatroomId;

    // 發訊者
    private long userId;

    // 訊息
    private String content;

    // 資產
    private List<Asset> assets;

    private Instant createAt;

    private Instant modifyAt;

    private Instant removeAt;
}
