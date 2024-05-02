package com.self.flipcart.serviceimpl;

import com.self.flipcart.exceptions.InvalidSubCategoryException;
import com.self.flipcart.exceptions.ProductTypeNotFoundException;
import com.self.flipcart.exceptions.StoreNotFoundException;
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

    private final StoreRepo storeRepo;
    private final ProductTypeRepo typeRepo;
    private final ProductRepo productRepo;

    @Override
    public ResponseEntity<ResponseStructure<ProductResponse>> addProduct(ProductRequest productRequest, String storeId) {
        return storeRepo.findById(storeId).map(store ->
                {
                    if (!productRequest.getTopCategory().getSubCategories().contains(productRequest.getSubCategory()))
                        throw new InvalidSubCategoryException("failed to add product");
                    return typeRepo.findByTypeNameAndSubCategoryAndTopCategory(productRequest.getProductType().toLowerCase(), productRequest.getSubCategory(), productRequest.getTopCategory())
                            .map(type -> {
                                Product product = ProductMapper.mapToProductEntity(productRequest, new Product());
                                product.setProductTypeId(type.getTypeId());
                                product.setStoreId(storeId);
                                product.setSpecification(new ArrayList<>());
                                product = productRepo.save(product);
                                return ResponseEntity.ok(new ResponseStructure<ProductResponse>().setStatus(HttpStatus.OK.value())
                                        .setMessage("Product saved successfully")
                                        .setData(ProductMapper.mapToProductPageResponse(product, type, store)));
                            }).orElseThrow(() -> new ProductTypeNotFoundException("Failed to add Product"));
                }
        ).orElseThrow(() -> new StoreNotFoundException("Failed to add Product"));
    }
}
