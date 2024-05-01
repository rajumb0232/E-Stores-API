package com.self.flipcart.serviceimpl;

import com.self.flipcart.enums.SubCategory;
import com.self.flipcart.enums.TopCategory;
import com.self.flipcart.exceptions.InvalidProductTypeNameException;
import com.self.flipcart.exceptions.InvalidSubCategoryException;
import com.self.flipcart.exceptions.InvalidTopCategoryException;
import com.self.flipcart.model.ProductType;
import com.self.flipcart.repository.ProductTypeRepo;
import com.self.flipcart.service.ProductTypeService;
import com.self.flipcart.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class ProductTypeServiceImpl implements ProductTypeService {

    private final ProductTypeRepo typeRepo;

    @Override
    public ResponseEntity<ResponseStructure<List<ProductType>>> addProductTypes(String topCategory, String subCategory, String[] productTypes) {
        TopCategory topCategoryEnum = validateAndGetTopCategory(topCategory);
        SubCategory subCategoryEnum = validateAndGetSubCategory(subCategory);
        if (!topCategoryEnum.getSubCategories().contains(subCategoryEnum))
            throw new InvalidSubCategoryException("Failed to add product type");

        List<ProductType> types = Stream.of(productTypes)
                .filter(productType -> !typeRepo.existsByTypeNameAndSubCategoryAndTopCategory(productType, subCategoryEnum, topCategoryEnum))
                .map(productType -> {
                    if (!Pattern.matches("^[A-Za-z-]*$", productType))
                        throw new InvalidProductTypeNameException("Failed to add product type");
                    return ProductType.builder()
                            .typeName(productType)
                            .subCategory(subCategoryEnum)
                            .topCategory(topCategoryEnum)
                            .build();
                }).collect(Collectors.toList());

        types = typeRepo.saveAll(types);
        return ResponseEntity.ok(new ResponseStructure<List<ProductType>>().setStatus(HttpStatus.OK.value())
                .setMessage("Product types added")
                .setData(types));
    }

    private SubCategory validateAndGetSubCategory(String subCategory) {
        try {
            System.out.println(subCategory);
            return SubCategory.valueOf(subCategory.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidSubCategoryException("failed to add product type");
        }
    }

    private TopCategory validateAndGetTopCategory(String topCategory) {
        try {
            System.out.println(topCategory);
            return TopCategory.valueOf(topCategory.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidTopCategoryException("failed to add product type");
        }
    }
}


