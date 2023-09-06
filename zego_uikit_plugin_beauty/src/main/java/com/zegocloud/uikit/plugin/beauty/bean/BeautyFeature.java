package com.zegocloud.uikit.plugin.beauty.bean;

import com.zegocloud.uikit.plugin.adapter.plugins.beauty.ZegoBeautyPluginEffectsType;
import java.util.List;

/**
 * beauty:  1. enable function  2.set param filter:  1. setFilter path 2. set param
 */
public class BeautyFeature {

    private int maxValue;
    private int minValue;
    private int defaultValue;

    private ZegoBeautyPluginEffectsType beautyType;
    private BeautyEditor editor;

    private BeautyGroup parentGroup;

    // only for make up items
    private List<ZegoBeautyPluginEffectsType> subTypes;

    // only for make up subtype items
    private ZegoBeautyPluginEffectsType parentType;

    // only for make up items
    public BeautyFeature(ZegoBeautyPluginEffectsType beautyTypes, List<ZegoBeautyPluginEffectsType> subTypes, BeautyGroup parentGroup,
        BeautyEditor editor) {
        this.parentGroup = parentGroup;
        this.maxValue = 100;
        this.minValue = 0;
        this.defaultValue = 50;
        this.editor = editor;
        this.beautyType = beautyTypes;
        this.subTypes = subTypes;
    }

    // for normal items
    public BeautyFeature(ZegoBeautyPluginEffectsType beautyTypes, BeautyGroup parentGroup, int minValue, int maxValue, int defaultValue,
        BeautyEditor editor) {
        this.parentGroup = parentGroup;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.defaultValue = defaultValue;
        this.editor = editor;
        this.beautyType = beautyTypes;
    }

    // for normal items
    public BeautyFeature(ZegoBeautyPluginEffectsType beautyTypes, BeautyGroup parentGroup, BeautyEditor editor) {
        this.parentGroup = parentGroup;
        this.maxValue = 100;
        this.minValue = 0;
        this.defaultValue = 50;
        this.editor = editor;
        this.beautyType = beautyTypes;
    }

    // only for make up subtype items
    public BeautyFeature(ZegoBeautyPluginEffectsType beautyTypes, ZegoBeautyPluginEffectsType parentType, BeautyGroup parentGroup, BeautyEditor editor) {
        this.maxValue = 100;
        this.minValue = 0;
        this.defaultValue = 50;
        this.editor = editor;
        this.beautyType = beautyTypes;
        this.parentType = parentType;
        this.parentGroup = parentGroup;
    }

    /**
     * for normal none ,reset
     *
     * @param parentGroup
     */
    public BeautyFeature(BeautyGroup parentGroup, BeautyEditor editor) {
        this.parentGroup = parentGroup;
        this.maxValue = 0;
        this.minValue = 0;
        this.defaultValue = 0;
        this.editor = editor;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public int getDefaultValue() {
        return defaultValue;
    }

    public BeautyGroup getParentGroup() {
        return parentGroup;
    }

    public ZegoBeautyPluginEffectsType getBeautyType() {
        return beautyType;
    }

    public List<ZegoBeautyPluginEffectsType> getSubTypes() {
        return subTypes;
    }

    public BeautyEditor getEditor() {
        return editor;
    }

    public ZegoBeautyPluginEffectsType getParentType() {
        return parentType;
    }

    @Override
    public String toString() {
        return "BeautyAbility{" + "maxValue=" + maxValue + ", minValue=" + minValue + ", defaultValue=" + defaultValue
            + ", beautyTypes=" + beautyType + ", beautyGroup=" + parentGroup + '}';
    }
}
