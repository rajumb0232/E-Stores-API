package com.devb.estores.controller;

import com.devb.estores.util.AppResponseBuilder;
import com.devb.estores.util.ResponseStructure;
import com.devb.estores.requestdto.VariantRequest;
import com.devb.estores.responsedto.VariantResponse;
import com.devb.estores.service.VariantService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.base-url}")
@AllArgsConstructor
public class VariantController {

    private final VariantService variantService;
    private final AppResponseBuilder responseBuilder;

    @PostMapping("/products/{productId}/variants")
    public ResponseEntity<ResponseStructure<List<VariantResponse>>> updateVariants(@RequestBody List<VariantRequest> variantRequest,
                                                                                   @PathVariable String productId){
        List<VariantResponse> response = variantService.updateVariants(variantRequest, productId);
        return responseBuilder.success(HttpStatus.OK, "variants added", response);
    }
}
