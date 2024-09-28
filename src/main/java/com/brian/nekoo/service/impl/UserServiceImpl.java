package com.brian.nekoo.service.impl;

import com.brian.nekoo.dto.MessageWrapper;
import com.brian.nekoo.dto.req.SigninReqDTO;
import com.brian.nekoo.dto.req.SignupReqDTO;
import com.brian.nekoo.entity.mysql.User;
import com.brian.nekoo.repository.mysql.UserRepository;
import com.brian.nekoo.service.UserService;
import com.brian.nekoo.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(JwtUtil jwtUtil, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public MessageWrapper<Object> signin(SigninReqDTO dto) {
        MessageWrapper.MessageWrapperBuilder<Object> builder = MessageWrapper.builder();
        Optional<User> oUser = userRepository.findUserByEmailAndRemoveAtIsNull(dto.getEmail());
        if (oUser.isPresent() && passwordEncoder.matches(dto.getPassword(), oUser.get().getPassword())) {
            User user = oUser.get();
            String token = jwtUtil.generateToken(user.getEmail(), new HashMap<>());
            Map<String, Object> map = new HashMap<>();
            map.put("jwt", token);
            map.put("userId", user.getId());
            map.put("email", user.getEmail());
            builder.isSuccess(true)
                .data(map);
            user.setWebToken(token);
            userRepository.save(user);
        } else {
            builder.isSuccess(false)
                .error("帳號或密碼錯誤");
        }
        log.info(builder.build());
        return builder.build();
    }

    @Override
    public MessageWrapper<Object> signup(SignupReqDTO dto) {
        MessageWrapper.MessageWrapperBuilder<Object> builder = MessageWrapper.builder();
        Optional<User> oUser = userRepository.findUserByEmailAndRemoveAtIsNull(dto.getEmail());
        if (oUser.isEmpty()) {
            Instant now = Instant.now();
            userRepository.save(
                User.builder()
                    .name(dto.getName())
                    .email(dto.getEmail())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .avatarPath(dto.getAvatarPath())
                    .webToken("")
                    .createAt(now)
                    .modifyAt(now)
                    .build()
            );
            builder.isSuccess(true);
        } else {
            builder.isSuccess(false)
                .error("信箱重複");
        }
        return builder.build();
    }

    @Override
    public void logout(User user) {
        user.setWebToken("");
        userRepository.save(user);
    }

    @Override
    public MessageWrapper<Object> checkLoginValidation(String authToken) {
        MessageWrapper.MessageWrapperBuilder<Object> builder = MessageWrapper.builder();
        if (authToken == null || !authToken.startsWith("Bearer ")) {
            builder.isSuccess(false)
                .error("header authorization 錯誤");
            return builder.build();
        }
        try {
            String jwtToken = authToken.replace("Bearer ", "");
            String email = jwtUtil.extractUsername(jwtToken);
            Optional<User> oUser = userRepository.findUserByEmailAndWebTokenAndRemoveAtIsNull(email, jwtToken);
            if (oUser.isPresent()) {
                builder.isSuccess(true)
                    .data(oUser.get());
            } else {
                builder.isSuccess(false)
                    .error("登入授權不合法");
            }
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            builder.isSuccess(false)
                .error("jwt 錯誤");
            e.printStackTrace();
        }
        return builder.build();
    }

    @Override
    public User checkLoginValid(HttpServletRequest request) {
        try {
            // 從請求中取得 JWT Token
            String bearerToken = request.getHeader("Authorization");
            String token = jwtUtil.resolveToken(bearerToken);
            // 透過 JWT 解析出 userId
            String username = jwtUtil.extractUsername(token);

            if (token != null && jwtUtil.validateToken(token, username)) {
                return findUserByEmail(username);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User checkLoginValid(SimpMessageHeaderAccessor accessor) {
        try {
            // 從請求中取得 JWT Token
            String bearerToken = accessor.getFirstNativeHeader("Authorization");
            String token = jwtUtil.resolveToken(bearerToken);
            // 透過 JWT 解析出 userId
            String username = jwtUtil.extractUsername(token);

            if (token != null && jwtUtil.validateToken(token, username)) {
                return findUserByEmail(username);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmailAndRemoveAtIsNull(email).orElse(null);
    }
}
