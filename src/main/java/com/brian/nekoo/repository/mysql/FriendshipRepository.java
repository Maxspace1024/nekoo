package com.brian.nekoo.repository.mysql;

import com.brian.nekoo.entity.mysql.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    @Query("SELECT f FROM Friendship f WHERE (f.senderUser.id = :userId1 AND f.receiverUser.id = :userId2) OR (f.senderUser.id = :userId2 AND f.receiverUser.id = :userId1)")
    Optional<Friendship> findFriendship(@Param("userId1") long userId1, @Param("userId2") long userId2);

    @Query(
        "SELECT f FROM Friendship f WHERE (( f.senderUser.id = :userId AND f.receiverUser.name LIKE %:searchName% ) " +
            "OR ( f.receiverUser.id = :userId AND f.senderUser.name LIKE %:searchName% )) " +
            "AND f.state = :state "
    )
    List<Friendship> findMyFriendshipsWithNameAndState(@Param("userId") long currUserId, @Param("searchName") String searchName, @Param("state") int state);

    @Query(value = """
            SELECT
            ff.*
            FROM
            (SELECT * FROM users WHERE id <> :userId) uu JOIN
            (SELECT * FROM friendships f WHERE f.user_receiver_id = :userId OR f.user_sender_id = :userId) ff
            ON (uu.id = ff.user_receiver_id OR uu.id = ff.user_sender_id)
            WHERE uu.name LIKE CONCAT('%', :searchName, '%');
        """, nativeQuery = true)
    List<Friendship> findFriendshipsWithName(@Param("userId") long userId, @Param("searchName") String searchName);

    @Query(value = """
            SELECT
            ff.*
            FROM
            (SELECT * FROM users WHERE id <> :userId) uu JOIN
            (SELECT * FROM friendships f WHERE f.user_receiver_id = :userId OR f.user_sender_id = :userId) ff
            ON (uu.id = ff.user_receiver_id OR uu.id = ff.user_sender_id)
            WHERE ff.state IN (0,2);
        """, nativeQuery = true)
    List<Friendship> findFriendshipsNotification(@Param("userId") long userId);
}
