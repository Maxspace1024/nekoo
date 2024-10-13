package com.brian.nekoo.controller;

import com.brian.nekoo.dto.MessageWrapper;
import com.brian.nekoo.dto.UserDTO;
import com.brian.nekoo.dto.req.SigninReqDTO;
import com.brian.nekoo.dto.req.SignupReqDTO;
import com.brian.nekoo.dto.req.UserProfileReqDTO;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserService userService;
    private final UserDetailService userDetailService;

    @PostMapping("/user/auth")
    public ResponseEntity<?> auth(HttpServletRequest request) {
        User user = userService.checkLoginValid(request);
        Map<String, Object> map = new HashMap<>();
        if (user != null) {
            map.put("userId", user.getId());
            map.put("userName", user.getName());
            map.put("userAvatarPath", user.getAvatarPath());
            map.put("email", user.getEmail());
        }
        return MessageWrapper.toResponseEntityOk(map);
    }

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

//    @PostMapping(value = "/user/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?> postProfile(HttpServletRequest request, @ModelAttribute UserProfileReqDTO dto) {
//        User user = userService.checkLoginValid(request);
//        UserDTO userDTO = null;
//        if (user != null) {
//            dto.setUserId(user.getId());
//            userDTO = userService.updateUserProfile(dto);
//        }
//        return MessageWrapper.toResponseEntityOk(userDTO);
//    }

    @PostMapping(value = "/user/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<Object>> postProfile(HttpServletRequest request, @ModelAttribute UserProfileReqDTO dto) {
        return Mono.fromCallable(() -> userService.checkLoginValid(request))
            .flatMap(user -> {
                if (user != null) {
                    dto.setUserId(user.getId());
                    return Mono.fromCallable(() -> userService.updateUserProfile(dto))
                        .map(MessageWrapper::toResponseEntityOk);
                } else {
                    return Mono.just(MessageWrapper.toResponseEntityOk(null));
                }
            });
    }

    @GetMapping(value = "/user/profile/{userId}")
    public ResponseEntity<?> getProfile(HttpServletRequest request, @PathVariable long userId) {
        User user = userService.checkLoginValid(request);
        User queryUser = userService.findUserById(userId);
        UserDTO userDTO = null;
        if (user != null && queryUser != null) {
            userDTO = UserDTO.getDTO(queryUser);
        }
        return MessageWrapper.toResponseEntityOk(userDTO);
    }
}
