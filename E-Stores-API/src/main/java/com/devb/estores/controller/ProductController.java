package com.devb.estores.controller;

import com.devb.estores.requestdto.SimpleProductRequest;
import com.devb.estores.requestdto.VaryingProductRequest;
import com.devb.estores.responsedto.ProductResponse;
import com.devb.estores.responsedto.SimpleProductResponse;
import com.devb.estores.responsedto.VaryingProductResponse;
import com.devb.estores.service.ProductService;
import com.devb.estores.util.AppResponseBuilder;
import com.devb.estores.util.ResponseStructure;
import com.devb.estores.util.SimpleResponseStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.base-url}")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final AppResponseBuilder responseBuilder;

    @Operation(
            responses = {
                    @ApiResponse(responseCode = "201", description = "Product added, type - simple", content = {
                            @Content(schema = @Schema(implementation = SimpleProductResponse.class))
                    })
            }
    )
    @PostMapping("/stores/{storeId}/simple-products")
    public ResponseEntity<ResponseStructure<ProductResponse>> addSimpleProduct(@RequestBody SimpleProductRequest productRequest,
                                                                               @PathVariable String storeId){
        ProductResponse response = productService.addProduct(productRequest, storeId);
        return responseBuilder.success(HttpStatus.CREATED, "Product added, type - simple", response);
    }

    @Operation(
            responses = {
                    @ApiResponse(responseCode = "201", description = "Varying Product Added.", content = {
                            @Content(schema = @Schema(implementation = VaryingProductResponse.class))
                    })
            }
    )
    @PostMapping("/stores/{storeId}/varying-products")
    public ResponseEntity<ResponseStructure<ProductResponse>> addVaryingProduct(@RequestBody VaryingProductRequest productRequest,
                                                                         @PathVariable String storeId){
        ProductResponse response = productService.addProduct(productRequest, storeId);
        return responseBuilder.success(HttpStatus.CREATED, "Product added, type - varying", response);
    }

    @PatchMapping("/products/{productId}")
    public ResponseEntity<SimpleResponseStructure> updateVariantBy(@PathVariable String productId, @RequestParam List<String> specNames){
        productService.updateVariantBy(productId, specNames);
        return responseBuilder.success(HttpStatus.OK, "VariantBy Updated");
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ResponseStructure<ProductResponse>> getProductById(@PathVariable String productId){
        ProductResponse response = productService.getProductById(productId);
        return responseBuilder.success(HttpStatus.FOUND, "Product found", response);
    }

    @GetMapping("/products")
    public ResponseEntity<ResponseStructure<List<ProductResponse>>> getProducts(String text){
        List<ProductResponse> response = productService.getProducts(text);
        return responseBuilder.success(HttpStatus.FOUND, "Products found", response);
    }
}
