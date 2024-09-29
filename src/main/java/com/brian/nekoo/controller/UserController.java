package com.brian.nekoo.controller;

import com.brian.nekoo.dto.MessageWrapper;
import com.brian.nekoo.dto.req.SigninReqDTO;
import com.brian.nekoo.dto.req.SignupReqDTO;
import com.brian.nekoo.entity.mysql.User;
import com.brian.nekoo.service.UserDetailService;
import com.brian.nekoo.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserService userService;
    private final UserDetailService userDetailService;

    @PostMapping("/user/signin")
    public ResponseEntity<?> signin(HttpServletResponse response, @RequestBody SigninReqDTO dto) {
        MessageWrapper<Object> mw = userService.signin(dto);
        if (mw.isSuccess()) {
            Map<String, Object> map = (HashMap<String, Object>) mw.getData();
            Cookie cookie = new Cookie("nekooToken", (String) map.get("jwt"));
            response.addCookie(cookie);
        }
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

    @GetMapping("/user/temp/{name}")
    public ResponseEntity<?> test(HttpServletRequest request, @PathVariable String name) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(name);
    }
}
