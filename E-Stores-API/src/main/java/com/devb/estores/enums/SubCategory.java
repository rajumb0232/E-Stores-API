package com.devb.estores.enums;

public enum SubCategory {

    // Electronics ------------------------------------------------------------------------------------------------
    AUDIO("Audio"),
    TV("TV"),
    HOME_APPLIANCES("Home Appliances"),
    GROOMING("Grooming"),
    MOBILE("Mobile"),
    LAPTOP("Laptop"),
    TABLET("Tablet"),
    CAMERA_AND_ACCESSORIES("Camera & Accessories"),
    SMART_HOME("Smart Home"),
    POWERBANK("Pawerbank"),

    // Fashion -----------------------------------------------------------------------------------------------------
    MENS_TOP_WEAR("Men's Top Wear"),
    MENS_BOTTOM_WEAR("Men's Bottom Wear"),
    WOMEN_ETHNIC("Women Ethnic"),
    WOMEN_WESTERN("Women Western"),
    MENS_ETHNIC("Men Ethnic"),
    MENS_FOOTWEAR("Mens Footwear"),
    WOMEN_FOOTWEAR("Women Footwear"),
    KIDS("Kids"),
    WINTER("Winter"),
    BAGS_AND_LUGGAGE("Bags & Luggage"),

    // Home --------------------------------------------------------------------------------------------------------
    FURNITURES("Furniture"),
    DECORS("Decors"),
    KITCHEN("Kitchen"),
    CLEANING("Cleaning"),
    ELECTRICAL("Electrical"),
    TOOLS_AND_UTILITY("Tools & Utility"),

    // Beauty ------------------------------------------------------------------------------------------------------
    PERSONAL_CARE("Personal Care"),
    MENS_GROOMING("Men's Grooming"),
    NUTRITION("Nutrition"),
    BABY_CARE("Baby Care"),
    SAFETY_AND_HYGIENE_ESSENTIALS("Safety & Hygiene Essentials");

    // -------------------------------------------------------------------------------------------------------------
    private String name;
    SubCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
