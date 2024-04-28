package com.self.flipcart.serviceimpl;

import com.self.flipcart.enums.AvailabilityStatus;
import com.self.flipcart.exceptions.InvalidSubCategoryException;
import com.self.flipcart.exceptions.ProductTypeNotFoundException;
import com.self.flipcart.exceptions.StoreNotFoundByIdException;
import com.self.flipcart.mapper.ProductMapper;
import com.self.flipcart.model.Product;
import com.self.flipcart.repository.ProductRepo;
import com.self.flipcart.repository.ProductTypeRepo;
import com.self.flipcart.repository.StoreRepo;
import com.self.flipcart.requestdto.ProductRequest;
import com.self.flipcart.responsedto.ProductResponse;
import com.self.flipcart.service.ProductService;
import com.self.flipcart.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private StoreRepo storeRepo;
    private ProductTypeRepo typeRepo;
    private ProductRepo productRepo;
    private ResponseStructure<ProductResponse> structure;

    @Override
    public ResponseEntity<ResponseStructure<ProductResponse>> addProduct(ProductRequest productRequest, String storeId) {
        return storeRepo.findById(storeId).map(store ->
                {
                    if (!productRequest.getTopCategory().getSubCategories().contains(productRequest.getSubCategory()))
                        throw new InvalidSubCategoryException("failed to add product");
                    return typeRepo.findByTypeNameAndSubCategoryAndTopCategory(productRequest.getProductType(), productRequest.getSubCategory(), productRequest.getTopCategory())
                            .map(type -> {
                                Product product = ProductMapper.mapToProductRequest(productRequest, new Product());
                                product.setAvailabilityStatus(validateAndGetProductAvailabilityStatus(productRequest.getStockQuantity()));
                                product.setProductTypeId(type.getTypeId());
                                product.setStoreId(storeId);
                                product.setSpecification(new ArrayList<>());
                                product = productRepo.save(product);
                                return ResponseEntity.ok(structure.setStatus(HttpStatus.OK.value())
                                        .setMessage("Product saved successfully")
                                        .setData(ProductMapper.mapToProductPageResponse(product, type, store)));
                            }).orElseThrow(() -> new ProductTypeNotFoundException("Failed to add Product"));
                }
        ).orElseThrow(() -> new StoreNotFoundByIdException("Failed to add Product"));
    }

    private String validateAndGetProductAvailabilityStatus(int quantity) {
        return quantity < 1
                ? AvailabilityStatus.OUT_OF_STOCK.name()
                : (quantity > 1 && quantity < 10)
                ? AvailabilityStatus.ONLY_FEW_LEFT.name()
                : AvailabilityStatus.AVAILABLE.name();
    }

}
