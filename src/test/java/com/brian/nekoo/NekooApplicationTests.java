package com.brian.nekoo;

import com.brian.nekoo.repository.mongo.ChatLogRepository;
import com.brian.nekoo.repository.mongo.DanmakuRepository;
import com.brian.nekoo.repository.mongo.PostRepository;
import com.brian.nekoo.repository.mysql.ChatroomRepository;
import com.brian.nekoo.util.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
class NekooApplicationTests {

    @Autowired
    private ChatLogRepository chatLogRepository;

    @Autowired
    private ChatroomRepository chatRoomRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private DanmakuRepository danmakuRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void test() {
//        log.info(jwtUtil.generateToken("asdfasdf", new HashMap<>()));
        log.info(jwtUtil.extractUsername("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhc2RmYXNkZiIsImlhdCI6MTcyNjgxNTgyMywiZXhwIjoxNzI2ODE5NDIzfQ.m3g0mBbkjZfsRXbih8cZWVcMLcUA5WBmysetry4IVXc"));
//        SignatureException e
    }

    @Test
    void contextLoads() {
//        Pageable pageable = PageRequest.of(0, 5, Sort.by("modify_at").descending());
//        Page<ChatLog> page = chatLogRepository.findAllByChatId(333, pageable);
//        List<ChatLog> chatLogs = page.getContent();
//        for (ChatLog chatLog : chatLogs) {
//            log.info(chatLog);
//        }

//        Pageable pageable = PageRequest.of(0, 5, Sort.by("create_at").descending());
//        Page<Post> page = postRepository.findByRemoveAtIsNull(pageable);
//        List<Post> posts = page.getContent();
//        for (Post post : posts) {
//            log.info(post);
//        }

//        Pageable pageable = PageRequest.of(0, 5, Sort.by("modify_at").descending());
//        Page<Danmaku> page = danmakuRepository.findByImageId("asdfasdf", pageable);
//        List<Danmaku> danmakus = page.getContent();
//        for (Danmaku danmaku : danmakus) {
//            log.info(danmaku);
//        }

//        Pageable pageable = PageRequest.of(0, 5, Sort.by("modify_at").descending());
//        Page<DanmakuReply> page = danmakuReplyRepository.findByDanmakuId("asdfasdf", pageable);
//        List<DanmakuReply> danmakuReplies = page.getContent();
//        for (DanmakuReply danmakuReply : danmakuReplies) {
//            log.info(danmakuReply);
//        }
    }
}
