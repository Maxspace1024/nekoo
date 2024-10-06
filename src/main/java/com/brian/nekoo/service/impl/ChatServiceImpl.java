package com.brian.nekoo.service.impl;

import com.brian.nekoo.dto.ChatLogDTO;
import com.brian.nekoo.dto.ChatroomDTO;
import com.brian.nekoo.dto.PageWrapper;
import com.brian.nekoo.dto.req.ChatLogReqDTO;
import com.brian.nekoo.dto.req.ChatroomReqDTO;
import com.brian.nekoo.entity.mongo.Asset;
import com.brian.nekoo.entity.mongo.ChatLog;
import com.brian.nekoo.entity.mysql.Chatroom;
import com.brian.nekoo.entity.mysql.ChatroomUser;
import com.brian.nekoo.entity.mysql.User;
import com.brian.nekoo.enumx.AssetTypeEnum;
import com.brian.nekoo.enumx.ChatroomTypeEnum;
import com.brian.nekoo.enumx.ReadStateEnum;
import com.brian.nekoo.repository.mongo.AssetRepository;
import com.brian.nekoo.repository.mongo.ChatLogRepository;
import com.brian.nekoo.repository.mysql.ChatroomRepository;
import com.brian.nekoo.repository.mysql.ChatroomUserRepository;
import com.brian.nekoo.repository.mysql.UserRepository;
import com.brian.nekoo.service.ChatService;
import com.brian.nekoo.service.S3Service;
import com.brian.nekoo.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final ChatLogRepository chatLogRepository;
    private final ChatroomRepository chatroomRepository;
    private final ChatroomUserRepository chatroomUserRepository;
    private final AssetRepository assetRepository;

    @Override
    public ChatroomDTO createChatroom(ChatroomReqDTO dto) {
        List<Long> userIds = dto.getUserIds();
        if (userIds.size() < 2) {
            // 噴個例外如何
            return null;
        }

        // 建立聊天室
        Instant now = Instant.now();
        Chatroom chatroom = Chatroom.builder()
            .name(dto.getChatroomName())
            .avatarPath(dto.getAvatarPath())
            .uuid(UUID.randomUUID().toString())
            .createAt(now)
            .modifyAt(now)
            .build();
        chatroom = chatroomRepository.save(chatroom);

        // 找使用者加入群組
        String roomName = dto.getChatroomName();
        int roomType = dto.getChatroomType();
        List<User> users = userRepository.findAllById(userIds);
        List<ChatroomUser> chatroomUsers = new ArrayList<>();

        if (roomType == ChatroomTypeEnum.GROUP.ordinal()) {
            // 群聊
            for (User user : users) {
                chatroomUsers.add(
                    ChatroomUser.builder()
                        .roomName(roomName)
                        .readState(ReadStateEnum.SEEN.ordinal())
                        .user(user)
                        .chatroom(chatroom)
                        .build()
                );
            }
        } else if (roomType == ChatroomTypeEnum.PRIVATE.ordinal() && users.size() == 2) {
            // 私有聊天
            User user1 = users.get(0);
            User user2 = users.get(1);

            // 儲存對方名稱
            ChatroomUser chatroomUser1 = ChatroomUser.builder()
                .roomName(user2.getName())
                .partnerUser(user2)
                .user(user1)
                .chatroom(chatroom)
                .build();
            ChatroomUser chatroomUser2 = ChatroomUser.builder()
                .roomName(user1.getName())
                .partnerUser(user1)
                .user(user2)
                .chatroom(chatroom)
                .build();

            chatroomUsers = Arrays.asList(chatroomUser1, chatroomUser2);
        }
        chatroomUsers = chatroomUserRepository.saveAll(chatroomUsers);
        chatroom.setChatroomUsers(new HashSet<>(chatroomUsers));
        chatroom = chatroomRepository.save(chatroom);

        return ChatroomDTO.builder()
            .chatroomId(chatroom.getId())
            .chatroomName(chatroom.getName())
            .chatroomUuid(chatroom.getUuid())
            .chatroomAvatarPath(chatroom.getAvatarPath())
            .build();
    }

    @Override
    public ChatLogDTO createChatLog(ChatLogReqDTO dto) {
        List<String> uuidFilenames = new ArrayList<>();
        List<Asset> assets = new ArrayList<>();
        ChatLogDTO chatLogDTO = null;
        try {
            if (dto.getFiles() != null) {
                for (MultipartFile file : dto.getFiles()) {
                    String uuidFilename = FileUtil.generateUuidFileName(file.getOriginalFilename());
                    String fileExtension = FileUtil.extractFileExtension(uuidFilename);
                    int assetType = AssetTypeEnum.fromExtension(fileExtension).ordinal();

                    uuidFilenames.add(uuidFilename);
                    s3Service.uploadFile(file, uuidFilename);
                    Asset asset = Asset.builder()
                        .path(uuidFilename)
                        .type(assetType)
                        .size(file.getSize())
                        .build();
                    assets.add(asset);
                }
                assets = assetRepository.saveAll(assets);
            }

            Instant now = Instant.now();
            ChatLog chatLog = ChatLog.builder()
                .userId(dto.getUserId())
                .chatroomId(dto.getChatroomId())
                .content(dto.getContent())
                .assets(assets)
                .createAt(now)
                .modifyAt(now)
                .build();
            chatLog = chatLogRepository.insert(chatLog);

            User user = userRepository.findById(chatLog.getUserId()).get();
            chatLogDTO = ChatLogDTO.getDTO(chatLog, user);
        } catch (Exception e) {
            try {
                for (String uuidFilename : uuidFilenames) {
                    s3Service.deleteFile(uuidFilename);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }

        return chatLogDTO;
    }

    @Override
    public ChatLogDTO deleteChatLog(ChatLogReqDTO dto) {
        Optional<ChatLog> oChatLog = chatLogRepository.findById(dto.getChatLogId());
        ChatLog chatLog = null;
        if (oChatLog.isPresent()) {
            Instant now = Instant.now();
            chatLog = oChatLog.get();
            chatLog.setModifyAt(now);
            chatLog.setRemoveAt(now);
            chatLog = chatLogRepository.save(chatLog);

            User user = userRepository.findById(chatLog.getUserId()).get();
            return ChatLogDTO.getDTO(chatLog, user);
        } else {
            return null;
        }
    }

    @Override
    public ChatLogDTO updateChatLog(ChatLogReqDTO dto) {
        Optional<ChatLog> oChatLog = chatLogRepository.findById(dto.getChatLogId());
        ChatLog chatLog = null;
        if (oChatLog.isPresent()) {
            Instant now = Instant.now();
            chatLog = oChatLog.get();
            String content = dto.getContent();
            if (content != null) {
                chatLog.setContent(content);
            }
            chatLog.setModifyAt(now);
            chatLog = chatLogRepository.save(chatLog);

            User user = userRepository.findById(chatLog.getUserId()).get();
            return ChatLogDTO.getDTO(chatLog, user);
        } else {
            return null;
        }
    }

    @Override
    public List<ChatLogDTO> findChatLogsByChatroomId(ChatLogReqDTO dto) {
        List<ChatLog> chatLogs = chatLogRepository.findAllByChatroomId(dto.getChatroomId(), null).getContent();
        return chatLogs.stream().map(chatlog -> {
                User user = userRepository.findById(chatlog.getUserId()).get();
                return ChatLogDTO.getDTO(chatlog, user);
            }
        ).toList();
    }

    @Override
    public PageWrapper<ChatroomDTO> findChatroomsByUserId(long userId, ChatroomReqDTO dto) {
//        Pageable pageable = PageRequest.of(dto.getPage(), 16);
        Page<ChatroomUser> chatroomUsers = chatroomUserRepository.findByUserId(userId, null);
        List<ChatroomDTO> chatroomDTOs = chatroomUsers.stream()
            .map(chatroomUser -> {
                Chatroom chatroom = chatroomUser.getChatroom();
                ChatLog lastChatLog = chatLogRepository.findFirstByChatroomIdOrderByCreateAtDesc(chatroom.getId());

                User partnerUser = chatroomUser.getPartnerUser();

                ChatroomDTO.ChatroomDTOBuilder builder = ChatroomDTO.builder()
                    .chatroomId(chatroom.getId())
                    .chatroomUuid(chatroom.getUuid());
                if (partnerUser != null) {
                    builder.chatroomName(partnerUser.getName())
                        .chatroomAvatarPath(partnerUser.getAvatarPath());
                }
                if (lastChatLog != null) {
                    User lastUser = userRepository.findById(lastChatLog.getUserId()).get();
                    builder.lastContent(lastChatLog.getContent())
                        .lastCreateAt(lastChatLog.getCreateAt())
                        .lastUserName(lastUser.getName());
                }
                return builder.build();
            }).toList();
        return PageWrapper.<ChatroomDTO>builder()
            .page(chatroomDTOs)
            .totalPages(chatroomUsers.getTotalPages())
            .build();
    }

    @Override
    public List<ChatroomDTO> findUnreadChatroomsByUserId(long userId) {
        List<ChatroomUser> chatroomUsers = chatroomUserRepository.findByUserIdAndReadState(userId, ReadStateEnum.UNREAD.ordinal());
        List<ChatroomDTO> chatroomDTOs = chatroomUsers.stream().map(chatroomUser -> {
            Chatroom chatroom = chatroomUser.getChatroom();
            ChatLog lastChatLog = chatLogRepository.findFirstByChatroomIdOrderByCreateAtDesc(chatroom.getId());
            ChatroomDTO.ChatroomDTOBuilder builder = ChatroomDTO.builder()
                .chatroomId(chatroom.getId())
                .chatroomUuid(chatroom.getUuid())
                .chatroomName(chatroomUser.getRoomName())
                .chatroomAvatarPath(chatroom.getAvatarPath());
            if (lastChatLog != null) {
                builder.lastContent(lastChatLog.getContent())
                    .lastCreateAt(lastChatLog.getCreateAt());
            }
            return builder.build();
        }).toList();

        return chatroomDTOs;
    }

    @Override
    public ChatroomDTO findChatroomByUserIdAndChatroomId(long userId, long chatroomId) {
        Optional<ChatroomUser> oChatroomUser = chatroomUserRepository.findByUserIdAndChatroomId(userId, chatroomId);
        if (oChatroomUser.isPresent()) {
            ChatroomUser chatroomUser = oChatroomUser.get();
            Chatroom chatroom = chatroomUser.getChatroom();
            ChatLog lastChatLog = chatLogRepository.findFirstByChatroomIdOrderByCreateAtDesc(chatroom.getId());
            ChatroomDTO.ChatroomDTOBuilder builder = ChatroomDTO.builder()
                .chatroomId(chatroom.getId())
                .chatroomUuid(chatroom.getUuid())
                .chatroomName(chatroomUser.getRoomName())
                .chatroomAvatarPath(chatroom.getAvatarPath());
            if (lastChatLog != null) {
                builder.lastContent(lastChatLog.getContent())
                    .lastCreateAt(lastChatLog.getCreateAt());
            }
            return builder.build();
        }
        return null;
    }

    @Override
    public Chatroom findChatroomByChatroomId(long chatroomId) {
        return chatroomRepository.findById(chatroomId).get();
    }
}
