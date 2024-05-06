package com.self.flipcart.serviceimpl;

import com.self.flipcart.exceptions.InvalidSubCategoryException;
import com.self.flipcart.exceptions.ProductTypeNotFoundException;
import com.self.flipcart.exceptions.StoreNotFoundException;
import com.self.flipcart.mapper.ProductMapper;
import com.self.flipcart.model.Product;
import com.self.flipcart.repository.ProductRepo;
import com.self.flipcart.repository.ProductTypeRepo;
import com.self.flipcart.repository.StoreRepo;
import com.self.flipcart.repository.VariantRepo;
import com.self.flipcart.requestdto.ProductRequest;
import com.self.flipcart.requestdto.VaryingProductRequest;
import com.self.flipcart.responsedto.ProductResponse;
import com.self.flipcart.service.ProductService;
import com.self.flipcart.util.ResponseStructure;
import com.self.flipcart.util.SimpleResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final StoreRepo storeRepo;
    private final ProductTypeRepo typeRepo;
    private final ProductRepo productRepo;
    private SpecSuggestService specSuggestService;
    private VariantRepo variantRepo;

    @Override
    public ResponseEntity<ResponseStructure<ProductResponse>> addProduct(ProductRequest productRequest, String storeId) {
        return storeRepo.findById(storeId).map(store ->
                {
                    /* Verifying if the specified subCategory is one of the subCategory of topCategory
                     * */
                    if (!store.getTopCategory().getSubCategories().contains(productRequest.getSubCategory()))
                        throw new InvalidSubCategoryException("failed to add product");

                    /* Verifying if the productType available under specified topCategory and subCategory
                     * */
                    return typeRepo.findByTypeNameAndSubCategoryAndTopCategory(
                                    productRequest.getProductType().toLowerCase(),
                                    productRequest.getSubCategory(),
                                    store.getTopCategory())
                            .map(type -> {
                                Product product = ProductMapper.mapToNewProductEntity(productRequest);

                                /* The Product Specification and Variant Specification should not be the same,
                                 * the specifications in variants are removed from the product specifications list.
                                 * */
                                if (productRequest instanceof VaryingProductRequest varyingProductRequest) {
                                    removeMatchingSpecFromProduct(product, varyingProductRequest.getVariantBy());
                                }

                                product.setProductTypeId(type.getTypeId());
                                product.setStoreId(storeId);
                                product = productRepo.save(product);
                                specSuggestService.updateSpecSuggest(product.getSpecifications(), product.getProductTypeId());

                                return ResponseEntity.ok(new ResponseStructure<ProductResponse>().setStatus(HttpStatus.OK.value())
                                        .setMessage("Product saved successfully")
                                        .setData(ProductMapper.mapToProductPageResponse(product, type, store)));
                            }).orElseThrow(() -> new ProductTypeNotFoundException("Failed to add Product"));
                }
        ).orElseThrow(() -> new StoreNotFoundException("Failed to add Product"));
    }

    @Override
    public ResponseEntity<SimpleResponseStructure> updateVariantBy(String productId, List<String> specNames) {
        System.out.println(specNames);
        return productRepo.findById(productId).map(product -> {
            boolean result = removeMatchingSpecFromProduct(product, new HashSet<>(specNames));
            if (result) productRepo.save(product);

            return ResponseEntity
                    .ok(new SimpleResponseStructure()
                            .setMessage("VariantBy updated successfully")
                            .setStatus(HttpStatus.OK.value()));
        }).orElseThrow();
    }

    private boolean removeMatchingSpecFromProduct(Product product, Set<String> specNames) {
        Map<String, String> invalidSpecs = new HashMap<>();
        if (product.getSpecifications() != null) {
            product.getSpecifications().forEach((k, v) -> specNames.forEach(name -> {
                if (name.equalsIgnoreCase(k)) invalidSpecs.put(k, v);
            }));
            product.getSpecifications().forEach((k, v) -> {
                if (("size").equalsIgnoreCase(k)) invalidSpecs.put(k, v);
                if (("weight").equalsIgnoreCase(k)) invalidSpecs.put(k, v);
                if (("liter").equalsIgnoreCase(k)) invalidSpecs.put(k, v);
            });
        }

        invalidSpecs.forEach((k, v) -> product.getSpecifications().remove(k));
        return true;
    }
}
