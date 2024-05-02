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

        /* validating if the SubCategory given is one of them under the TopCategory
         * If not one of them, throw an exception*/
        if (!topCategoryEnum.getSubCategories().contains(subCategoryEnum))
            throw new InvalidSubCategoryException("Failed to add product type");

        /* collects the generated list of ProductTypes after ensuring if the similar ProductType doesn't already exist
         * in the database, and if the product type name contains only alphabetical characters with allowed special
         * character '-'*/
        List<ProductType> types = Stream.of(productTypes)
                .filter(productType -> !typeRepo.existsByTypeNameAndSubCategoryAndTopCategory(productType.toLowerCase(), subCategoryEnum, topCategoryEnum))
                .map(productType -> {
                    if (!Pattern.matches("^[A-Za-z-]*$", productType))
                        throw new InvalidProductTypeNameException("Failed to add product type");
                    return ProductType.builder()
                            .typeName(productType.toLowerCase())
                            .subCategory(subCategoryEnum)
                            .topCategory(topCategoryEnum)
                            .build();
                }).collect(Collectors.toList());

        types = typeRepo.saveAll(types);
        return ResponseEntity.ok(new ResponseStructure<List<ProductType>>().setStatus(HttpStatus.OK.value())
                .setMessage("Product types added")
                .setData(types));
    }

    /**
     * The method validates if the given SubCategory is valid or not
     *
     * @throws InvalidSubCategoryException if the given string doesn't match to any of the SubCategory defined
     */
    private SubCategory validateAndGetSubCategory(String subCategory) {
        try {
            System.out.println(subCategory);
            return SubCategory.valueOf(subCategory.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidSubCategoryException("failed to add product type");
        }
    }

    /**
     * The method validates if the given TopCategory is valid or not
     *
     * @throws InvalidTopCategoryException if the given string doesn't match to any of the TopCategory defined
     */
    private TopCategory validateAndGetTopCategory(String topCategory) {
        try {
            System.out.println(topCategory);
            return TopCategory.valueOf(topCategory.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidTopCategoryException("failed to add product type");
        }
    }
}


