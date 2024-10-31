package com.devb.estores.serviceimpl;

import com.devb.estores.exceptions.InvalidOperationException;
import com.devb.estores.exceptions.InvalidVariantGroupException;
import com.devb.estores.mapper.VariantMapper;
import com.devb.estores.model.Variant;
import com.devb.estores.model.VaryingProduct;
import com.devb.estores.repository.ProductRepo;
import com.devb.estores.repository.VariantRepo;
import com.devb.estores.requestdto.VariantRequest;
import com.devb.estores.responsedto.VariantResponse;
import com.devb.estores.service.VariantService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VariantServiceImpl implements VariantService {

    private final VariantRepo variantRepo;
    private final ProductRepo productRepo;
    private final VariantMapper variantMapper;

    @Override
    @Transactional
    public List<VariantResponse> updateVariants(List<VariantRequest> variantRequest, String productId) {

        return productRepo.findById(productId).map(product -> {

            /* The product found should be a varying product
             * */
            if (product instanceof VaryingProduct varyingProduct) {
                Set<Variant> variants = this.validateVariantsConsistency(variantRequest, varyingProduct.getVariantBy());

                varyingProduct.getVariants().addAll(variants);
                productRepo.save(varyingProduct);

                return variantMapper.mapToVariantResponseList(variants);
            } else throw new InvalidOperationException("The product is simple product, not a varying product");
        }).orElseThrow();
    }

    Set<Variant> validateVariantsConsistency(List<VariantRequest> variantRequest, Set<String> variantBy) {
        return variantRequest.stream()
                .map(req -> {
                    /*
                     * Validating if the given set specs are same are the defined variantBy in the product
                     * This ensures that there are same exact set of specs in every variant with differing values
                     * */
                    if (!req.getSpecifications().keySet().equals(variantBy))
                        throw new InvalidVariantGroupException("Invalid variant group made. Expected to be a group of " + variantBy);

                    return variantMapper.mapToVariantEntity(req);

                }).map(variantRepo::save)
                .collect(Collectors.toSet());
    }
}


