package com.devb.estores.service;

import com.devb.estores.util.ResponseStructure;
import com.devb.estores.requestdto.VariantRequest;
import com.devb.estores.responsedto.VariantResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface VariantService {
    ResponseEntity<ResponseStructure<List<VariantResponse>>> updateVariants(List<VariantRequest> variantRequest, String productId);
}
