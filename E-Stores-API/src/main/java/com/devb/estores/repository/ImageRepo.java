package com.devb.estores.repository;

import com.devb.estores.model.Image;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImageRepo extends MongoRepository<Image, String> {
}
