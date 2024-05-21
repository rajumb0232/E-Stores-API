package com.devb.estores.serviceimpl;

import com.devb.estores.enums.ImageType;
import com.devb.estores.model.Image;
import com.devb.estores.repository.ImageRepo;
import com.devb.estores.repository.StoreRepo;
import com.devb.estores.service.ImageService;
import com.devb.estores.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepo imageRepo;
    private final StoreRepo storeRepo;

    @Override
    public ResponseEntity<ResponseStructure<String>> addStoreImage(String storeId, MultipartFile image) {
        System.err.println(storeId);
        return storeRepo.findById(storeId).map(store -> {
            if (store.getLogoLink() != null) {
                String[] a = store.getLogoLink().split("/");
                imageRepo.deleteById(a[a.length - 1]);
            }
            Image storeImage = new Image();
            storeImage.setContentType(image.getContentType());
            storeImage.setImageType(ImageType.LOGO);
            try {
                storeImage.setImageBytes(image.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Image i = imageRepo.save(storeImage);
            store.setLogoLink("/images/" + i.getImageId());
            storeRepo.save(store);
            return ResponseEntity.ok(new ResponseStructure<String>().setStatus(HttpStatus.OK.value())
                    .setMessage("Successfully save image").setData("/images/" + i.getImageId()));
        }).orElseThrow();
    }

    @Override
    public ResponseEntity<byte[]> getImageById(String imageId) {
        return imageRepo.findById(imageId).map(image -> ResponseEntity.ok()
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getImageBytes().length)
                .cacheControl(CacheControl.maxAge(Duration.ofDays(1)))
                .body(image.getImageBytes())).orElseThrow();
    }
}
