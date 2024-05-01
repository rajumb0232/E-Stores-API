package com.self.flipcart.repository;

import com.self.flipcart.model.Address;
import com.self.flipcart.model.Store;
import com.self.flipcart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepo extends JpaRepository<Store, String> {
    Optional<Address> findAddressByStoreId(String storeId);

    boolean existsByUser(User user);

    Optional<Store> findByUser(User user);
}
