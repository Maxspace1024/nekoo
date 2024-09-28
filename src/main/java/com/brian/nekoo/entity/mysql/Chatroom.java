package com.brian.nekoo.entity.mysql;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "chatrooms")
public class Chatroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "uuid")
    private String uuid;

    @OneToMany(mappedBy = "chatroom")
    private Set<ChatroomUser> chatroomUsers;

    @Field(name = "avatar_path")
    private String avatarPath = "";

    @Column(name = "create_at")
    private Instant createAt;

    @Column(name = "modify_at")
    private Instant modifyAt;

    @Column(name = "remove_at")
    private Instant removeAt;

    // 自定義的 toString
    @Override
    public String toString() {
        return "Chat{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", uuid='" + uuid + '\'' +
            ", avatarPath='" + avatarPath + '\'' +
            ", createAt=" + createAt +
            ", modifyAt=" + modifyAt +
            ", removeAt=" + removeAt +
            '}';
    }

    // 自定義的 equals 和 hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chatroom chatroom = (Chatroom) o;
        return Objects.equals(id, chatroom.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
