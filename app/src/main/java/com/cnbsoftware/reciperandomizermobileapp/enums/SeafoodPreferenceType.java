package com.cnbsoftware.reciperandomizermobileapp.enums;

import java.util.HashMap;
import java.util.Map;

public enum SeafoodPreferenceType {
    Cod(1),
    Haddock(2),
    Mussels(3),
    Prawns(4),
    Dorado(5);

    private int value;
    private static Map map = new HashMap();

    private SeafoodPreferenceType(int value) {
        this.value = value;
    }

    static {
        for (SeafoodPreferenceType seafoodType : SeafoodPreferenceType.values()) {
            map.put(seafoodType.value, seafoodType);
        }
    }

    public static SeafoodPreferenceType valueOf(int seafoodType) {
        return (SeafoodPreferenceType) map.get(seafoodType);
    }

    public int getValue() {
        return value;
    }
}
