package com.brian.nekoo.repository.mongo;

import com.brian.nekoo.entity.mongo.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findAllByOrderByCreateAtDesc();

    List<Post> findPostByUserId(long userId);

    Page<Post> findAll(Pageable pageable);

    Page<Post> findByRemoveAtIsNull(Pageable pageable);
}
