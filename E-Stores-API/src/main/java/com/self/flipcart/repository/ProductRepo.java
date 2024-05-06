package com.self.flipcart.repository;

import com.self.flipcart.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepo extends MongoRepository<Product, String>{
}
