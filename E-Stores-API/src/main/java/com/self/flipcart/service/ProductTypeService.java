package com.self.flipcart.service;

import com.self.flipcart.model.ProductType;
import com.self.flipcart.util.ResponseStructure;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductTypeService {
    ResponseEntity<ResponseStructure<List<ProductType>>> addProductTypes(String topCategory, String subCategory, String[] productTypes);
}
