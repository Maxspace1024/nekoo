package com.brian.nekoo.controller;

import com.brian.nekoo.dto.MessageWrapper;
import com.brian.nekoo.dto.req.SigninReqDTO;
import com.brian.nekoo.dto.req.SignupReqDTO;
import com.brian.nekoo.entity.mysql.User;
import com.brian.nekoo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/signin")
    public ResponseEntity<?> signin(@RequestBody SigninReqDTO dto) {
        MessageWrapper<Object> mw = userService.signin(dto);
        return ResponseEntity.ok(mw);
    }

    @PostMapping("/user/signup")
    public ResponseEntity<?> signup(@RequestBody SignupReqDTO dto) {
        MessageWrapper<Object> mw = userService.signup(dto);
        return ResponseEntity.ok(mw);
    }

    @PostMapping("/user/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        MessageWrapper<Object> mw = userService.checkLoginValidation(authToken);
        if (mw.isSuccess()) {
            User user = (User) mw.getData();
            userService.logout(user);
            mw.setData(null);
            return ResponseEntity.ok(mw);
        } else {
            return new ResponseEntity<>(mw, HttpStatus.UNAUTHORIZED);
        }
    }
}
