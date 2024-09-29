package com.brian.nekoo.service.impl;

import com.brian.nekoo.dto.FriendshipDTO;
import com.brian.nekoo.dto.req.ChatroomReqDTO;
import com.brian.nekoo.entity.mysql.Friendship;
import com.brian.nekoo.entity.mysql.User;
import com.brian.nekoo.enumx.ChatroomTypeEnum;
import com.brian.nekoo.enumx.FriendshipStateEnum;
import com.brian.nekoo.repository.mysql.FriendshipRepository;
import com.brian.nekoo.repository.mysql.UserRepository;
import com.brian.nekoo.service.ChatService;
import com.brian.nekoo.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendshipServcieImpl implements FriendshipService {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final ChatService chatService;

    @Override
    public FriendshipDTO findFriendship(long userId1, long userId2) {
        Optional<Friendship> oFriendship = friendshipRepository.findFriendship(userId1, userId2);
        if (oFriendship.isPresent()) {
            Friendship friendship = oFriendship.get();
            return FriendshipDTO.getDTO(friendship);
        } else {
            return null;
        }
    }

    @Override
    public FriendshipDTO invite(long senderUserId, long receiverUserId) {
        // 使用先前建立的舊交友邀請紀錄
        Optional<Friendship> oFriendship = friendshipRepository.findFriendship(senderUserId, receiverUserId);
        if (oFriendship.isPresent()) {
            Instant now = Instant.now();
            Friendship friendship = oFriendship.get();
            friendship.setState(FriendshipStateEnum.PENDING.ordinal());
            friendship.setModifyAt(now);
            friendship = friendshipRepository.save(friendship);
            return FriendshipDTO.getDTO(friendship);
        }

        // 沒有建立一個新的交友邀請
        Optional<User> senderUser = userRepository.findById(senderUserId);
        Optional<User> receiverUser = userRepository.findById(receiverUserId);
        Friendship friendship = null;
        if (senderUser.isPresent() && receiverUser.isPresent()) {
            Instant now = Instant.now();
            friendship = Friendship.builder()
                .senderUser(senderUser.get())
                .receiverUser(receiverUser.get())
                .state(FriendshipStateEnum.PENDING.ordinal())
                .createAt(now)
                .modifyAt(now)
                .build();
            friendship = friendshipRepository.save(friendship);
        }
        return FriendshipDTO.getDTO(friendship);
    }

    @Override
    public FriendshipDTO pending(long friendId) {
        Optional<Friendship> oFriend = friendshipRepository.findById(friendId);
        Friendship friendship = null;
        if (oFriend.isPresent()) {
            Instant now = Instant.now();
            friendship = oFriend.get();
            friendship.setState(FriendshipStateEnum.PENDING.ordinal());
            friendship.setModifyAt(now);
            friendship = friendshipRepository.save(friendship);
        } else {

        }
        return FriendshipDTO.getDTO(friendship);
    }

    @Override
    public FriendshipDTO approve(long friendId) {
        Optional<Friendship> oFriend = friendshipRepository.findById(friendId);
        Friendship friendship = null;
        if (oFriend.isPresent()) {
            Instant now = Instant.now();
            friendship = oFriend.get();
            friendship.setState(FriendshipStateEnum.APPROVED.ordinal());
            friendship.setModifyAt(now);
            friendship = friendshipRepository.save(friendship);

            User senderUser = friendship.getSenderUser();
            User receiverUser = friendship.getReceiverUser();
            ChatroomReqDTO dto = ChatroomReqDTO.builder()
                .userIds(Arrays.asList(senderUser.getId(), receiverUser.getId()))
                .chatroomType(ChatroomTypeEnum.PRIVATE.ordinal())
                .chatroomName("")
                .build();
            chatService.createChatroom(dto);
        } else {

        }
        return FriendshipDTO.getDTO(friendship);
    }

    @Override
    public FriendshipDTO reject(long friendId) {
        Optional<Friendship> oFriend = friendshipRepository.findById(friendId);
        Friendship friendship = null;
        if (oFriend.isPresent()) {
            Instant now = Instant.now();
            friendship = oFriend.get();
            friendship.setState(FriendshipStateEnum.REJECTED.ordinal());
            friendship.setModifyAt(now);
            friendship = friendshipRepository.save(friendship);
        } else {

        }
        return FriendshipDTO.getDTO(friendship);
    }

    @Override
    public List<FriendshipDTO> findFriendshipsWithName(long currUserId, String searchName) {
        List<Friendship> friendships = friendshipRepository.findFriendshipsWithName(currUserId, searchName);
        return friendships.stream().map(
            FriendshipDTO::getDTO
        ).toList();
    }

    @Override
    public List<FriendshipDTO> findFriendshipsNotification(long currUserId) {
        List<Friendship> friendships = friendshipRepository.findFriendshipsNotification(currUserId);
        return friendships.stream().map(
            FriendshipDTO::getDTO
        ).toList();
    }

    @Override
    public List<FriendshipDTO> findNoFriendshipsWithName(long currUserId, String searchName) {
        List<User> users = userRepository.findNoFriendshipsWithName(currUserId, searchName);
        return users.stream().map(
            user -> FriendshipDTO.builder()
                .senderUserId(user.getId())
                .senderUserName(user.getName())
                .senderUserAvatarPath(user.getAvatarPath())
                .friendshipState(FriendshipStateEnum.NONE.ordinal())
                .build()
        ).toList();
    }

    @Override
    public List<FriendshipDTO> findAllFriendshipsWithName(long currUserId, String searchName) {
        List<FriendshipDTO> dtos = new ArrayList<>();
        dtos.addAll(findNoFriendshipsWithName(currUserId, searchName));
        dtos.addAll(findFriendshipsWithName(currUserId, searchName));
        return dtos;
    }
}
