package com.zegocloud.uikit.plugin.beauty.components;

import com.zegocloud.uikit.plugin.beauty.bean.BeautyGroup;
import java.util.List;

public class BeautyGroupItem {

    public BeautyGroup beautyGroup;
    public String groupName;
    public List<BeautyFeatureItem> beautyFeatureItems;

    public BeautyGroupItem(BeautyGroup beautyGroup, String groupName, List<BeautyFeatureItem> beautyFeatureItems) {
        this.beautyGroup = beautyGroup;
        this.groupName = groupName;
        this.beautyFeatureItems = beautyFeatureItems;
    }
}
