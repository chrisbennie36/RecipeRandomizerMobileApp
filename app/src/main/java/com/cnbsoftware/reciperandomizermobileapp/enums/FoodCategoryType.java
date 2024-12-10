package com.cnbsoftware.reciperandomizermobileapp.enums;

import java.util.HashMap;
import java.util.Map;

public enum FoodCategoryType {
    General(0),
    Meat(1),
    Vegetarian(2),
    Seafood(3),
    Vegan (4);

    private int value;
    private static Map map = new HashMap();

    private FoodCategoryType(int value) {
        this.value = value;
    }

    static {
        for (FoodCategoryType foodType : FoodCategoryType.values()) {
            map.put(foodType.value, foodType);
        }
    }

    public static FoodCategoryType valueOf(int foodType) {
        return (FoodCategoryType) map.get(foodType);
    }

    public int getValue() {
        return value;
    }
}
