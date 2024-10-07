package com.brian.nekoo.repository.mysql;

import com.brian.nekoo.entity.mysql.ChatroomUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatroomUserRepository extends JpaRepository<ChatroomUser, Long> {
    Page<ChatroomUser> findByUserId(long userId, Pageable pageable);

    List<ChatroomUser> findByUserIdAndReadState(long userId, int readState);

    Optional<ChatroomUser> findByUserIdAndChatroomId(long userId, long chatroomId);

    List<ChatroomUser> findByUserIdNotAndChatroomId(long userId, long chatroomId);
}
