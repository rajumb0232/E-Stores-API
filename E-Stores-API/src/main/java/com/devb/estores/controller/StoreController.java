package com.devb.estores.controller;

import com.devb.estores.service.StoreService;
import com.devb.estores.util.ResponseStructure;
import com.devb.estores.requestdto.StoreRequest;
import com.devb.estores.responsedto.StoreResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.base_url}")
@AllArgsConstructor
public class StoreController {

    private StoreService storeService;

    @PostMapping("/stores")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<ResponseStructure<StoreResponse>> setUpStore(@RequestBody StoreRequest storeRequest){
        return storeService.setUpStore(storeRequest);
    }

    @PutMapping("/stores/{storeId}")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<ResponseStructure<StoreResponse>> updateStore(@RequestBody StoreRequest storeRequest, @PathVariable String storeId){
        return storeService.updateStore(storeRequest, storeId);
    }

    @GetMapping("/stores/{storeId}")
    public ResponseEntity<ResponseStructure<StoreResponse>> getStore(@PathVariable String storeId){
        return storeService.getStore(storeId);
    }

    @GetMapping("/stores-exist")
    public ResponseEntity<Boolean> checkIfStoreExistBySeller(){
        return storeService.checkIfStoreExistBySeller();
    }

    @GetMapping("/stores")
    public ResponseEntity<ResponseStructure<StoreResponse>> getStoreBySeller(){
        return storeService.getStoreBySeller();
    }
}
