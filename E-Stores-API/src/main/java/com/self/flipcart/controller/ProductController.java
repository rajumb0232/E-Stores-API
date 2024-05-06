package com.self.flipcart.controller;

import com.self.flipcart.requestdto.SimpleProductRequest;
import com.self.flipcart.requestdto.VaryingProductRequest;
import com.self.flipcart.responsedto.ProductResponse;
import com.self.flipcart.service.ProductService;
import com.self.flipcart.util.ResponseStructure;
import com.self.flipcart.util.SimpleResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("${app.base_url}")
@AllArgsConstructor
public class ProductController {

    private ProductService productService;

    @PostMapping("/stores/{storeId}/simple-products")
    public ResponseEntity<ResponseStructure<ProductResponse>> addSimpleProduct(@RequestBody SimpleProductRequest productRequest,
                                                                         @PathVariable String storeId){
        return productService.addProduct(productRequest, storeId);
    }

    @PostMapping("/stores/{storeId}/varying-products")
    public ResponseEntity<ResponseStructure<ProductResponse>> addVaryingProduct(@RequestBody VaryingProductRequest productRequest,
                                                                         @PathVariable String storeId){
        return productService.addProduct(productRequest, storeId);
    }

    @PatchMapping("/products/{productId}")
    public ResponseEntity<SimpleResponseStructure> updateVariantBy(@PathVariable String productId, @RequestParam ArrayList<String> spec_names){
        return productService.updateVariantBy(productId, spec_names);
    }
}
