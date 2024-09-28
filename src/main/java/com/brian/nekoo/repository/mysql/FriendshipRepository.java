package com.brian.nekoo.repository.mysql;

import com.brian.nekoo.entity.mysql.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    @Query("SELECT f FROM Friendship f WHERE (f.senderUser.id = :userId1 AND f.receiverUser.id = :userId2) OR (f.senderUser.id = :userId2 AND f.receiverUser.id = :userId1)")
    Optional<Friendship> findFriendship(@Param("userId1") long userId1, @Param("userId2") long userId2);
}
