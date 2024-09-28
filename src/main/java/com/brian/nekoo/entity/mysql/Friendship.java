package com.brian.nekoo.entity.mysql;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "friendships")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 發出交友邀請者
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_sender_id", referencedColumnName = "id")
    private User senderUser;

    // 接收交友邀請者
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_receiver_id", referencedColumnName = "id")
    private User receiverUser;

    // 交友狀態
    @Column(name = "state")
    private int state;

    @Column(name = "create_at")
    private Instant createAt;

    @Column(name = "modify_at")
    private Instant modifyAt;
}
