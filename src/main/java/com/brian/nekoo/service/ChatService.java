package com.brian.nekoo.service;

import com.brian.nekoo.dto.ChatLogDTO;
import com.brian.nekoo.dto.ChatroomDTO;
import com.brian.nekoo.dto.req.ChatLogReqDTO;
import com.brian.nekoo.dto.req.ChatroomReqDTO;

import java.util.List;

public interface ChatService {
    ChatroomDTO createChatroom(ChatroomReqDTO dto);

    ChatLogDTO createChatLog(ChatLogReqDTO dto);

    ChatLogDTO deleteChatLog(ChatLogReqDTO dto);

    ChatLogDTO updateChatLog(ChatLogReqDTO dto);

    List<ChatroomDTO> findChatroomsByUserId(long userId);
}
