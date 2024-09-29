package com.brian.nekoo.repository.mysql;

import com.brian.nekoo.entity.mysql.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmailAndWebTokenAndRemoveAtIsNull(String email, String webToken);

    Optional<User> findUserByEmailAndRemoveAtIsNull(String email);

    Page<User> findUsersByNameContaining(String name, Pageable pageable);

    @Query(value = """
            SELECT
            uu.*
            FROM
            (SELECT * FROM users WHERE id <> :userId) uu LEFT JOIN
            (SELECT * FROM friendships f WHERE f.user_receiver_id = :userId OR f.user_sender_id = :userId) ff
            ON (uu.id = ff.user_receiver_id OR uu.id = ff.user_sender_id)
            WHERE uu.name LIKE CONCAT('%', :searchName, '%') AND ff.state IS NULL;
        """, nativeQuery = true)
    List<User> findNoFriendshipsWithName(long userId, String searchName);
}
