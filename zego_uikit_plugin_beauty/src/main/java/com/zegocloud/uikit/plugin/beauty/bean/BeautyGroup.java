package com.zegocloud.uikit.plugin.beauty.bean;

public enum BeautyGroup {
    BASIC, /////
    ADVANCED, /////
    FILTERS, ////
    MAKEUPS,////
    STYLE_MAKEUP, ////
    STICKERS, ////
    BACKGROUND;

    public static BeautyGroup getByName(String name) {
        BeautyGroup group = null;
        for (BeautyGroup value : BeautyGroup.values()) {
            if (value.name().equals(name)) {
                group = value;
            }
        }
        return group;
    }
}
