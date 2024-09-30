package com.brian.nekoo.service;

import com.brian.nekoo.dto.FriendshipDTO;

import java.util.List;

public interface FriendshipService {
    FriendshipDTO findFriendship(long userId1, long userId2);

    FriendshipDTO invite(long senderUserId, long receiverUserId);

    FriendshipDTO pending(long friendId);

    FriendshipDTO approve(long friendId);

    FriendshipDTO reject(long friendId);

    FriendshipDTO update(long friendId, int state);

    // 找交友通知
    List<FriendshipDTO> findFriendshipsNotification(long currUserId);

    List<FriendshipDTO> findFriendshipsWithName(long currUserId, String searchName);

    List<FriendshipDTO> findNoFriendshipsWithName(long currUserId, String searchName);

    // 人名搜尋交友
    List<FriendshipDTO> findAllFriendshipsWithName(long currUserId, String searchName);
}
