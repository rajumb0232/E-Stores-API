package com.devb.estores.controller;

import com.devb.estores.util.ResponseStructure;
import com.devb.estores.requestdto.VariantRequest;
import com.devb.estores.responsedto.VariantResponse;
import com.devb.estores.service.VariantService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.base_url}")
@AllArgsConstructor
public class VariantController {

    private VariantService variantService;

    @PostMapping("/products/{productId}/variants")
    public ResponseEntity<ResponseStructure<List<VariantResponse>>> updateVariants(@RequestBody List<VariantRequest> variantRequest,
                                                                                   @PathVariable String productId){
        return variantService.updateVariants(variantRequest, productId);
    }
}
