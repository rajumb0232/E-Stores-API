package com.devb.estores.controller;

import com.devb.estores.service.StoreService;
import com.devb.estores.util.AppResponseBuilder;
import com.devb.estores.util.ResponseStructure;
import com.devb.estores.requestdto.StoreRequest;
import com.devb.estores.responsedto.StoreResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.base-url}")
@AllArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final AppResponseBuilder responseBuilder;

    @PostMapping("/stores")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<ResponseStructure<StoreResponse>> setUpStore(@RequestBody StoreRequest storeRequest){
        StoreResponse response = storeService.setUpStore(storeRequest);
        return responseBuilder.success(HttpStatus.CREATED, "Store created", response);
    }

    @PutMapping("/stores/{storeId}")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<ResponseStructure<StoreResponse>> updateStore(@RequestBody StoreRequest storeRequest, @PathVariable String storeId){
        StoreResponse response = storeService.updateStore(storeRequest, storeId);
        return responseBuilder.success(HttpStatus.OK, "Store updated", response);
    }

    @GetMapping("/stores/{storeId}")
    public ResponseEntity<ResponseStructure<StoreResponse>> getStore(@PathVariable String storeId){
        StoreResponse response = storeService.getStore(storeId);
        return responseBuilder.success(HttpStatus.FOUND, "Store found", response);
    }

    @GetMapping("/stores-exist")
    public ResponseEntity<Boolean> checkIfStoreExistBySeller(){
        boolean result = storeService.checkIfStoreExistBySeller();
        return responseBuilder.success(HttpStatus.CREATED, result);
    }

    @GetMapping("/stores")
    public ResponseEntity<ResponseStructure<StoreResponse>> getStoreBySeller(){
        StoreResponse response = storeService.getStoreBySeller();
        return responseBuilder.success(HttpStatus.FOUND, "Store found by seller", response);
    }
}
