package com.devb.estores.controller;

import com.devb.estores.dto.CategoryDTO;
import com.devb.estores.dto.TopCategoryDTO;
import com.devb.estores.enums.State;
import com.devb.estores.enums.SubCategory;
import com.devb.estores.enums.TopCategory;
import com.devb.estores.repository.ProductTypeRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("${app.base_url}")
@AllArgsConstructor
public class OptionsController {

    private ProductTypeRepo typeRepo;

    @GetMapping("/top-categories")
    public ResponseEntity<List<String>> getPrimeCategories() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(Duration.ofDays(1)))
                .body(Arrays.stream(TopCategory.values()).map(TopCategory::getName).toList());
    }

    @GetMapping("/top-categories/{topCategory}/sub-categories")
    public ResponseEntity<List<String>> getSubCategories(@PathVariable String topCategory) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(Duration.ofDays(1)))
                .body(TopCategory.valueOf(topCategory.toUpperCase()).getSubCategories()
                        .stream().map(SubCategory::getName).toList());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<TopCategoryDTO>> getCategories() {
        List<TopCategoryDTO> response = Arrays.stream(TopCategory.values()).map(category -> TopCategoryDTO.builder()
                .displayName(category.getName())
                .topCategoryName(category.name())
                .topCategoryImage("/categories/" + category.name().toLowerCase() + "/images")
                .categories(category.getSubCategories().stream()
                        .map(subCategory -> CategoryDTO.builder()
                                .displayName(subCategory.getName())
                                .categoryName(subCategory.name())
                                .productTypes(typeRepo.findTypeNameBySubCategory(subCategory)
                                        .stream().map(ProductTypeRepo.TypeNameProjection::getTypeName)
                                        .toList()).build()
                        ).toList()).build()
        ).toList();

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(Duration.ofDays(15)))
                .body(response);
    }

    @GetMapping("/states")
    public ResponseEntity<List<String>> getStates() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(Duration.ofDays(1)))
                .body(Arrays.stream(State.values())
                        .map(state -> state.name().toLowerCase())
                        .toList());
    }

    @GetMapping("/states/{stateName}/districts")
    public ResponseEntity<List<String>> getDistrictsByState(@PathVariable String stateName) {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(Duration.ofDays(1)))
                .body(State.valueOf(stateName).getDistricts());
    }
}
