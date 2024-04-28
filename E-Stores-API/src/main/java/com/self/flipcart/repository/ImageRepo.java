package com.self.flipcart.repository;

import com.self.flipcart.model.Image;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImageRepo extends MongoRepository<Image, String> {
}
