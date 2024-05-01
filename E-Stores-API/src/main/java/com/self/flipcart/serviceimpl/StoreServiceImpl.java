package com.self.flipcart.serviceimpl;

import com.self.flipcart.exceptions.InvalidPrimeCategoryException;
import com.self.flipcart.exceptions.StoreNotFoundException;
import com.self.flipcart.mapper.StoreMapper;
import com.self.flipcart.model.Store;
import com.self.flipcart.repository.StoreRepo;
import com.self.flipcart.repository.UserRepo;
import com.self.flipcart.requestdto.StoreRequest;
import com.self.flipcart.responsedto.StoreResponse;
import com.self.flipcart.service.StoreService;
import com.self.flipcart.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final UserRepo userRepo;
    private final StoreRepo storeRepo;

    @Override
    public ResponseEntity<ResponseStructure<StoreResponse>> setUpStore(StoreRequest storeRequest) {
        if (storeRequest.getCategory() == null)
            throw new InvalidPrimeCategoryException("Failed to update the store data");

        return userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .map(user -> {
                    Store store = StoreMapper.mapToStoreEntity(storeRequest, new Store());
                    store.setUser(user);
                    store = storeRepo.save(store);
                    return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseStructure<StoreResponse>()
                            .setStatus(HttpStatus.CREATED.value())
                            .setMessage("Store Created Successfully")
                            .setData(StoreMapper.mapToStorePageResponse(store)));
                }).orElseThrow();
    }

    @Override
    public ResponseEntity<ResponseStructure<StoreResponse>> updateStore(StoreRequest storeRequest, String storeId) {
        return storeRepo.findById(storeId).map(exStore -> {

            Store store = StoreMapper.mapToStoreEntity(storeRequest, exStore);
            store.setTopCategory(exStore.getTopCategory());
            Store uniqueStore = storeRepo.save(store);

            return ResponseEntity.ok(new ResponseStructure<StoreResponse>()
                    .setStatus(HttpStatus.OK.value())
                    .setMessage("Store Created Successfully")
                    .setData(StoreMapper.mapToStorePageResponse(store)));
        }).orElseThrow();
    }

    @Override
    public ResponseEntity<ResponseStructure<StoreResponse>> getStore(String storeId) {
        return storeRepo.findById(storeId)
                .map(store -> ResponseEntity.status(HttpStatus.FOUND).body(new ResponseStructure<StoreResponse>()
                        .setStatus(HttpStatus.FOUND.value())
                        .setMessage("Store data found")
                        .setData(StoreMapper.mapToStorePageResponse(store))))
                .orElseThrow(() -> new StoreNotFoundException("Failed to find the store data"));
    }

    @Override
    public ResponseEntity<Boolean> checkIfStoreExistBySeller() {
        return userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .map(user -> ResponseEntity.ok(storeRepo.existsByUser(user)))
                .orElseThrow();
    }

    @Override
    public ResponseEntity<ResponseStructure<StoreResponse>> getStoreBySeller() {
        return userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .map(user -> storeRepo.findByUser(user).map(store -> ResponseEntity
                        .status(HttpStatus.FOUND)
                        .body(new ResponseStructure<StoreResponse>()
                                .setStatus(HttpStatus.FOUND.value())
                                .setMessage("Store found")
                                .setData(StoreMapper.mapToStorePageResponse(store))
                        )).orElseThrow(() -> new StoreNotFoundException("failed to find store"))
                ).orElseThrow(() -> new UsernameNotFoundException("failed to find store"));
    }

}
