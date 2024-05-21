package com.devb.estores.controller;

import com.devb.estores.service.ImageService;
import com.devb.estores.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("${app.base_url}")
@AllArgsConstructor
public class ImageController {

    private ImageService imageService;

    @PostMapping("/stores/{storeId}/images")
    private ResponseEntity<ResponseStructure<String>> addStoreImage(@PathVariable String storeId, MultipartFile image){
        return imageService.addStoreImage(storeId, image);
    }

    @GetMapping("/images/{imageId}")
    private ResponseEntity<byte[]> getImageById(@PathVariable String imageId){
        return imageService.getImageById(imageId);
    }
}
