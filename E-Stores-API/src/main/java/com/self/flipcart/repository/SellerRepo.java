package com.self.flipcart.repository;

import com.self.flipcart.model.Seller;
import com.self.flipcart.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepo extends JpaRepository<Seller, String> {
    boolean existsByEmail(String email);

    Optional<Seller> findByUsername(String split);

    Optional<Store> findStoreByUserId(String userId);

}
