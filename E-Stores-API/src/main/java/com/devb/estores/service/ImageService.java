package com.devb.estores.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String addStoreImage(String storeId, MultipartFile image);

    ResponseEntity<byte[]> getImageById(String imageId);
}
