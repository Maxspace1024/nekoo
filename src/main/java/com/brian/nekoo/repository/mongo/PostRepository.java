package com.brian.nekoo.repository.mongo;

import com.brian.nekoo.entity.mongo.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    Page<Post> findAllByRemoveAtIsNullOrderByCreateAtDesc(Pageable pageable);

    Page<Post> findByRemoveAtIsNullAndPrivacyInOrderByCreateAtDesc(Pageable pageable, List<Integer> privacy);

    Page<Post> findByRemoveAtIsNullAndUserIdAndPrivacyInOrderByCreateAtDesc(Pageable pageable, long userId, List<Integer> privacy);

    // 匹配content內容、在查詢時間之前
    Page<Post> findByRemoveAtIsNullAndContentContainingAndCreateAtBeforeOrderByCreateAtDesc(Pageable pageable, String content, Instant createAt);

    // 匹配hashtag
    Page<Post> findByRemoveAtIsNullAndHashtagsInAndCreateAtBeforeOrderByCreateAtDesc(Pageable pageable, String hashtags, Instant createAt);

    List<Post> findPostByUserId(long userId);

    Page<Post> findAll(Pageable pageable);

    Page<Post> findByRemoveAtIsNull(Pageable pageable);
}
