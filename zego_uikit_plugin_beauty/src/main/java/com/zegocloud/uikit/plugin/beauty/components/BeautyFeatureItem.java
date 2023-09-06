package com.zegocloud.uikit.plugin.beauty.components;

import android.graphics.drawable.Drawable;
import com.zegocloud.uikit.plugin.beauty.ZegoUIKitBeautyPlugin;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyFeature;
import com.zegocloud.uikit.plugin.adapter.plugins.beauty.ZegoBeautyPluginEffectsType;

public class BeautyFeatureItem {

    public String name;
    public Drawable drawable;
    public BeautyFeature beautyFeature;

    public BeautyFeatureItem(String name, Drawable drawable, ZegoBeautyPluginEffectsType beautyType) {
        this.name = name;
        this.drawable = drawable;
        this.beautyFeature = ZegoUIKitBeautyPlugin.getInstance().getBeautyFeature(beautyType);
    }

    public BeautyFeatureItem(String name, Drawable drawable, BeautyFeature beautyFeature) {
        this.name = name;
        this.drawable = drawable;
        this.beautyFeature = beautyFeature;
    }
}
