package com.self.flipcart.repository;

import com.self.flipcart.enums.SubCategory;
import com.self.flipcart.enums.TopCategory;
import com.self.flipcart.model.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductTypeRepo extends JpaRepository<ProductType, String> {
    boolean existsByTypeName(String productType);

    boolean existsByTypeNameAndSubCategoryAndTopCategory(String productType, SubCategory subCategoryEnum, TopCategory topCategoryEnum);

    Optional<ProductType> findByTypeNameAndSubCategoryAndTopCategory(String productType, SubCategory subCategory, TopCategory topCategory);

    interface TypeNameProjection {
        String getTypeName();
    }

    List<TypeNameProjection> findTypeNameBySubCategory(SubCategory subCategory);
}
