package com.self.flipcart.controller;

import com.self.flipcart.service.ImageService;
import com.self.flipcart.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/fkv1")
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
