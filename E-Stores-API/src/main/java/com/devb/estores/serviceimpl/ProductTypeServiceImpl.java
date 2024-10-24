package com.devb.estores.serviceimpl;

import com.devb.estores.enums.SubCategory;
import com.devb.estores.enums.TopCategory;
import com.devb.estores.exceptions.InvalidProductTypeNameException;
import com.devb.estores.exceptions.InvalidSubCategoryException;
import com.devb.estores.exceptions.InvalidTopCategoryException;
import com.devb.estores.model.ProductType;
import com.devb.estores.repository.ProductTypeRepo;
import com.devb.estores.service.ProductTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class ProductTypeServiceImpl implements ProductTypeService {

    private final ProductTypeRepo typeRepo;

    @Override
    public List<ProductType> addProductTypes(String topCategory, String subCategory, String[] productTypes) {
        TopCategory topCategoryEnum = validateAndGetTopCategory(topCategory);
        SubCategory subCategoryEnum = validateAndGetSubCategory(subCategory);

        /*
         validating if the SubCategory given is one of them under the TopCategory
         If not one of them, throw an exception
         */
        if (!topCategoryEnum.getSubCategories().contains(subCategoryEnum))
            throw new InvalidSubCategoryException("Failed to add product type");

        /*
         collects the generated list of ProductTypes after ensuring if the similar ProductType doesn't already exist
         in the database, and if the product type name contains only alphabetical characters with allowed special
         character '-'
         */
        List<ProductType> types = Stream.of(productTypes)
                .filter(productType -> !typeRepo.existsByTypeNameAndSubCategoryAndTopCategory(productType.toLowerCase(), subCategoryEnum, topCategoryEnum))
                .map(typeName -> this.validateAndSaveProductType(typeName, subCategoryEnum, topCategoryEnum)).toList();

        return typeRepo.saveAll(types);
    }

    /**
     * @throws InvalidProductTypeNameException if the given tagName doesn't match the pattern
     *                                         "^[A-Za-z_]$",
     *                                         <p>
     *                                         else saves the data to the database
     */
    private ProductType validateAndSaveProductType(String typeName, SubCategory subCategory, TopCategory topCategory) {
        if (!Pattern.matches("^[A-Za-z_]*$", typeName))
            throw new InvalidProductTypeNameException("Failed to add product type");

        return typeRepo.save(ProductType.builder()
                .typeName(typeName.toLowerCase())
                .subCategory(subCategory)
                .topCategory(topCategory)
                .build());
    }

    /**
     * The method validates if the given SubCategory is valid or not
     *
     * @throws InvalidSubCategoryException if the given string doesn't match to any of the SubCategory defined
     */
    private SubCategory validateAndGetSubCategory(String subCategory) {
        try {
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
            return TopCategory.valueOf(topCategory.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidTopCategoryException("failed to add product type");
        }
    }
}


