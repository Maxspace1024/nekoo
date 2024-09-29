package com.brian.nekoo.service;

import com.brian.nekoo.entity.mysql.User;
import com.brian.nekoo.repository.mysql.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmailAndRemoveAtIsNull(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // 將資料庫中的 User 轉換成 UserDetails
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), List.of());
    }
}
