package com.self.flipcart.controller;

import com.self.flipcart.model.ProductType;
import com.self.flipcart.service.ProductTypeService;
import com.self.flipcart.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fkv1")
@AllArgsConstructor
@CrossOrigin(allowCredentials = "true", origins = "http://localhost:5173/")
public class ProductTypeController {

    private ProductTypeService productTypeService;

    @PostMapping("/top-categories/{topCategory}/sub-categories/{subCategory}/product-types")
    public ResponseEntity<ResponseStructure<List<ProductType>>> addProductTypes(@PathVariable String topCategory,
                                                                               @PathVariable String subCategory,
                                                                               @RequestParam String[] productTypes){
        return productTypeService.addProductTypes(topCategory, subCategory, productTypes);
    }
}
