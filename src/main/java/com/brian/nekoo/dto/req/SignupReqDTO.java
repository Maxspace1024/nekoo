package com.brian.nekoo.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupReqDTO {
    private String name;
    private String email;
    private String password;
    private String avatarPath;
}
