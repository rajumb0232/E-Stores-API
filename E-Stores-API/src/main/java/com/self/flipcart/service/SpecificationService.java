package com.self.flipcart.service;

import com.self.flipcart.requestdto.SpecificationRequest;
import com.self.flipcart.responsedto.SpecificationResponse;
import com.self.flipcart.util.ResponseStructure;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SpecificationService {
    ResponseEntity<ResponseStructure<List<SpecificationResponse>>> saveSpecification(List<SpecificationRequest> specRequest, String productId);
}
