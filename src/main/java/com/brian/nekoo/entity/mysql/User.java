package com.brian.nekoo.entity.mysql;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "avatar_path")
    private String avatarPath = "";

    @Column(name = "web_token")
    private String webToken;

    @Column(name = "location")
    private String location;

    @Column(name = "gender")
    private String gender;

    @Column(name = "content")
    private String content;

    @Column(name = "birthday")
    private Instant birthday;

    @OneToMany(mappedBy = "user")
    private Set<ChatroomUser> chatroomUsers;

    @Column(name = "create_at")
    private Instant createAt;

    @Column(name = "modify_at")
    private Instant modifyAt;

    @Column(name = "remove_at")
    private Instant removeAt;
}
