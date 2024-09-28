package com.brian.nekoo.controller.test;

import com.brian.nekoo.dto.req.UserReqDTO;
import com.brian.nekoo.entity.mysql.User;
import com.brian.nekoo.repository.mysql.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/test/v1")
@Log4j2
public class UserTestController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/user/findUsersByNameContaining")
    private ResponseEntity<Object> findUsersByNameContaining(@RequestBody UserReqDTO dto) {
        Integer pageNumber = dto.getPageNumber();
        Pageable pageable = pageNumber == null ? null : PageRequest.of(pageNumber, 3);
        Page<User> page = userRepository.findUsersByNameContaining(dto.getSearchUsername(), pageable);
        List<User> users = page.getContent();
        return ResponseEntity.ok(users);
    }
}
