package com.brian.nekoo.repository.mongo;

import com.brian.nekoo.entity.mongo.ChatLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatLogRepository extends MongoRepository<ChatLog, String> {
    Page<ChatLog> findAllByChatroomId(long chatroomId, Pageable pageable);

    Page<ChatLog> findAllByChatroomIdAndRemoveAtIsNull(long chatroomId, Pageable pageable);

    ChatLog findFirstByChatroomIdOrderByCreateAtDesc(long chatroomId);
}
