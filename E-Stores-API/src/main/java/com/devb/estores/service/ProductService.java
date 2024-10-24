package com.devb.estores.service;

import com.devb.estores.util.SimpleResponseStructure;
import com.devb.estores.requestdto.ProductRequest;
import com.devb.estores.responsedto.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse addProduct(ProductRequest productRequest, String storeId);

    /**
     * The Method updates the product specification list by removing the specs
     * associated with the specName in the variantBy, ensuring there are no repeated specs in product specification
     * and variant specification
     */
    void updateVariantBy(String productId, List<String> specNames);

    ProductResponse getProductById(String productId);

    List<ProductResponse> getProducts(String text);
}
