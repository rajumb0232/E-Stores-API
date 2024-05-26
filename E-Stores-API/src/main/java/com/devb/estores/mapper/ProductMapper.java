package com.devb.estores.mapper;

import com.devb.estores.enums.AvailabilityStatus;
import com.devb.estores.model.*;
import com.devb.estores.responsedto.*;
import com.devb.estores.requestdto.ProductRequest;
import com.devb.estores.requestdto.SimpleProductRequest;
import com.devb.estores.requestdto.VaryingProductRequest;

import java.util.HashSet;

public class ProductMapper {

    private ProductMapper() {
        /*
         * Created private constructor to avoid Instantiation of class
         * */
    }

    /**
     * The method used to map the product entity to product response
     * The method doesn't map the fields such as avgRating, totalOrders, totalReviews which has to be generated on the fly
     */
    public static ProductResponse mapToProductPageResponse(Product product, ProductType productType, Store store) {
        ProductResponse productResponse = null;
        if(product instanceof SimpleProduct simpleProduct)
            productResponse = mapToSimpleProductResponse(new SimpleProductResponse(), simpleProduct, productType, store);
        else if (product instanceof VaryingProduct varyingProduct){
            VaryingProductResponse varyingProductResponse = (VaryingProductResponse)
                    mapToSimpleProductResponse(new VaryingProductResponse(), varyingProduct, productType, store);
            productResponse = mapToVaryingProductResponse(varyingProductResponse, varyingProduct);
        }
        if(productResponse != null){
            BaseProductResponse response = (BaseProductResponse) productResponse;
            response.setProductId(product.getProductId());
            response.setTitle(product.getTitle());
        }
        return productResponse;
    }

    private static ProductResponse mapToVaryingProductResponse(VaryingProductResponse response,
                                                               VaryingProduct varyingProduct) {
        response.setVariable(varyingProduct.isVariable());
        response.setVariantBy(varyingProduct.getVariantBy());
        response.setVariants(VariantMapper.mapToVariantResponseList(varyingProduct.getVariants()));
        response.setAvailabilityStatus(AvailabilityStatus.VARYING.name());
        return response;
    }

    private static SimpleProductResponse mapToSimpleProductResponse(SimpleProductResponse response,
                                                  Product product,
                                                  ProductType productType,
                                                  Store store) {
        response.setDescription(product.getDescription());
        response.setCategory(CategoryResponse.builder()
                .subCategory(productType.getSubCategory().getName())
                .topCategory(productType.getTopCategory().getName())
                .productType(productType.getTypeName())
                .build());
        response.setSpecifications(product.getSpecifications());
        response.setStore(StoreMapper.mapToStoreCardResponse(store));

        if(product instanceof SimpleProduct simpleProduct){
            response.setPrice(simpleProduct.getPrice());
            response.setStockQuantity(simpleProduct.getStockQuantity());
            response.setAvailabilityStatus(validateAndGetProductAvailabilityStatus(simpleProduct.getStockQuantity()));
        }
        return  response;
    }

    private static String validateAndGetProductAvailabilityStatus(int quantity) {
        if(quantity < 1){
            return AvailabilityStatus.OUT_OF_STOCK.name();
        } else return (quantity > 1 && quantity < 10)
                ? AvailabilityStatus.ONLY_FEW_LEFT.name()
                : AvailabilityStatus.AVAILABLE.name();
    }

    /**
     * The method mapToNewProductEntity maps the given productRequest to the respective child Entity
     * If the product is VaryingProduct, it doesn't map the field variants.
     *
     * @return T
     */
    public static Product mapToNewProductEntity(ProductRequest productRequest) {
        Product product = null;
        /* Getting the instance of respective child of the product
         * */
        if (productRequest instanceof SimpleProductRequest request) {
            product = SimpleProduct.builder()
                    .price(request.getPrice())
                    .stockQuantity(request.getStockQuantity())
                    .build();
        } else if (productRequest instanceof VaryingProductRequest request) {
            product = VaryingProduct.builder()
                    .variantBy(request.getVariantBy())
                    .variants(new HashSet<>())
                    .build();
        }
        /* Checking if the product never got initialized
         * */
        if (product != null) {
            product.setTitle(productRequest.getTitle());
            product.setVariable(productRequest instanceof VaryingProductRequest);
            product.setDescription(productRequest.getDescription());
            product.setSubCategory(productRequest.getSubCategory().name());
            product.setSpecifications(productRequest.getSpecifications());
        }
        return product;
    }
}
