package com.self.flipcart.repository;

import com.self.flipcart.model.Product;
import com.self.flipcart.model.Specification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SpecificationRepo extends MongoRepository<Specification, String> {
    boolean existsByNameAndProduct(String name, Product product);

    Optional<Specification> findByNameAndProduct(String name, Product product);
}
