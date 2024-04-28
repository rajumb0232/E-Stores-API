package com.self.flipcart.controller;

import com.self.flipcart.requestdto.ProductRequest;
import com.self.flipcart.responsedto.ProductResponse;
import com.self.flipcart.service.ProductService;
import com.self.flipcart.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fkv1")
@AllArgsConstructor
@CrossOrigin(allowCredentials = "true", origins = "http://localhost:5173/")
public class ProductController {

    private ProductService productService;

    @PostMapping("/stores/{storeId}/products")
    public ResponseEntity<ResponseStructure<ProductResponse>> addProduct(@RequestBody ProductRequest productRequest,
                                                                         @PathVariable String storeId){
        return productService.addProduct(productRequest, storeId);
    }
}
