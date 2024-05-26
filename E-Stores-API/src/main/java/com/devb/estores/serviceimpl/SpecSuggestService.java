package com.devb.estores.serviceimpl;

import com.devb.estores.repository.ProductTypeRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
@AllArgsConstructor
public class SpecSuggestService {

    private final ProductTypeRepo productTypeRepo;

    public void updateSpecSuggest(Map<String, String> specifications, String productTypeId) {
        // The method will later be implemented to save the data to the database
    }
}
