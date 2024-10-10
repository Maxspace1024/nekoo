package com.brian.nekoo.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendshipReqDTO {
    private Long friendshipId;
    private Long senderUserId;
    private Long receiverUserId;
    private String searchName;
    private Integer page;
    private Integer state;
    private Instant queryAt;
}
