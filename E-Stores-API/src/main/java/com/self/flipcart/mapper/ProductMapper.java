package com.self.flipcart.mapper;

import com.self.flipcart.enums.AvailabilityStatus;
import com.self.flipcart.model.Product;
import com.self.flipcart.model.ProductType;
import com.self.flipcart.model.Store;
import com.self.flipcart.requestdto.ProductRequest;
import com.self.flipcart.responsedto.CategoryResponse;
import com.self.flipcart.responsedto.ProductPageResponse;
import com.self.flipcart.responsedto.ProductResponse;

public class ProductMapper {

    /**
     * The method used to map the product entity to product response
     * The method doesn't map the fields such as avgRating, totalOrders, totalReviews which has to be generated on the fly
     */
    public static ProductResponse mapToProductPageResponse(Product product, ProductType productType, Store store) {
        return ProductPageResponse.builder()
                .productId(product.getProductId())
                .productTitle(product.getTitle())
                .stockQuantity(product.getStockQuantity())
                .description(product.getDescription())
                .availabilityStatus(validateAndGetProductAvailabilityStatus(product.getStockQuantity()))
                .category(CategoryResponse.builder()
                        .subCategory(productType.getSubCategory().getName())
                        .topCategory(productType.getTopCategory().getName())
                        .productType(productType.getTypeName())
                        .build())
                .store(StoreMapper.mapToStoreCardResponse(store))
                .specification(product.getSpecification())
                .build();
    }

    private static String validateAndGetProductAvailabilityStatus(int quantity) {
        return quantity < 1
                ? AvailabilityStatus.OUT_OF_STOCK.name()
                : (quantity > 1 && quantity < 10)
                ? AvailabilityStatus.ONLY_FEW_LEFT.name()
                : AvailabilityStatus.AVAILABLE.name();
    }

    public static Product mapToProductEntity(ProductRequest productRequest, Product product) {
        product.setTitle(productRequest.getProductTitle());
        product.setDescription(productRequest.getDescription());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setSubCategory(productRequest.getSubCategory().name());
        product.setTopCategory(productRequest.getTopCategory().name());
        return product;
    }
}
