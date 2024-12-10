package com.cnbsoftware.reciperandomizermobileapp.enums;

import java.util.HashMap;
import java.util.Map;

public enum RecipePreferenceType {
    Meat(1),
    Vegetarian(2),
    Pescatarian(3);

    private int value;
    private static Map map = new HashMap();

    private RecipePreferenceType(int value) {
        this.value = value;
    }

    static {
        for (RecipePreferenceType recipePreferenceType : RecipePreferenceType.values()) {
            map.put(recipePreferenceType.value, recipePreferenceType);
        }
    }

    public static RecipePreferenceType valueOf(int dietType) {
        return (RecipePreferenceType) map.get(dietType);
    }

    public int getValue() {
        return value;
    }
}
