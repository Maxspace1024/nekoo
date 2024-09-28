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
@Document(collection = "posts")
public class Post {

    @Id
    private String id;

    // 發文者
    private long userId;

    // 貼文隱私
    private int privacy;

    // 貼文內文
    private String content;

    // 標籤
    private List<String> hashtags;

    // 檔案資產
    private List<Asset> assets;

    private Instant createAt;

    private Instant modifyAt;

    private Instant removeAt;
}
