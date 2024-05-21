package com.devb.estores.repository;

import com.devb.estores.model.Variant;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VariantRepo extends MongoRepository<Variant, String> {
}
