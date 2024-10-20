package com.brian.nekoo.service;

import com.brian.nekoo.dto.ChatLogDTO;
import com.brian.nekoo.dto.ChatroomDTO;
import com.brian.nekoo.dto.PageWrapper;
import com.brian.nekoo.dto.req.ChatLogReqDTO;
import com.brian.nekoo.dto.req.ChatroomReqDTO;
import com.brian.nekoo.entity.mysql.Chatroom;
import com.brian.nekoo.entity.mysql.ChatroomUser;

import java.util.List;

public interface ChatService {
    ChatroomDTO createChatroom(ChatroomReqDTO dto);

    ChatLogDTO createChatLog(ChatLogReqDTO dto);

    ChatLogDTO deleteChatLog(ChatLogReqDTO dto);

    ChatLogDTO updateChatLog(ChatLogReqDTO dto);

    List<ChatLogDTO> findChatLogsByChatroomId(ChatLogReqDTO dto);

    PageWrapper<ChatroomDTO> findChatroomsByUserId(long userId, ChatroomReqDTO dto);

    List<ChatroomDTO> findUnreadChatroomsByUserId(long userId);

    ChatroomDTO findChatroomByUserIdAndChatroomId(long userId, long chatroomId);

    Chatroom findChatroomByChatroomId(long chatroomId);

    Chatroom updateModifyAtByChatroomId(long chatroomId);

    List<ChatroomUser> updateReadState(long senderUserId, long chatroomId, int readState);

    ChatroomUser updateSelfReadState(long userId, long chatroomId, int readState);
}
