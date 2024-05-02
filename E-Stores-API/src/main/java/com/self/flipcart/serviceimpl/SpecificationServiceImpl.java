package com.self.flipcart.serviceimpl;

import com.self.flipcart.mapper.SpecificationMapper;
import com.self.flipcart.model.Specification;
import com.self.flipcart.repository.ProductRepo;
import com.self.flipcart.repository.SpecificationRepo;
import com.self.flipcart.requestdto.SpecificationRequest;
import com.self.flipcart.responsedto.SpecificationResponse;
import com.self.flipcart.service.SpecificationService;
import com.self.flipcart.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SpecificationServiceImpl implements SpecificationService {

    private final SpecificationRepo specificationRepo;
    private final ProductRepo productRepo;

    @Override
    public ResponseEntity<ResponseStructure<List<SpecificationResponse>>> saveSpecification(List<SpecificationRequest> specRequest, String productId) {
        return productRepo.findById(productId).map(product -> {
            /* Iterating over each spec request given
             * creates new if it is not present by name, or updates the existing
             * */
            List<Specification> specifications = specRequest.stream().map(specReq -> {
                Specification specification = specificationRepo.findByNameAndProduct(specReq.getName().toLowerCase(), product)
                        .map(exSpec -> {
                            exSpec.setName(specReq.getName());
                            exSpec.setValue(specReq.getValue());
                            return specificationRepo.save(exSpec);
                        }).orElseGet(() -> {
                            Specification spec = new Specification();
                            spec.setProduct(product);
                            spec.setName(specReq.getName());
                            spec.setValue(specReq.getValue());
                            spec = specificationRepo.save(spec);
                            /* adding specification to the product */
                            product.getSpecification().add(spec);
                            return spec;
                        });
                return specificationRepo.save(specification);
            }).collect(Collectors.toList());

            productRepo.save(product);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ResponseStructure<List<SpecificationResponse>>()
                            .setStatus(HttpStatus.CREATED.value())
                            .setMessage("Specifications Created")
                            .setData(SpecificationMapper.mapToSpecificationResponse(specifications)));
        }).orElseThrow();
    }
}
