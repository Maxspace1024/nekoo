package com.brian.nekoo.repository.mongo;

import com.brian.nekoo.entity.mongo.Danmaku;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DanmakuRepository extends MongoRepository<Danmaku, String> {
    Page<Danmaku> findByAssetId(String assetId, Pageable pageable);

    long countByAssetId(String assetId);
}
