package com.self.flipcart.repository;

import com.self.flipcart.model.Variant;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VariantRepo extends MongoRepository<Variant, String> {
}
