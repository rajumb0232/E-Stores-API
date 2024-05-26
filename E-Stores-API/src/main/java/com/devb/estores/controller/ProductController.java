package com.devb.estores.controller;

import com.devb.estores.requestdto.SimpleProductRequest;
import com.devb.estores.requestdto.VaryingProductRequest;
import com.devb.estores.responsedto.ProductResponse;
import com.devb.estores.service.ProductService;
import com.devb.estores.util.ResponseStructure;
import com.devb.estores.util.SimpleResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<SimpleResponseStructure> updateVariantBy(@PathVariable String productId, @RequestParam List<String> specNames){
        return productService.updateVariantBy(productId, specNames);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ResponseStructure<ProductResponse>> getProductById(@PathVariable String productId){
        return productService.getProductById(productId);
    }

    @GetMapping("/products")
    public ResponseEntity<ResponseStructure<List<ProductResponse>>> getProducts(String text){
        return productService.getProducts(text);
    }
}
