package com.outing.friendship.repository;

import com.outing.friendship.model.DummyUserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DummyUserRepository extends MongoRepository<DummyUserModel, String> {
}
