package com.self.flipcart.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class StoreImage extends Image{
    private String storeId;
}
