package com.brian.nekoo.entity.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "danmakus")
public class Danmaku {

    @Id
    private String id;

    // 發彈幕者
    private Long userId;

    // 本彈幕對哪個影片/圖片進行留言
    private String assetId;

    // 彈幕型態(動態/靜態)
    private Integer type;

    // 彈幕可見狀態
    private Integer visible;

    // 彈幕訊息
    private String content;

    // 彈幕圖片
    private String imagePath;

    // 彈幕x軸座標
    private Float posX;

    // 彈幕y軸座標
    private Float posY;

    // 彈幕文字顏色
    private String color;

    // 彈幕標籤底色
    private String backgroundColor;

    // 彈幕大小(相對性的大中小)
    private Integer size;

    // 彈幕出現時間
    private Float appearAt;

    private Instant createAt;

    private Instant modifyAt;

    private Instant removeAt;
}
