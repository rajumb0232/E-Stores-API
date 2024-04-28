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

    private SpecificationRepo specificationRepo;
    private ProductRepo productRepo;
    private ResponseStructure<List<SpecificationResponse>> structure;

    @Override
    public ResponseEntity<ResponseStructure<List<SpecificationResponse>>> saveSpecification(List<SpecificationRequest> specRequest, String productId) {
        return productRepo.findById(productId).map(product -> {
            List<Specification> specifications = specRequest.stream()
                    .map(spec -> Specification.builder()
                            .name(spec.getName())
                            .value(spec.getValue())
                            .product(product)
                            .build())
                    // setting up the existing ID, if the spec is already associated with the product
                    .peek(spec -> specificationRepo.findByNameAndProduct(spec.getName(), product)
                            .ifPresent(exSpec -> spec.setSpecificationId(exSpec.getSpecificationId())))
                    .collect(Collectors.toList());
            specifications = specificationRepo.saveAll(specifications);
            product.getSpecification().addAll(specifications);
            productRepo.save(product);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(structure.setStatus(HttpStatus.CREATED.value())
                            .setMessage("Specifications Created")
                            .setData(SpecificationMapper.mapToSpecificationResponse(specifications)));
        }).orElseThrow();
    }
}
