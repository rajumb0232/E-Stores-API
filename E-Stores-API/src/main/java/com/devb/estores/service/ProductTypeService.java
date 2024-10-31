package com.devb.estores.service;

import com.devb.estores.model.ProductType;

import java.util.List;

public interface ProductTypeService {
    List<ProductType> addProductTypes(String topCategory, String subCategory, String[] productTypes);
}
