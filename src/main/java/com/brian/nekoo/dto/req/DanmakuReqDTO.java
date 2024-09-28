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
public class DanmakuReqDTO {
    private String danmakuId;

    // 發訊者
    private Long userId;

    // 本彈幕對哪個影片/圖片進行留言
    private String assetId;

    // 彈幕型態(動態/靜態)
    private Integer type;

    // 彈幕顯示狀態
    private Integer visible;

    // 彈幕內文
    private String content;

    // 彈幕圖片
    private MultipartFile image;

    // 彈幕大小(相對性的大中小)
    private Integer size;

    // 彈幕x軸座標
    private Float posX;

    // 彈幕y軸座標
    private Float posY;

    // 彈幕文字顏色
    private String color;

    // 彈幕標籤底色
    private String backgroundColor;

    // 彈幕出現時間
    private Float appearAt;

    private Instant createAt;

    private Instant modifyAt;
}
