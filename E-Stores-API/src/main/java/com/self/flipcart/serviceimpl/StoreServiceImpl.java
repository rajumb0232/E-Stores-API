package com.self.flipcart.serviceimpl;

import com.self.flipcart.exceptions.InvalidPrimeCategoryException;
import com.self.flipcart.exceptions.StoreNotFoundByIdException;
import com.self.flipcart.mapper.StoreMapper;
import com.self.flipcart.model.Store;
import com.self.flipcart.repository.SellerRepo;
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
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StoreServiceImpl implements StoreService {

    private UserRepo userRepo;
    private SellerRepo sellerRepo;
    private StoreRepo storeRepo;

    @Override
    public ResponseEntity<ResponseStructure<Store>> setUpStore(StoreRequest storeRequest) {
        if (storeRequest.getCategory() == null)
            throw new InvalidPrimeCategoryException("Failed to update the store data");

        Store store = StoreMapper.mapToStoreEntity(storeRequest, new Store());

        return userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .map(user -> sellerRepo.findById(user.getUserId())
                        .map(seller -> {
                            Store uniqueStore = storeRepo.save(store);
                            seller.setStore(uniqueStore);
                            sellerRepo.save(seller);
                            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseStructure<Store>()
                                    .setStatus(HttpStatus.CREATED.value())
                                    .setMessage("Store Created Successfully")
                                    .setData(uniqueStore));
                        }).get()).orElseThrow();
    }

    @Override
    public ResponseEntity<ResponseStructure<Store>> updateStore(StoreRequest storeRequest, String storeId) {
        return storeRepo.findById(storeId).map(exStore -> {

            Store store = StoreMapper.mapToStoreEntity(storeRequest, exStore);
            store.setTopCategory(exStore.getTopCategory());
            Store uniqueStore = storeRepo.save(store);

            return ResponseEntity.ok(new ResponseStructure<Store>()
                    .setStatus(HttpStatus.OK.value())
                    .setMessage("Store Created Successfully")
                    .setData(uniqueStore));
        }).orElseThrow();
    }

    @Override
    public ResponseEntity<ResponseStructure<StoreResponse>> getStore(String storeId) {
        return storeRepo.findById(storeId)
                .map(store -> ResponseEntity.status(HttpStatus.FOUND).body(new ResponseStructure<StoreResponse>()
                        .setStatus(HttpStatus.FOUND.value())
                        .setMessage("Store data found")
                        .setData(StoreMapper.mapToStorePageResponse(store))))
                .orElseThrow(() -> new StoreNotFoundByIdException("Failed to find the store data"));
    }

    @Override
    public ResponseEntity<Boolean> checkIfStoreExistBySeller() {
        return userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .map(user -> sellerRepo.findById(user.getUserId())
                        .map(seller -> {
                            if (seller.getStore() != null) return ResponseEntity.ok(true);
                            else return ResponseEntity.ok(false);
                        })
                        .get())
                .orElseThrow();
    }

    @Override
    public ResponseEntity<ResponseStructure<StoreResponse>> getStoreBySeller() {
        return userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .map(user -> sellerRepo.findById(user.getUserId())
                        .map(seller -> {
                            if (seller.getStore() != null) {
                                return ResponseEntity.status(HttpStatus.FOUND).body(new ResponseStructure<StoreResponse>()
                                        .setStatus(HttpStatus.FOUND.value())
                                        .setMessage("Store found")
                                        .setData(StoreMapper.mapToStorePageResponse(seller.getStore())));
                            } else throw new RuntimeException("No Store found associated with seller");
                        }).get())
                .orElseThrow();
    }

}
