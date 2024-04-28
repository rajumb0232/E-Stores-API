package com.self.flipcart.service;

import com.self.flipcart.requestdto.ProductRequest;
import com.self.flipcart.responsedto.ProductResponse;
import com.self.flipcart.util.ResponseStructure;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    ResponseEntity<ResponseStructure<ProductResponse>> addProduct(ProductRequest productRequest, String storeId);
}
