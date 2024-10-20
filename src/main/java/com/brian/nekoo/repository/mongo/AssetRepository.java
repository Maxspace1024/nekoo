package com.brian.nekoo.repository.mongo;

import com.brian.nekoo.entity.mongo.Asset;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends MongoRepository<Asset, String> {
}
