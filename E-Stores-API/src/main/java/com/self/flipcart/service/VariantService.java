package com.self.flipcart.service;

import com.self.flipcart.requestdto.VariantRequest;
import com.self.flipcart.responsedto.VariantResponse;
import com.self.flipcart.util.ResponseStructure;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface VariantService {
    ResponseEntity<ResponseStructure<List<VariantResponse>>> updateVariants(List<VariantRequest> variantRequest, String productId);
}
