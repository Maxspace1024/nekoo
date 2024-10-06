package com.brian.nekoo.dto;

import com.brian.nekoo.entity.mysql.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long userId;
    private String userName;
    private String email;
    private String avatarPath = "";

    private String location;
    private String gender;
    private String content;
    private Instant birthday;

    public static UserDTO getDTO(User user) {
        if (user == null) {
            return null;
        }
        return UserDTO.builder()
            .userId(user.getId())
            .userName(user.getName())
            .birthday(user.getBirthday())
            .gender(user.getGender())
            .location(user.getLocation())
            .content(user.getContent())
            .avatarPath(user.getAvatarPath())
            .email(user.getEmail())
            .build();
    }
}
