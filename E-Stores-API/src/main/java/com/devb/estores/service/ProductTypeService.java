package com.devb.estores.service;

import com.devb.estores.model.ProductType;
import com.devb.estores.util.ResponseStructure;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductTypeService {
    ResponseEntity<ResponseStructure<List<ProductType>>> addProductTypes(String topCategory, String subCategory, String[] productTypes);
}
