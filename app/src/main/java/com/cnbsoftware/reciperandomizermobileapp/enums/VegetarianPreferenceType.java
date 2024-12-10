package com.cnbsoftware.reciperandomizermobileapp.enums;

import java.util.HashMap;
import java.util.Map;

public enum VegetarianPreferenceType {
    Salad(1),
    Soup(2),
    Curry(3);

    private int value;
    private static Map map = new HashMap();

    private VegetarianPreferenceType(int value) {
        this.value = value;
    }

    static {
        for (VegetarianPreferenceType vegetarianType : VegetarianPreferenceType.values()) {
            map.put(vegetarianType.value, vegetarianType);
        }
    }

    public static VegetarianPreferenceType valueOf(int vegetarianType) {
        return (VegetarianPreferenceType) map.get(vegetarianType);
    }

    public int getValue() {
        return value;
    }
}
