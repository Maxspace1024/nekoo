package com.brian.nekoo.service;

import com.brian.nekoo.dto.FriendshipDTO;

public interface FriendshipService {
    FriendshipDTO findFriendship(long userId1, long userId2);

    FriendshipDTO invite(long senderUserId, long receiverUserId);

    FriendshipDTO pending(long friendId);

    FriendshipDTO approve(long friendId);

    FriendshipDTO reject(long friendId);
}
