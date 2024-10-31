package com.devb.estores.service;

import com.devb.estores.requestdto.VariantRequest;
import com.devb.estores.responsedto.VariantResponse;

import java.util.List;

public interface VariantService {
    List<VariantResponse> updateVariants(List<VariantRequest> variantRequest, String productId);
}
