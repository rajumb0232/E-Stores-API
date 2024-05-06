package com.self.flipcart.repository;

import com.self.flipcart.model.Product;
import com.self.flipcart.model.Variant;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VariantRepo extends MongoRepository<Variant, String> {
}
