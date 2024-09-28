package com.brian.nekoo.dto;

import com.brian.nekoo.entity.mongo.Asset;
import com.brian.nekoo.entity.mongo.Post;
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
public class PostDTO {
    private String postId;
    // 發文者訊息
    private Long userId;
    private String userName;
    private String userAvatarPath;
    // 內容
    private int privacy;
    private String content;
    private List<String> hashtags;
    private List<Asset> assets;
    private Long totalDanmakuCount;
    // 時間
    private Instant createAt;
    private Instant modifyAt;

    public static PostDTO getDTO(Post post, User user) {
        if (post == null || user == null) {
            return null;
        }

        return PostDTO.builder()
            .postId(post.getId())
            .userId(user.getId())
            .userName(user.getName())
            .userAvatarPath(user.getAvatarPath())
            .hashtags(post.getHashtags())
            .privacy(post.getPrivacy())
            .content(post.getContent())
            .assets(post.getAssets())
            .createAt(post.getCreateAt())
            .modifyAt(post.getModifyAt())
            .build();
    }
}
