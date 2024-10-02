package com.brian.nekoo.service;

import com.brian.nekoo.dto.ChatLogDTO;
import com.brian.nekoo.dto.ChatroomDTO;
import com.brian.nekoo.dto.PageWrapper;
import com.brian.nekoo.dto.req.ChatLogReqDTO;
import com.brian.nekoo.dto.req.ChatroomReqDTO;
import com.brian.nekoo.entity.mysql.Chatroom;

import java.util.List;

public interface ChatService {
    ChatroomDTO createChatroom(ChatroomReqDTO dto);

    ChatLogDTO createChatLog(ChatLogReqDTO dto);

    ChatLogDTO deleteChatLog(ChatLogReqDTO dto);

    ChatLogDTO updateChatLog(ChatLogReqDTO dto);

    PageWrapper<ChatroomDTO> findChatroomsByUserId(long userId, ChatroomReqDTO dto);

    List<ChatroomDTO> findUnreadChatroomsByUserId(long userId);

    ChatroomDTO findChatroomByUserIdAndChatroomId(long userId, long chatroomId);

    Chatroom findChatroomByChatroomId(long chatroomId);
}
