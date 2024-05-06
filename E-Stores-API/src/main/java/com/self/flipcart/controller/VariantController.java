package com.self.flipcart.controller;

import com.self.flipcart.requestdto.VariantRequest;
import com.self.flipcart.responsedto.VariantResponse;
import com.self.flipcart.service.VariantService;
import com.self.flipcart.util.ResponseStructure;
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
