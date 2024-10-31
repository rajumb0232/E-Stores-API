package com.devb.estores.serviceimpl;

import com.devb.estores.exceptions.InvalidPrimeCategoryException;
import com.devb.estores.exceptions.StoreNotFoundException;
import com.devb.estores.mapper.StoreMapper;
import com.devb.estores.model.Store;
import com.devb.estores.repository.StoreRepo;
import com.devb.estores.repository.UserRepo;
import com.devb.estores.requestdto.StoreRequest;
import com.devb.estores.responsedto.StoreResponse;
import com.devb.estores.service.StoreService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final UserRepo userRepo;
    private final StoreRepo storeRepo;
    private final StoreMapper storeMapper;

    @Override
    public StoreResponse setUpStore(StoreRequest storeRequest) {

        log.info("create store requested, validating storeRequest");
        if (storeRequest.getCategory() == null)
            throw new InvalidPrimeCategoryException("Failed to update the store data");

        log.info("prime Category is valid.");

        return userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .map(user -> {
                    log.info("User found.");
                    Store store = storeMapper.mapToStoreEntity(storeRequest, new Store());
                    store.setUser(user);
                    store = storeRepo.save(store);
                    return storeMapper.mapToStorePageResponse(store);
                }).orElseThrow();
    }

    @Override
    public StoreResponse updateStore(StoreRequest storeRequest, String storeId) {
        return storeRepo.findById(storeId).map(exStore -> {

            Store store = storeMapper.mapToStoreEntity(storeRequest, exStore);
            store.setTopCategory(exStore.getTopCategory());
            store = storeRepo.save(store);

            return storeMapper.mapToStorePageResponse(store);
        }).orElseThrow();
    }

    @Override
    public StoreResponse getStore(String storeId) {
        return storeRepo.findById(storeId)
                .map(storeMapper::mapToStorePageResponse)
                .orElseThrow(() -> new StoreNotFoundException("Failed to find the store data"));
    }

    @Override
    public Boolean checkIfStoreExistBySeller() {
        return userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .map(storeRepo::existsByUser)
                .orElseThrow();
    }

    @Override
    public StoreResponse getStoreBySeller() {
        return userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .map(user -> storeRepo.findByUser(user).map(storeMapper::mapToStorePageResponse)
                        .orElseThrow(() -> new StoreNotFoundException("failed to find store"))
                ).orElseThrow(() -> new UsernameNotFoundException("failed to find store"));
    }

}
