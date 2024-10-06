package com.brian.nekoo.service;

import com.brian.nekoo.dto.MessageWrapper;
import com.brian.nekoo.dto.UserDTO;
import com.brian.nekoo.dto.req.SigninReqDTO;
import com.brian.nekoo.dto.req.SignupReqDTO;
import com.brian.nekoo.dto.req.UserProfileReqDTO;
import com.brian.nekoo.entity.mysql.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public interface UserService {
    MessageWrapper<Object> signin(SigninReqDTO dto);

    MessageWrapper<Object> signup(SignupReqDTO dto);

    void logout(User user);

    MessageWrapper<Object> checkLoginValidation(String authToken);

    User checkLoginValid(HttpServletRequest request);

    User checkLoginValid(SimpMessageHeaderAccessor accessor);

    User findUserByEmail(String email);

    User findUserById(Long userId);

    UserDTO updateUserProfile(UserProfileReqDTO dto);
}
