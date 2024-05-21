package com.devb.estores.controller;

import com.devb.estores.model.ProductType;
import com.devb.estores.service.ProductTypeService;
import com.devb.estores.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.base_url}")
@AllArgsConstructor
public class ProductTypeController {

    private ProductTypeService productTypeService;

    @PostMapping("/top-categories/{topCategory}/sub-categories/{subCategory}/product-types")
    public ResponseEntity<ResponseStructure<List<ProductType>>> addProductTypes(@PathVariable String topCategory,
                                                                                @PathVariable String subCategory,
                                                                                @RequestParam String[] productTypes){
        return productTypeService.addProductTypes(topCategory, subCategory, productTypes);
    }
}
