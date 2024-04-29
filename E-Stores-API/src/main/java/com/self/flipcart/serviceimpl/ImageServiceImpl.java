package com.self.flipcart.serviceimpl;

import com.self.flipcart.enums.ImageType;
import com.self.flipcart.model.Image;
import com.self.flipcart.repository.ImageRepo;
import com.self.flipcart.repository.StoreRepo;
import com.self.flipcart.service.ImageService;
import com.self.flipcart.util.ResponseStructure;
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

    private ImageRepo imageRepo;
    private StoreRepo storeRepo;

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
