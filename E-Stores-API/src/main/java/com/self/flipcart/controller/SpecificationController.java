package com.self.flipcart.controller;

import com.self.flipcart.requestdto.SpecificationRequest;
import com.self.flipcart.responsedto.SpecificationResponse;
import com.self.flipcart.service.SpecificationService;
import com.self.flipcart.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fkv1")
@AllArgsConstructor
@CrossOrigin(allowCredentials = "true", origins = "http://localhost:5173/")
public class SpecificationController {

    private SpecificationService specificationService;

    @PostMapping("/products/{productId}/specifications")
    private ResponseEntity<ResponseStructure<List<SpecificationResponse>>> saveSpecification(@RequestBody List<SpecificationRequest> specRequest,
                                                                                             @PathVariable String productId){
        return specificationService.saveSpecification(specRequest, productId);
    }
}
