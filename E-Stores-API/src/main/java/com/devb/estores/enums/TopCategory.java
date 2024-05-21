package com.devb.estores.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum TopCategory {
    ELECTRONICS("Electronics", Arrays.asList(
            SubCategory.HOME_APPLIANCES,
            SubCategory.TV,
            SubCategory.GROOMING,
            SubCategory.AUDIO,
            SubCategory.LAPTOP,
            SubCategory.MOBILE,
            SubCategory.CAMERA_AND_ACCESSORIES,
            SubCategory.TV,
            SubCategory.TABLET,
            SubCategory.SMART_HOME,
            SubCategory.POWERBANK
    )),
    FASHION("Fashion", Arrays.asList(
            SubCategory.MENS_TOP_WEAR,
            SubCategory.MENS_BOTTOM_WEAR,
            SubCategory.WOMEN_ETHNIC,
            SubCategory.WOMEN_WESTERN,
            SubCategory.MENS_ETHNIC,
            SubCategory.MENS_FOOTWEAR,
            SubCategory.WOMEN_FOOTWEAR,
            SubCategory.KIDS,
            SubCategory.WINTER,
            SubCategory.BAGS_AND_LUGGAGE
    )),
    HOME("Home", Arrays.asList(
            SubCategory.FURNITURES,
            SubCategory.DECORS,
            SubCategory.KITCHEN,
            SubCategory.CLEANING,
            SubCategory.TOOLS_AND_UTILITY
    )),
    BEAUTY("Beauty", Arrays.asList(
            SubCategory.PERSONAL_CARE,
            SubCategory.MENS_GROOMING,
            SubCategory.NUTRITION,
            SubCategory.BABY_CARE,
            SubCategory.SAFETY_AND_HYGIENE_ESSENTIALS
    )),
    BOOKS("Books", new ArrayList<>()),
    SPORTS("Sports", new ArrayList<>());


    private final String name;
    private final List<SubCategory> subCategories;

    TopCategory(String name, List<SubCategory> subCategories) {
        this.name = name;
        this.subCategories = subCategories;
    }

    public String getName() {
        return name;
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }
}
