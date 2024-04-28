package com.self.flipcart.repository;

import com.self.flipcart.model.Address;
import com.self.flipcart.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepo extends JpaRepository<Store, String> {
    Optional<Address> findAddressByStoreId(String storeId);

}
