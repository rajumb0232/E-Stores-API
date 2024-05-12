package com.self.flipcart.repository;

import com.self.flipcart.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepo extends MongoRepository<Product, String>{
    List<Product> findByTitleContainingOrDescriptionContaining(String text, String text1);

    List<Product> findByTitleLikeOrDescriptionLike(String text, String text1);

    List<Product> findByTitleInOrDescriptionIn(List<String> keywords, List<String> keywords1);

    List<Product> findByTitleIgnoreCaseInOrDescriptionIgnoreCaseIn(String keywords, String keywords1);

    List<Product> findByTitleIgnoreCaseLikeOrDescriptionIgnoreCaseLike(String s, String s1);

    List<Product> findByTitleOrDescription(String s, String s1);
}
