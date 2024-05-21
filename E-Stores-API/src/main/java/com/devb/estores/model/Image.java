package com.devb.estores.model;

import com.devb.estores.enums.ImageType;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "images")
public class Image {
    @MongoId
    private String imageId;
    private ImageType imageType;
    private String contentType;
    private byte[] imageBytes;
}
