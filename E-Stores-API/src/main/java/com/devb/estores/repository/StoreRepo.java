package com.devb.estores.repository;

import com.devb.estores.model.Address;
import com.devb.estores.model.Store;
import com.devb.estores.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepo extends JpaRepository<Store, String> {
    Optional<Address> findAddressByStoreId(String storeId);

    boolean existsByUser(User user);

    Optional<Store> findByUser(User user);
}
