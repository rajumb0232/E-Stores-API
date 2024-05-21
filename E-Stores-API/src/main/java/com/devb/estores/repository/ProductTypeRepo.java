package com.devb.estores.repository;

import com.devb.estores.enums.SubCategory;
import com.devb.estores.enums.TopCategory;
import com.devb.estores.model.ProductType;
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
