package com.devb.estores.serviceimpl;

import com.devb.estores.exceptions.InvalidSubCategoryException;
import com.devb.estores.exceptions.ProductTypeNotFoundException;
import com.devb.estores.exceptions.StoreNotFoundException;
import com.devb.estores.mapper.ProductMapper;
import com.devb.estores.model.Product;
import com.devb.estores.model.ProductType;
import com.devb.estores.model.Store;
import com.devb.estores.repository.ProductRepo;
import com.devb.estores.repository.ProductTypeRepo;
import com.devb.estores.repository.StoreRepo;
import com.devb.estores.requestdto.ProductRequest;
import com.devb.estores.requestdto.VaryingProductRequest;
import com.devb.estores.responsedto.ProductResponse;
import com.devb.estores.service.ProductService;
import com.devb.estores.util.ResponseStructure;
import com.devb.estores.util.SimpleResponseStructure;
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
    private ProductMapper productMapper;

    @Override
    public ProductResponse addProduct(ProductRequest productRequest, String storeId) {
        return storeRepo.findById(storeId).map(store ->
                {
                    /* Verifying if the specified subCategory is one of the subCategory of topCategory
                     */
                    if (!store.getTopCategory().getSubCategories().contains(productRequest.getSubCategory()))
                        throw new InvalidSubCategoryException("failed to add product");

                    /* Verifying if the productType available under specified topCategory and subCategory
                     and type
                     */
                    return typeRepo.findByTypeNameAndSubCategoryAndTopCategory(
                                    productRequest.getProductType().toLowerCase(),
                                    productRequest.getSubCategory(),
                                    store.getTopCategory())
                            .map(type -> {
                                Product product = productMapper.mapToNewProductEntity(productRequest);

                                /* The Product Specification and Variant Specification should not be the same,
                                 the specifications that are a part of variants are removed from the product specifications list.
                                 */
                                if (productRequest instanceof VaryingProductRequest varyingProductRequest) {
                                    removeMatchingSpecFromProduct(product, varyingProductRequest.getVariantBy());
                                }

                                product.setProductTypeId(type.getTypeId());
                                product.setStoreId(storeId);
                                product = productRepo.save(product);

                                /* Updating Spec Suggest list for the products type, this can further be used for
                                Auto suggestion of specifications to the seller
                                */
                                specSuggestService.updateSpecSuggest(product.getSpecifications(), product.getProductTypeId());

                                return productMapper.mapToProductPageResponse(product, type, store);
                            }).orElseThrow(() -> new ProductTypeNotFoundException("Failed to add Product"));
                }
        ).orElseThrow(() -> new StoreNotFoundException("Failed to add Product"));
    }

    private void removeMatchingSpecFromProduct(Product product, Set<String> specNames) {
        Map<String, String> specifications = product.getSpecifications();
        specNames.forEach(specifications::remove);
        product.setSpecifications(specifications);
    }

    @Override
    public void updateVariantBy(String productId, List<String> specNames) {
        productRepo.findById(productId).map(product -> {
            removeMatchingSpecFromProduct(product, new HashSet<>(specNames));
            return productRepo.save(product);
        }).orElseThrow();
    }

    @Override
    public ProductResponse getProductById(String productId) {
        return productRepo.findById(productId).map(product -> {
            ProductType type = typeRepo.findById(product.getProductTypeId()).orElseThrow();
            Store store = storeRepo.findById(product.getStoreId()).orElseThrow();

            return productMapper.mapToProductPageResponse(product, type, store);
        }).orElseThrow();
    }

    @Override
    public List<ProductResponse> getProducts(String text) {
        return productRepo.findByTitleIgnoreCaseLikeOrDescriptionIgnoreCaseLike(text, text)
                .stream().map(product -> {
                    ProductType type = typeRepo.findById(product.getProductTypeId()).orElseThrow();
                    Store store = storeRepo.findById(product.getStoreId()).orElseThrow();
                    return productMapper.mapToProductPageResponse(product, type, store);
                }).toList();
    }
}
