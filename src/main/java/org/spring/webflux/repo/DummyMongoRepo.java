package org.spring.webflux.repo;

import org.spring.webflux.entity.DummyMongo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DummyMongoRepo extends ReactiveMongoRepository<DummyMongo, Integer>{

}
