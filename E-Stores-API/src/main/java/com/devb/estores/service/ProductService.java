package com.devb.estores.service;

import com.devb.estores.util.ResponseStructure;
import com.devb.estores.util.SimpleResponseStructure;
import com.devb.estores.requestdto.ProductRequest;
import com.devb.estores.responsedto.ProductResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<ResponseStructure<ProductResponse>> addProduct(ProductRequest productRequest, String storeId);

    ResponseEntity<SimpleResponseStructure> updateVariantBy(String productId, List<String> specNames);

    ResponseEntity<ResponseStructure<ProductResponse>> getProductById(String productId);

    ResponseEntity<ResponseStructure<List<ProductResponse>>> getProducts(String text);
}
