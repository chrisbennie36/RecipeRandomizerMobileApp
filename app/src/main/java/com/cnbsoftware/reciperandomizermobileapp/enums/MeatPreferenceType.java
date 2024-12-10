package com.cnbsoftware.reciperandomizermobileapp.enums;

import java.util.HashMap;
import java.util.Map;

public enum MeatPreferenceType {
    Beef(1),
    Pork(2),
    Chicken(3);

    private int value;
    private static Map map = new HashMap();

    private MeatPreferenceType(int value) {
        this.value = value;
    }

    static {
        for (MeatPreferenceType meatType : MeatPreferenceType.values()) {
            map.put(meatType.value, meatType);
        }
    }

    public static MeatPreferenceType valueOf(int meatType) {
        return (MeatPreferenceType) map.get(meatType);
    }

    public int getValue() {
        return value;
    }
}
