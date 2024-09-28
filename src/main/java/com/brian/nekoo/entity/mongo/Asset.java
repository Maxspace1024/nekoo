package com.brian.nekoo.entity.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "assets")
public class Asset {
    @Id
    private String id;

    // 資產型態(圖片/影片/音訊/檔案)
    private int type;

    // 資產存放路徑
    private String path;

    // 資產檔案大小
    private long size;
}
