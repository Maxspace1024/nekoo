package com.brian.nekoo.repository.mysql;

import com.brian.nekoo.entity.mysql.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmailAndWebTokenAndRemoveAtIsNull(String email, String webToken);

    Optional<User> findUserByEmailAndRemoveAtIsNull(String email);

    Page<User> findUsersByNameContaining(String name, Pageable pageable);
}
