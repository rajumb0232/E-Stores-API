package com.self.flipcart.service;

import com.self.flipcart.requestdto.ProductRequest;
import com.self.flipcart.responsedto.ProductResponse;
import com.self.flipcart.util.ResponseStructure;
import com.self.flipcart.util.SimpleResponseStructure;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<ResponseStructure<ProductResponse>> addProduct(ProductRequest productRequest, String storeId);

    ResponseEntity<SimpleResponseStructure> updateVariantBy(String productId, List<String> specNames);

    ResponseEntity<ResponseStructure<ProductResponse>> getProductById(String productId);

    ResponseEntity<ResponseStructure<List<ProductResponse>>> getProducts(String text);
}
