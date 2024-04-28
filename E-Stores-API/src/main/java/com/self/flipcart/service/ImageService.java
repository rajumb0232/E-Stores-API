package com.self.flipcart.service;

import com.self.flipcart.util.ResponseStructure;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    ResponseEntity<ResponseStructure> addStoreImage(String storeId, MultipartFile image);

    ResponseEntity<byte[]> getImageById(String imageId);
}
