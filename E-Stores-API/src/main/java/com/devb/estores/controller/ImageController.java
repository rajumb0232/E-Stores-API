package com.devb.estores.controller;

import com.devb.estores.service.ImageService;
import com.devb.estores.util.AppResponseBuilder;
import com.devb.estores.util.ResponseStructure;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("${app.base_url}")
@AllArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final AppResponseBuilder responseBuilder;

    @PostMapping("/stores/{storeId}/images")
    public ResponseEntity<ResponseStructure<String>> addStoreImage(@PathVariable String storeId, @NotNull MultipartFile image){
        String url = imageService.addStoreImage(storeId, image);
        return responseBuilder.success(HttpStatus.OK, "Image Uploaded", url);
    }

    @GetMapping("/images/{imageId}")
    public ResponseEntity<byte[]> getImageById(@PathVariable String imageId){
        return imageService.getImageById(imageId);
    }
}
