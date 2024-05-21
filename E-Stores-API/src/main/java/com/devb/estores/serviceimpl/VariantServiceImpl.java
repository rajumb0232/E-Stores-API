package com.devb.estores.serviceimpl;

import com.devb.estores.mapper.VariantMapper;
import com.devb.estores.model.Variant;
import com.devb.estores.model.VaryingProduct;
import com.devb.estores.repository.ProductRepo;
import com.devb.estores.repository.VariantRepo;
import com.devb.estores.requestdto.VariantRequest;
import com.devb.estores.responsedto.VariantResponse;
import com.devb.estores.service.VariantService;
import com.devb.estores.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VariantServiceImpl implements VariantService {

    private VariantRepo variantRepo;
    private ProductRepo productRepo;
    private SpecSuggestService specSuggestService;

    @Override
    public ResponseEntity<ResponseStructure<List<VariantResponse>>> updateVariants(List<VariantRequest> variantRequest, String productId) {

        return productRepo.findById(productId).map(product -> {
            /* The product found should be a varying product
             * */
            if (product instanceof VaryingProduct varyingProduct) {
                variantRequest.forEach(req -> {

                    /* Validating if the given set specs are same are the defined variantBy in the product
                     * This ensures that there are same exact set of specs in every variant with differing values
                     * */
                    Set<String> names = new HashSet<>(req.getSpecifications().keySet());
                    System.out.println(names);
                    System.out.println(varyingProduct.getVariantBy());
                    if (!varyingProduct.getVariantBy().equals(names))
                        throw new RuntimeException("Invalid variant group made. expected to be a group of " + varyingProduct.getVariantBy());

                });

                /* Saving the variants to the database by iterating on each
                 * */
                Set<Variant> variants = variantRequest.stream()
                        .map(VariantMapper::mapToVariantEntity)
                        .map(variant -> variantRepo.save(variant))
                        .collect(Collectors.toSet());

                varyingProduct.getVariants().addAll(variants);
                productRepo.save(varyingProduct);

                return ResponseEntity.ok(new ResponseStructure<List<VariantResponse>>()
                        .setData(VariantMapper.mapToVariantResponseList(variants))
                        .setMessage("Variants added successfully")
                        .setStatus(HttpStatus.OK.value()));
            } else throw new RuntimeException("The product is simple product, not a varying product");
        }).orElseThrow();
    }
}


