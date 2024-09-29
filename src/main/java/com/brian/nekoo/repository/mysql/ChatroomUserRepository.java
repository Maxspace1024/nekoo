package com.brian.nekoo.repository.mysql;

import com.brian.nekoo.entity.mysql.ChatroomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatroomUserRepository extends JpaRepository<ChatroomUser, Long> {
    List<ChatroomUser> findByUserId(long userId);

    List<ChatroomUser> findByUserIdAndReadState(long userId, int readState);
}
