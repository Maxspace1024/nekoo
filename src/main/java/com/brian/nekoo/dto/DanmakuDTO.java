package com.brian.nekoo.dto;

import com.brian.nekoo.entity.mongo.Danmaku;
import com.brian.nekoo.entity.mysql.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DanmakuDTO {
    private String danmakuId;
    // 發訊者
    private Long userId;
    private String userName;
    private String userAvatarPath;
    // 彈幕資訊
    private String assetId;
    private Integer type;
    private Integer visible;
    private String content;
    private String imagePath;
    // 屬性
    private Integer size;
    private Float posX;
    private Float posY;
    private String color;
    private String backgroundColor;
    private Float appearAt;
    // 時間
    private Instant createAt;
    private Instant modifyAt;

    public static DanmakuDTO getDTO(Danmaku dmk, User user) {
        if (dmk == null || user == null) {
            return null;
        }

        return DanmakuDTO.builder()
            .danmakuId(dmk.getId())
            .userId(user.getId())
            .userName(user.getName())
            .userAvatarPath(user.getAvatarPath())
            .assetId(dmk.getAssetId())
            .type(dmk.getType())
            .visible(dmk.getVisible())
            .content(dmk.getContent())
            .imagePath(dmk.getImagePath())
            // attr
            .size(dmk.getSize())
            .posX(dmk.getPosX())
            .posY(dmk.getPosY())
            .color(dmk.getColor())
            .backgroundColor(dmk.getBackgroundColor())
            .appearAt(dmk.getAppearAt())
            // time
            .createAt(dmk.getCreateAt())
            .modifyAt(dmk.getModifyAt())
            .build();
    }
}
