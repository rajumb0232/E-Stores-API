package com.self.flipcart.mapper;

import com.self.flipcart.model.Product;
import com.self.flipcart.model.ProductType;
import com.self.flipcart.model.Store;
import com.self.flipcart.requestdto.ProductRequest;
import com.self.flipcart.responsedto.CategoryResponse;
import com.self.flipcart.responsedto.ProductPageResponse;
import com.self.flipcart.responsedto.ProductResponse;

public class ProductMapper {

    public static ProductResponse mapToProductPageResponse(Product product, ProductType productType, Store store) {
        return ProductPageResponse.builder()
                .productId(product.getProductId())
                .productTitle(product.getTitle())
                .productQuantity(product.getStockQuantity())
                .description(product.getDescription())
                .availabilityStatus(product.getAvailabilityStatus())
                .avgRating(product.getAvgRating())
                .totalOrders(product.getTotalOrders())
                .totalReviews(product.getTotalReviews())
                .category(CategoryResponse.builder()
                        .subCategory(product.getSubCategory())
                        .topCategory(product.getTopCategory())
                        .productType(productType.getTypeName())
                        .build())
                .store(StoreMapper.mapToStoreCardResponse(store))
                .specification(product.getSpecification())
                .build();
    }

    public static Product mapToProductRequest(ProductRequest productRequest, Product product) {
        product.setTitle(productRequest.getProductTitle());
        product.setDescription(productRequest.getDescription());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setSubCategory(productRequest.getSubCategory().name());
        product.setTopCategory(productRequest.getTopCategory().name());
        return product;
    }
}
