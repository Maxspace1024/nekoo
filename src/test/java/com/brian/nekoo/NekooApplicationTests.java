package com.brian.nekoo;

import com.brian.nekoo.entity.mysql.User;
import com.brian.nekoo.repository.mysql.UserRepository;
import com.brian.nekoo.service.S3Service;
import com.brian.nekoo.service.impl.UserServiceImpl;
import com.brian.nekoo.util.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@Log4j2
class NekooApplicationTests {

//    @Autowired
//    private ChatLogRepository chatLogRepository;
//
//    @Autowired
//    private ChatroomRepository chatRoomRepository;
//
//    @Autowired
//    private PostRepository postRepository;
//
//    @Autowired
//    private DanmakuRepository danmakuRepository;
//
//    @Autowired
//    private JwtUtil jwtUtil;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private S3Service s3Service;

    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(jwtUtil, userRepository, passwordEncoder, s3Service);
    }

    @Test
    void test() {
        User stubUser = new User();
        stubUser.setId(1L);
        stubUser.setName("hi");
        stubUser.setEmail("pypy@gmail.com");
        when(userRepository.findUserByEmailAndRemoveAtIsNull("pypy@gmail.com")).thenReturn(Optional.of(stubUser));

        userService.findUserByEmail("ypy@gmail.com");
        verify(userRepository, times(1)).findUserByEmailAndRemoveAtIsNull("ypy@gmail.com");

    }

    @Test
    void contextLoads() {
        User stubUser = new User();
        stubUser.setId(1L);
        stubUser.setName("hi");
        when(userRepository.findById(1L)).thenReturn(Optional.of(stubUser));

        userService.findUserById(1L);

        // 驗證 UserRepository 的 findById(1L) 是否被正確調用一次
        verify(userRepository, times(1)).findById(1L);

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
