package com.brian.nekoo.repository.mongo;

import com.brian.nekoo.entity.mongo.Danmaku;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DanmakuRepository extends MongoRepository<Danmaku, String> {
    Page<Danmaku> findByAssetIdOrderByCreateAtDesc(String assetId, Pageable pageable);

    @Query("{ 'assetId': ?0 }")
    Page<Danmaku> findByAssetIdWithQuery(String assetId, Pageable pageable);

    List<Danmaku> findByAssetId(String assetId);

    long countByAssetId(String assetId);
}
