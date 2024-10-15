package com.brian.nekoo.dto;

import com.brian.nekoo.entity.mysql.Friendship;
import com.brian.nekoo.entity.mysql.User;
import com.brian.nekoo.enumx.FriendshipStateEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendshipDTO {
    private Long friendshipId;
    // sender user
    private Long senderUserId;
    private String senderUserName;
    private String senderUserEmail;
    private String senderUserAvatarPath;
    // receiver user
    private long receiverUserId;
    private String receiverUserName;
    private String receiverUserEmail;
    private String receiverUserAvatarPath;
    // state
    private int friendshipState;
    // time info
    private Instant createAt;
    private Instant modifyAt;

    public static FriendshipDTO getDTO(Friendship friendship) {
        if (friendship == null) {
            return null;
        }

        return FriendshipDTO.builder()
            .friendshipId(friendship.getId())
            .senderUserId(friendship.getSenderUser().getId())
            .senderUserName(friendship.getSenderUser().getName())
            .senderUserEmail(friendship.getSenderUser().getEmail())
            .senderUserAvatarPath(friendship.getSenderUser().getAvatarPath())
            .receiverUserId(friendship.getReceiverUser().getId())
            .receiverUserName(friendship.getReceiverUser().getName())
            .receiverUserEmail(friendship.getReceiverUser().getEmail())
            .receiverUserAvatarPath(friendship.getReceiverUser().getAvatarPath())
            .friendshipState(friendship.getState())
            .createAt(friendship.getCreateAt())
            .modifyAt(friendship.getModifyAt())
            .build();
    }

    public static FriendshipDTO getNoneStateDTO(User receiverUser) {
        if (receiverUser == null) {
            return null;
        }
        return FriendshipDTO.builder()
            .receiverUserId(receiverUser.getId())
            .receiverUserName(receiverUser.getName())
            .receiverUserEmail(receiverUser.getEmail())
            .receiverUserAvatarPath(receiverUser.getAvatarPath())
            .friendshipState(FriendshipStateEnum.NONE.ordinal())
            .build();
    }
}
