package com.zegocloud.uikit.plugin.beauty.components;

import android.content.Context;
import androidx.arch.core.util.Function;
import androidx.core.content.ContextCompat;
import com.zegocloud.uikit.plugin.adapter.plugins.beauty.ZegoBeautyPluginConfig;
import com.zegocloud.uikit.plugin.adapter.plugins.beauty.ZegoBeautyPluginEffectsType;
import com.zegocloud.uikit.plugin.beauty.R;
import com.zegocloud.uikit.plugin.beauty.ZegoUIKitBeautyPlugin;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.BlusherMakeupEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.ColoredContactsEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.EyelashesEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.EyelinerEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.EyeshadowEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.FilterEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.LipstickEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.SimpleEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.StickerEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.StyleMakeupEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyFeature;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyGroup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeautyDialogHelper {

    public static List<BeautyGroupItem> getBeautyGroupItems(Context context,
        List<BeautyFeatureItem> beautyFeatureItems) {
        List<BeautyGroupItem> beautyGroupItems = new ArrayList<>();

        Map<BeautyGroup, List<BeautyFeatureItem>> groupMap = new HashMap<>();
        for (BeautyGroup group : BeautyGroup.values()) {
            groupMap.put(group, new ArrayList<>());
        }
        for (BeautyFeatureItem beautyFeatureItem : beautyFeatureItems) {
            List<BeautyFeatureItem> featureItems = groupMap.get(beautyFeatureItem.beautyFeature.getParentGroup());
            featureItems.add(beautyFeatureItem);
        }
        if (!groupMap.get(BeautyGroup.BASIC).isEmpty()) {
            beautyGroupItems.add(new BeautyGroupItem(BeautyGroup.BASIC,
                getStringOrDefault(context, R.string.beauty_group_basic, config -> config.innerText.titleBeautyBasic),
                groupMap.get(BeautyGroup.BASIC)));
        }
        if (!groupMap.get(BeautyGroup.ADVANCED).isEmpty()) {
            beautyGroupItems.add(new BeautyGroupItem(BeautyGroup.ADVANCED,
                getStringOrDefault(context, R.string.beauty_group_advanced,
                    config -> config.innerText.titleBeautyAdvanced), groupMap.get(BeautyGroup.ADVANCED)));
        }
        if (!groupMap.get(BeautyGroup.MAKEUPS).isEmpty()) {
            beautyGroupItems.add(new BeautyGroupItem(BeautyGroup.MAKEUPS,
                getStringOrDefault(context, R.string.beauty_group_makeups,
                    config -> config.innerText.titleBeautyMakeup), groupMap.get(BeautyGroup.MAKEUPS)));
        }
        if (!groupMap.get(BeautyGroup.STYLE_MAKEUP).isEmpty()) {
            beautyGroupItems.add(new BeautyGroupItem(BeautyGroup.STYLE_MAKEUP,
                getStringOrDefault(context, R.string.beauty_group_style_makeup,
                    config -> config.innerText.titleBeautyStyleMakeup), groupMap.get(BeautyGroup.STYLE_MAKEUP)));
        }
        if (!groupMap.get(BeautyGroup.FILTERS).isEmpty()) {
            beautyGroupItems.add(new BeautyGroupItem(BeautyGroup.FILTERS,
                getStringOrDefault(context, R.string.beauty_group_filter, config -> config.innerText.titleFilter),
                groupMap.get(BeautyGroup.FILTERS)));
        }
        if (!groupMap.get(BeautyGroup.STICKERS).isEmpty()) {
            beautyGroupItems.add(new BeautyGroupItem(BeautyGroup.STICKERS,
                getStringOrDefault(context, R.string.beauty_group_sticker, config -> config.innerText.titleStickers),
                groupMap.get(BeautyGroup.STICKERS)));
        }
        if (!groupMap.get(BeautyGroup.BACKGROUND).isEmpty()) {
            beautyGroupItems.add(new BeautyGroupItem(BeautyGroup.BACKGROUND,
                getStringOrDefault(context, R.string.beauty_group_background,
                    config -> config.innerText.titleBackground), groupMap.get(BeautyGroup.BACKGROUND)));
        }
        return beautyGroupItems;
    }

    public static List<BeautyFeatureItem> getBeautyFeatureItems(Context context,
        List<ZegoBeautyPluginEffectsType> effectsTypes) {
        List<BeautyFeatureItem> allBeautyFeatureItems = new ArrayList<>();
        allBeautyFeatureItems.addAll(getBasicItems(context, effectsTypes));
        allBeautyFeatureItems.addAll(getAdvancedItems(context, effectsTypes));
        allBeautyFeatureItems.addAll(getMakeupItems(context, effectsTypes));
        allBeautyFeatureItems.addAll(getMakeupLipstickItems(context, effectsTypes));
        allBeautyFeatureItems.addAll(getMakeupBlusherItems(context, effectsTypes));
        allBeautyFeatureItems.addAll(getMakeupEyelashesItems(context, effectsTypes));
        allBeautyFeatureItems.addAll(getMakeupEyelinerItems(context, effectsTypes));
        allBeautyFeatureItems.addAll(getMakeupEyeShadowItems(context, effectsTypes));
        allBeautyFeatureItems.addAll(getMakeupColoredContactsItems(context, effectsTypes));
        allBeautyFeatureItems.addAll(getMakeupStyleItems(context, effectsTypes));
        allBeautyFeatureItems.addAll(getStickerItems(context, effectsTypes));
        allBeautyFeatureItems.addAll(getFilterItems(context, effectsTypes));
        allBeautyFeatureItems.addAll(getBackgroundItems(context, effectsTypes));
        return allBeautyFeatureItems;
    }

    // add reset or none for every ability or group
    public static List<BeautyFeatureItem> getBasicItems(Context context, List<ZegoBeautyPluginEffectsType> effectsTypes) {
        Map<ZegoBeautyPluginEffectsType, BeautyFeatureItem> beautyFeatureItemMap = new HashMap<>();
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.BASIC_SMOOTHING, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_basic_smoothing,
                config -> config.innerText.beautyBasicSmoothing),
            ContextCompat.getDrawable(context, R.drawable.beauty_basic_smoothing),
            ZegoBeautyPluginEffectsType.BASIC_SMOOTHING));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.BASIC_SKIN_TONE, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_basic_skin_tone,
                config -> config.innerText.beautyBasicSkinTone),
            ContextCompat.getDrawable(context, R.drawable.beauty_basic_skin_tone),
            ZegoBeautyPluginEffectsType.BASIC_SKIN_TONE));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.BASIC_BLUSHER, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_basic_blusher, config -> config.innerText.beautyBasicBlusher),
            ContextCompat.getDrawable(context, R.drawable.beauty_basic_blusher),
            ZegoBeautyPluginEffectsType.BASIC_BLUSHER));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.BASIC_SHARPENING, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_basic_sharpening,
                config -> config.innerText.beautyBasicSharpening),
            ContextCompat.getDrawable(context, R.drawable.beauty_basic_sharpening),
            ZegoBeautyPluginEffectsType.BASIC_SHARPENING));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.BASIC_WRINKLES, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_basic_wrinkles, config -> config.innerText.beautyBasicWrinkles),
            ContextCompat.getDrawable(context, R.drawable.beauty_basic_wrinkles),
            ZegoBeautyPluginEffectsType.BASIC_WRINKLES));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.BASIC_DARK_CIRCLES, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_basic_dark_circles,
                config -> config.innerText.beautyBasicDarkCircles),
            ContextCompat.getDrawable(context, R.drawable.beauty_basic_dark_circles),
            ZegoBeautyPluginEffectsType.BASIC_DARK_CIRCLES));

        List<BeautyFeatureItem> beautyFeatureItemList = new ArrayList<>();
        for (ZegoBeautyPluginEffectsType basicType : effectsTypes) {
            BeautyFeatureItem beautyFeatureItem = beautyFeatureItemMap.get(basicType);
            if (beautyFeatureItem != null) {
                beautyFeatureItemList.add(beautyFeatureItem);
            }
        }
        if (!beautyFeatureItemList.isEmpty()) {
            beautyFeatureItemList.add(0, new BeautyFeatureItem(
                getStringOrDefault(context, R.string.beauty_reset, config -> config.innerText.beautyReset),
                ContextCompat.getDrawable(context, R.drawable.beauty_reset_round),
                new BeautyFeature(BeautyGroup.BASIC, new SimpleEditor())));
        }
        return beautyFeatureItemList;
    }

    public static List<BeautyFeatureItem> getMakeupItems(Context context,
        List<ZegoBeautyPluginEffectsType> effectsTypes) {
        Map<ZegoBeautyPluginEffectsType, BeautyFeatureItem> beautyFeatureItemMap = new HashMap<>();
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_LIPSTICK, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_group_makeup_lipstick,
                config -> config.innerText.beautyMakeupLipstick),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_lipstick),
            ZegoBeautyPluginEffectsType.MAKEUP_LIPSTICK));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_BLUSHER, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_group_makeup_blusher,
                config -> config.innerText.beautyMakeupBlusher),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_blusher),
            ZegoBeautyPluginEffectsType.MAKEUP_BLUSHER));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_EYELASHES, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_group_makeup_eyelashes,
                config -> config.innerText.beautyMakeupEyelashe),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyelash),
            ZegoBeautyPluginEffectsType.MAKEUP_EYELASHES));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_EYELINER, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_group_makeup_eyeliner,
                config -> config.innerText.beautyMakeupEyeliner),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyeliner),
            ZegoBeautyPluginEffectsType.MAKEUP_EYELINER));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_EYESHADOW, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_group_makeup_eyeshadow,
                config -> config.innerText.beautyMakeupEyeshadow),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyeshadow),
            ZegoBeautyPluginEffectsType.MAKEUP_EYESHADOW));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_COLORED_CONTACTS, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_group_makeup_colored_contacts,
                config -> config.innerText.beautyMakeupColoredContacts),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_colored_contacts),
            ZegoBeautyPluginEffectsType.MAKEUP_COLORED_CONTACTS));

        List<BeautyFeatureItem> beautyFeatureItemList = new ArrayList<>();
        for (ZegoBeautyPluginEffectsType effectsType : effectsTypes) {
            BeautyFeatureItem beautyFeatureItem = beautyFeatureItemMap.get(effectsType);
            if (beautyFeatureItem != null) {
                beautyFeatureItemList.add(beautyFeatureItem);
            }
        }
        if (!beautyFeatureItemList.isEmpty()) {
            beautyFeatureItemList.add(0, new BeautyFeatureItem(
                getStringOrDefault(context, R.string.beauty_reset, config -> config.innerText.beautyReset),
                ContextCompat.getDrawable(context, R.drawable.beauty_reset_round),
                new BeautyFeature(BeautyGroup.MAKEUPS, new SimpleEditor())));
        }
        return beautyFeatureItemList;
    }

    public static List<BeautyFeatureItem> getAdvancedItems(Context context,
        List<ZegoBeautyPluginEffectsType> effectsTypes) {
        Map<ZegoBeautyPluginEffectsType, BeautyFeatureItem> beautyFeatureItemMap = new HashMap<>();
        ZegoBeautyPluginConfig beautyPluginConfig = ZegoUIKitBeautyPlugin.getInstance().getBeautyPluginConfig();

        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.ADVANCED_FACE_SLIMMING, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_advanced_face_slimming,
                config -> config.innerText.beautyAdvancedFaceSlimming),
            ContextCompat.getDrawable(context, R.drawable.beauty_advanced_face_slimming),
            ZegoBeautyPluginEffectsType.ADVANCED_FACE_SLIMMING));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.ADVANCED_EYES_ENLARGING, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_advanced_eye_enlarging,
                config -> config.innerText.beautyAdvancedEyesEnlarging),
            ContextCompat.getDrawable(context, R.drawable.beauty_advanced_eyes_enlarging),
            ZegoBeautyPluginEffectsType.ADVANCED_EYES_ENLARGING));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.ADVANCED_EYES_BRIGHTENING, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_advanced_eye_brightening,
                config -> config.innerText.beautyAdvancedEyesBrightening),
            ContextCompat.getDrawable(context, R.drawable.beauty_advanced_eyes_brightening),
            ZegoBeautyPluginEffectsType.ADVANCED_EYES_BRIGHTENING));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.ADVANCED_CHIN_LENGTHENING, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_advanced_chin_lengthening,
                config -> config.innerText.beautyAdvancedChinLengthening),
            ContextCompat.getDrawable(context, R.drawable.beauty_advanced_chin_lengthening),
            ZegoBeautyPluginEffectsType.ADVANCED_CHIN_LENGTHENING));
        String reshape = context.getString(R.string.beauty_advanced_mouth_reshape);
        if (beautyPluginConfig != null && beautyPluginConfig.innerText != null
            && beautyPluginConfig.innerText.beautyAdvancedMouthReshape != null) {
            reshape = beautyPluginConfig.innerText.beautyAdvancedMouthReshape;
        }
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.ADVANCED_MOUTH_RESHAPE,
            new BeautyFeatureItem(reshape, ContextCompat.getDrawable(context, R.drawable.beauty_advanced_mouth_reshape),
                ZegoBeautyPluginEffectsType.ADVANCED_MOUTH_RESHAPE));
        String whitening = context.getString(R.string.beauty_advanced_teeth_whitening);
        if (beautyPluginConfig != null && beautyPluginConfig.innerText != null
            && beautyPluginConfig.innerText.beautyAdvancedTeethWhitening != null) {
            whitening = beautyPluginConfig.innerText.beautyAdvancedTeethWhitening;
        }
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.ADVANCED_TEETH_WHITENING, new BeautyFeatureItem(whitening,
            ContextCompat.getDrawable(context, R.drawable.beauty_advanced_teeth_whitening),
            ZegoBeautyPluginEffectsType.ADVANCED_TEETH_WHITENING));

        String noseSlimming = context.getString(R.string.beauty_advanced_nose_slimming);
        if (beautyPluginConfig != null && beautyPluginConfig.innerText != null
            && beautyPluginConfig.innerText.beautyAdvancedNoseSlimming != null) {
            noseSlimming = beautyPluginConfig.innerText.beautyAdvancedNoseSlimming;
        }
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.ADVANCED_NOSE_SLIMMING, new BeautyFeatureItem(noseSlimming,
            ContextCompat.getDrawable(context, R.drawable.beauty_advanced_nose_slimming),
            ZegoBeautyPluginEffectsType.ADVANCED_NOSE_SLIMMING));
        String noseLengthening = context.getString(R.string.beauty_advanced_nose_lengthening);
        if (beautyPluginConfig != null && beautyPluginConfig.innerText != null
            && beautyPluginConfig.innerText.beautyAdvancedNoseLengthening != null) {
            noseLengthening = beautyPluginConfig.innerText.beautyAdvancedNoseLengthening;
        }
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.ADVANCED_NOSE_LENGTHENING,
            new BeautyFeatureItem(noseLengthening,
                ContextCompat.getDrawable(context, R.drawable.beauty_advanced_nose_lengthening),
                ZegoBeautyPluginEffectsType.ADVANCED_NOSE_LENGTHENING));
        String faceShortening = context.getString(R.string.beauty_advanced_face_shortening);
        if (beautyPluginConfig != null && beautyPluginConfig.innerText != null
            && beautyPluginConfig.innerText.beautyAdvancedFaceShortening != null) {
            faceShortening = beautyPluginConfig.innerText.beautyAdvancedFaceShortening;
        }
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.ADVANCED_FACE_SHORTENING,
            new BeautyFeatureItem(faceShortening,
                ContextCompat.getDrawable(context, R.drawable.beauty_advanced_face_shortening),
                ZegoBeautyPluginEffectsType.ADVANCED_FACE_SHORTENING));
        String mandible = context.getString(R.string.beauty_advanced_mandible_slimming);
        if (beautyPluginConfig != null && beautyPluginConfig.innerText != null
            && beautyPluginConfig.innerText.beautyAdvancedMandibleSlimming != null) {
            mandible = beautyPluginConfig.innerText.beautyAdvancedMandibleSlimming;
        }
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.ADVANCED_MANDIBLE_SLIMMING, new BeautyFeatureItem(mandible,
            ContextCompat.getDrawable(context, R.drawable.beauty_advanced_mandible_slimming),
            ZegoBeautyPluginEffectsType.ADVANCED_MANDIBLE_SLIMMING));
        String cheekbone = context.getString(R.string.beauty_advanced_cheekbone_slimming);
        if (beautyPluginConfig != null && beautyPluginConfig.innerText != null
            && beautyPluginConfig.innerText.beautyAdvancedCheekboneSlimming != null) {
            cheekbone = beautyPluginConfig.innerText.beautyAdvancedCheekboneSlimming;
        }
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.ADVANCED_CHEEKBONE_SLIMMING,
            new BeautyFeatureItem(cheekbone,
                ContextCompat.getDrawable(context, R.drawable.beauty_advanced_cheekbone_slimming),
                ZegoBeautyPluginEffectsType.ADVANCED_CHEEKBONE_SLIMMING));
        String forehead = context.getString(R.string.beauty_advanced_forehead_slimming);
        if (beautyPluginConfig != null && beautyPluginConfig.innerText != null
            && beautyPluginConfig.innerText.beautyAdvancedForeheadSlimming != null) {
            forehead = beautyPluginConfig.innerText.beautyAdvancedForeheadSlimming;
        }
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.ADVANCED_FOREHEAD_SLIMMING, new BeautyFeatureItem(forehead,
            ContextCompat.getDrawable(context, R.drawable.beauty_advanced_forehead_slimming),
            ZegoBeautyPluginEffectsType.ADVANCED_FOREHEAD_SLIMMING));

        List<BeautyFeatureItem> beautyFeatureItemList = new ArrayList<>();
        for (ZegoBeautyPluginEffectsType effectsType : effectsTypes) {
            BeautyFeatureItem beautyFeatureItem = beautyFeatureItemMap.get(effectsType);
            if (beautyFeatureItem != null) {
                beautyFeatureItemList.add(beautyFeatureItem);
            }
        }
        if (!beautyFeatureItemList.isEmpty()) {
            beautyFeatureItemList.add(0, new BeautyFeatureItem(
                getStringOrDefault(context, R.string.beauty_reset, config -> config.innerText.beautyReset),
                ContextCompat.getDrawable(context, R.drawable.beauty_reset_round),
                new BeautyFeature(BeautyGroup.ADVANCED, new SimpleEditor())));
        }
        return beautyFeatureItemList;
    }

    // add parent type and parent group for none ,reset ,clear it's BeautyType
    public static List<BeautyFeatureItem> getMakeupLipstickItems(Context context,
        List<ZegoBeautyPluginEffectsType> effectsTypes) {
        Map<ZegoBeautyPluginEffectsType, BeautyFeatureItem> beautyFeatureItemMap = new HashMap<>();
        ZegoBeautyPluginConfig beautyPluginConfig = ZegoUIKitBeautyPlugin.getInstance().getBeautyPluginConfig();
        String cameoPink = context.getString(R.string.beauty_makeup_lipstick_cameo_pink);
        if (beautyPluginConfig != null && beautyPluginConfig.innerText != null
            && beautyPluginConfig.innerText.beautyMakeupLipstickCameoPink != null) {
            cameoPink = beautyPluginConfig.innerText.beautyMakeupLipstickCameoPink;
        }
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_LIPSTICK_CAMEO_PINK,
            new BeautyFeatureItem(cameoPink,
                ContextCompat.getDrawable(context, R.drawable.beauty_makeup_lipstick_cameo_pink),
                ZegoBeautyPluginEffectsType.MAKEUP_LIPSTICK_CAMEO_PINK));
        String sweetOrange = context.getString(R.string.beauty_makeup_lipstick_sweet_orange);
        if (beautyPluginConfig != null && beautyPluginConfig.innerText != null
            && beautyPluginConfig.innerText.beautyMakeupLipstickSweetOrange != null) {
            sweetOrange = beautyPluginConfig.innerText.beautyMakeupLipstickSweetOrange;
        }
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_LIPSTICK_SWEET_ORANGE,
            new BeautyFeatureItem(sweetOrange,
                ContextCompat.getDrawable(context, R.drawable.beauty_makeup_lipstick_sweet_orange),
                ZegoBeautyPluginEffectsType.MAKEUP_LIPSTICK_SWEET_ORANGE));
        String rustRed = context.getString(R.string.beauty_makeup_lipstick_rust_red);
        if (beautyPluginConfig != null && beautyPluginConfig.innerText != null
            && beautyPluginConfig.innerText.beautyMakeupLipstickRustRed != null) {
            rustRed = beautyPluginConfig.innerText.beautyMakeupLipstickRustRed;
        }
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_LIPSTICK_RUST_RED, new BeautyFeatureItem(rustRed,
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_lipstick_rust_red),
            ZegoBeautyPluginEffectsType.MAKEUP_LIPSTICK_RUST_RED));
        String coral = context.getString(R.string.beauty_makeup_lipstick_coral);
        if (beautyPluginConfig != null && beautyPluginConfig.innerText != null
            && beautyPluginConfig.innerText.beautyMakeupLipstickCoral != null) {
            coral = beautyPluginConfig.innerText.beautyMakeupLipstickCoral;
        }
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_LIPSTICK_CORAL,
            new BeautyFeatureItem(coral, ContextCompat.getDrawable(context, R.drawable.beauty_makeup_lipstick_coral),
                ZegoBeautyPluginEffectsType.MAKEUP_LIPSTICK_CORAL));
        String redVelvet = context.getString(R.string.beauty_makeup_lipstick_red_velvet);
        if (beautyPluginConfig != null && beautyPluginConfig.innerText != null
            && beautyPluginConfig.innerText.beautyMakeupLipstickRedVelvet != null) {
            redVelvet = beautyPluginConfig.innerText.beautyMakeupLipstickRedVelvet;
        }
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_LIPSTICK_RED_VELVET,
            new BeautyFeatureItem(redVelvet,
                ContextCompat.getDrawable(context, R.drawable.beauty_makeup_lipstick_red_velvet),
                ZegoBeautyPluginEffectsType.MAKEUP_LIPSTICK_RED_VELVET));

        List<BeautyFeatureItem> beautyFeatureItemList = new ArrayList<>();
        for (ZegoBeautyPluginEffectsType effectsType : effectsTypes) {
            BeautyFeatureItem beautyFeatureItem = beautyFeatureItemMap.get(effectsType);
            if (beautyFeatureItem != null) {
                beautyFeatureItemList.add(beautyFeatureItem);
            }
        }
        if (!beautyFeatureItemList.isEmpty()) {
            beautyFeatureItemList.add(0, new BeautyFeatureItem(
                getStringOrDefault(context, R.string.beauty_none, config -> config.innerText.beautyNone),
                ContextCompat.getDrawable(context, R.drawable.beauty_none_round),
                new BeautyFeature(null, ZegoBeautyPluginEffectsType.MAKEUP_LIPSTICK, BeautyGroup.MAKEUPS,
                    new LipstickEditor())));
        }
        return beautyFeatureItemList;
    }

    public static List<BeautyFeatureItem> getMakeupBlusherItems(Context context,
        List<ZegoBeautyPluginEffectsType> effectsTypes) {
        Map<ZegoBeautyPluginEffectsType, BeautyFeatureItem> beautyFeatureItemMap = new HashMap<>();

        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_BLUSHER_SLIGHTLY_DRUNK, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_makeup_blusher_slightly_drunk,
                config -> config.innerText.beautyMakeupBlusherSlightlyDrunk),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_blusher_slightly_drunk),
            ZegoBeautyPluginEffectsType.MAKEUP_BLUSHER_SLIGHTLY_DRUNK));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_BLUSHER_PEACH, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_makeup_blusher_peach,
                config -> config.innerText.beautyMakeupBlusherPeach),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_blusher_peach),
            ZegoBeautyPluginEffectsType.MAKEUP_BLUSHER_PEACH));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_BLUSHER_MILKY_ORANGE, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_makeup_blusher_milky_orange,
                config -> config.innerText.beautyMakeupBlusherMilkyOrange),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_blusher_milky_orange),
            ZegoBeautyPluginEffectsType.MAKEUP_BLUSHER_MILKY_ORANGE));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_BLUSHER_APRICOT_PINK, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_makeup_blusher_milky_apricot_pink,
                config -> config.innerText.beautyMakeupBlusherApricotPink),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_blusher_apricot_pink),
            ZegoBeautyPluginEffectsType.MAKEUP_BLUSHER_APRICOT_PINK));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_BLUSHER_SWEET_ORANGE, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_makeup_blusher_milky_sweet_orange,
                config -> config.innerText.beautyMakeupBlusherSweetOrange),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_blusher_sweet_orange),
            ZegoBeautyPluginEffectsType.MAKEUP_BLUSHER_SWEET_ORANGE));

        List<BeautyFeatureItem> beautyFeatureItemList = new ArrayList<>();
        for (ZegoBeautyPluginEffectsType effectsType : effectsTypes) {
            BeautyFeatureItem beautyFeatureItem = beautyFeatureItemMap.get(effectsType);
            if (beautyFeatureItem != null) {
                beautyFeatureItemList.add(beautyFeatureItem);
            }
        }
        if (!beautyFeatureItemList.isEmpty()) {
            beautyFeatureItemList.add(0, new BeautyFeatureItem(
                getStringOrDefault(context, R.string.beauty_none, config -> config.innerText.beautyNone),
                ContextCompat.getDrawable(context, R.drawable.beauty_none_round),
                new BeautyFeature(null, ZegoBeautyPluginEffectsType.MAKEUP_BLUSHER, BeautyGroup.MAKEUPS,
                    new BlusherMakeupEditor())));
        }
        return beautyFeatureItemList;
    }

    public static List<BeautyFeatureItem> getMakeupEyelashesItems(Context context,
        List<ZegoBeautyPluginEffectsType> effectsTypes) {
        Map<ZegoBeautyPluginEffectsType, BeautyFeatureItem> beautyFeatureItemMap = new HashMap<>();

        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_EYELASHES_NATURAL, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_makeup_eyelashes_natural,
                config -> config.innerText.beautyMakeupEyelashesNatural),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyelashes_natural),
            ZegoBeautyPluginEffectsType.MAKEUP_EYELASHES_NATURAL));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_EYELASHES_TENDER, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_makeup_eyelashes_tender,
                config -> config.innerText.beautyMakeupEyelashesTender),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyelashes_tender),
            ZegoBeautyPluginEffectsType.MAKEUP_EYELASHES_TENDER));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_EYELASHES_CURL, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_makeup_eyelashes_curl,
                config -> config.innerText.beautyMakeupEyelashesCurl),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyelashes_curl),
            ZegoBeautyPluginEffectsType.MAKEUP_EYELASHES_CURL));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_EYELASHES_EVERLONG, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_makeup_eyelashes_everlong,
                config -> config.innerText.beautyMakeupEyelashesEverlong),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyelashes_everlong),
            ZegoBeautyPluginEffectsType.MAKEUP_EYELASHES_EVERLONG));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_EYELASHES_THICK, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_makeup_eyelashes_thick,
                config -> config.innerText.beautyMakeupEyelashesThick),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyelashes_thick),
            ZegoBeautyPluginEffectsType.MAKEUP_EYELASHES_THICK));

        List<BeautyFeatureItem> beautyFeatureItemList = new ArrayList<>();
        for (ZegoBeautyPluginEffectsType effectsType : effectsTypes) {
            BeautyFeatureItem beautyFeatureItem = beautyFeatureItemMap.get(effectsType);
            if (beautyFeatureItem != null) {
                beautyFeatureItemList.add(beautyFeatureItem);
            }
        }
        if (!beautyFeatureItemList.isEmpty()) {
            beautyFeatureItemList.add(0, new BeautyFeatureItem(
                getStringOrDefault(context, R.string.beauty_none, config -> config.innerText.beautyNone),
                ContextCompat.getDrawable(context, R.drawable.beauty_none_round),
                new BeautyFeature(null, ZegoBeautyPluginEffectsType.MAKEUP_EYELASHES, BeautyGroup.MAKEUPS,
                    new EyelashesEditor())));
        }
        return beautyFeatureItemList;
    }

    public static List<BeautyFeatureItem> getMakeupEyelinerItems(Context context,
        List<ZegoBeautyPluginEffectsType> effectsTypes) {
        Map<ZegoBeautyPluginEffectsType, BeautyFeatureItem> beautyFeatureItemMap = new HashMap<>();

        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_EYELINER_NATURAL, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_makeup_eyeliner_natural,
                config -> config.innerText.beautyMakeupEyelinerNatural),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyeliner_natural),
            ZegoBeautyPluginEffectsType.MAKEUP_EYELINER_NATURAL));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_EYELINER_CAT_EYE, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_makeup_eyeliner_cat_eye,
                config -> config.innerText.beautyMakeupEyelinerCatEye),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyeliner_cat_eye),
            ZegoBeautyPluginEffectsType.MAKEUP_EYELINER_CAT_EYE));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_EYELINER_NAUGHTY, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_makeup_eyeliner_naughty,
                config -> config.innerText.beautyMakeupEyelinerNaughty),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyeliner_naughty),
            ZegoBeautyPluginEffectsType.MAKEUP_EYELINER_NAUGHTY));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_EYELINER_INNOCENT, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_makeup_eyeliner_innocent,
                config -> config.innerText.beautyMakeupEyelinerInnocent),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyeliner_innocent),
            ZegoBeautyPluginEffectsType.MAKEUP_EYELINER_INNOCENT));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_EYELINER_DIGNIFIED, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_makeup_eyeliner_dignified,
                config -> config.innerText.beautyMakeupEyelinerDignified),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyeliner_dignified),
            ZegoBeautyPluginEffectsType.MAKEUP_EYELINER_DIGNIFIED));

        List<BeautyFeatureItem> beautyFeatureItemList = new ArrayList<>();
        for (ZegoBeautyPluginEffectsType effectsType : effectsTypes) {
            BeautyFeatureItem beautyFeatureItem = beautyFeatureItemMap.get(effectsType);
            if (beautyFeatureItem != null) {
                beautyFeatureItemList.add(beautyFeatureItem);
            }
        }
        if (!beautyFeatureItemList.isEmpty()) {
            beautyFeatureItemList.add(0, new BeautyFeatureItem(
                getStringOrDefault(context, R.string.beauty_none, config -> config.innerText.beautyNone),
                ContextCompat.getDrawable(context, R.drawable.beauty_none_round),
                new BeautyFeature(null, ZegoBeautyPluginEffectsType.MAKEUP_EYELINER, BeautyGroup.MAKEUPS,
                    new EyelinerEditor())));
        }
        return beautyFeatureItemList;
    }

    public static List<BeautyFeatureItem> getMakeupEyeShadowItems(Context context,
        List<ZegoBeautyPluginEffectsType> effectsTypes) {
        Map<ZegoBeautyPluginEffectsType, BeautyFeatureItem> beautyFeatureItemMap = new HashMap<>();

        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_EYESHADOW_PINK_MIST, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_makeup_eyeshadow_pink_mist,
                config -> config.innerText.beautyMakeupEyeshadowPinkMist),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyeshadow_pink_mist),
            ZegoBeautyPluginEffectsType.MAKEUP_EYESHADOW_PINK_MIST));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_EYESHADOW_SHIMMER_PINK, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_makeup_eyeshadow_shimmer_pink,
                config -> config.innerText.beautyMakeupEyeshadowShimmerPink),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyeshadow_shimmer_pink),
            ZegoBeautyPluginEffectsType.MAKEUP_EYESHADOW_SHIMMER_PINK));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_EYESHADOW_TEA_BROWN, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_makeup_eyeshadow_tea_brown,
                config -> config.innerText.beautyMakeupEyeshadowTeaBrown),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyeshadow_tea_brown),
            ZegoBeautyPluginEffectsType.MAKEUP_EYESHADOW_TEA_BROWN));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_EYESHADOW_BRIGHT_ORANGE, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_makeup_eyeshadow_bright_orange,
                config -> config.innerText.beautyMakeupEyeshadowBrightOrange),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyeshadow_bright_orange),
            ZegoBeautyPluginEffectsType.MAKEUP_EYESHADOW_BRIGHT_ORANGE));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_EYESHADOW_MOCHA_BROWN, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_makeup_eyeshadow_mocha_brown,
                config -> config.innerText.beautyMakeupEyeshadowMochaBrown),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyeshadow_mocha_brown),
            ZegoBeautyPluginEffectsType.MAKEUP_EYESHADOW_MOCHA_BROWN));

        List<BeautyFeatureItem> beautyFeatureItemList = new ArrayList<>();
        for (ZegoBeautyPluginEffectsType effectsType : effectsTypes) {
            BeautyFeatureItem beautyFeatureItem = beautyFeatureItemMap.get(effectsType);
            if (beautyFeatureItem != null) {
                beautyFeatureItemList.add(beautyFeatureItem);
            }
        }
        if (!beautyFeatureItemList.isEmpty()) {
            beautyFeatureItemList.add(0, new BeautyFeatureItem(
                getStringOrDefault(context, R.string.beauty_none, config -> config.innerText.beautyNone),
                ContextCompat.getDrawable(context, R.drawable.beauty_none_round),
                new BeautyFeature(null, ZegoBeautyPluginEffectsType.MAKEUP_EYESHADOW, BeautyGroup.MAKEUPS,
                    new EyeshadowEditor())));
        }
        return beautyFeatureItemList;
    }

    public static List<BeautyFeatureItem> getMakeupColoredContactsItems(Context context,
        List<ZegoBeautyPluginEffectsType> effectsTypes) {
        Map<ZegoBeautyPluginEffectsType, BeautyFeatureItem> beautyFeatureItemMap = new HashMap<>();

        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_COLORED_CONTACTS_DARKNIGHT_BLACK,
            new BeautyFeatureItem(getStringOrDefault(context, R.string.beauty_makeup_colored_contacts_darknight_black,
                config -> config.innerText.beautyMakeupColoredContactsDarknightBlack),
                ContextCompat.getDrawable(context, R.drawable.beauty_makeup_colored_contacts_darknight_black),
                ZegoBeautyPluginEffectsType.MAKEUP_COLORED_CONTACTS_DARKNIGHT_BLACK));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_COLORED_CONTACTS_STARRY_BLUE, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_makeup_colored_contacts_starry_blue,
                config -> config.innerText.beautyMakeupColoredContactsStarryBlue),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_colored_contacts_starry_blue),
            ZegoBeautyPluginEffectsType.MAKEUP_COLORED_CONTACTS_STARRY_BLUE));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_COLORED_CONTACTS_BROWN_GREEN, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_makeup_colored_contacts_brown_green,
                config -> config.innerText.beautyMakeupColoredContactsBrownGreen),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_colored_contacts_brown_green),
            ZegoBeautyPluginEffectsType.MAKEUP_COLORED_CONTACTS_BROWN_GREEN));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_COLORED_CONTACTS_LIGHTS_BROWN,
            new BeautyFeatureItem(getStringOrDefault(context, R.string.beauty_makeup_colored_contacts_lights_brown,
                config -> config.innerText.beautyMakeupColoredContactsLightsBrown),
                ContextCompat.getDrawable(context, R.drawable.beauty_makeup_colored_contacts_lights_brown),
                ZegoBeautyPluginEffectsType.MAKEUP_COLORED_CONTACTS_LIGHTS_BROWN));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.MAKEUP_COLORED_CONTACTS_CHOCOLATE_BROWN,
            new BeautyFeatureItem(
                getStringOrDefault(context, R.string.beauty_makeup_colored_contacts_chocolate_contacts,
                    config -> config.innerText.beautyMakeupColoredContactsChocolateBrown),
                ContextCompat.getDrawable(context, R.drawable.beauty_makeup_colored_contacts_chocolate_brown),
                ZegoBeautyPluginEffectsType.MAKEUP_COLORED_CONTACTS_CHOCOLATE_BROWN));

        List<BeautyFeatureItem> beautyFeatureItemList = new ArrayList<>();
        for (ZegoBeautyPluginEffectsType effectsType : effectsTypes) {
            BeautyFeatureItem beautyFeatureItem = beautyFeatureItemMap.get(effectsType);
            if (beautyFeatureItem != null) {
                beautyFeatureItemList.add(beautyFeatureItem);
            }
        }
        if (!beautyFeatureItemList.isEmpty()) {
            beautyFeatureItemList.add(0, new BeautyFeatureItem(
                getStringOrDefault(context, R.string.beauty_none, config -> config.innerText.beautyNone),
                ContextCompat.getDrawable(context, R.drawable.beauty_none_round),
                new BeautyFeature(null, ZegoBeautyPluginEffectsType.MAKEUP_COLORED_CONTACTS, BeautyGroup.MAKEUPS,
                    new ColoredContactsEditor())));
        }
        return beautyFeatureItemList;
    }

    public static List<BeautyFeatureItem> getMakeupStyleItems(Context context,
        List<ZegoBeautyPluginEffectsType> effectsTypes) {
        Map<ZegoBeautyPluginEffectsType, BeautyFeatureItem> beautyFeatureItemMap = new HashMap<>();

        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.STYLE_MAKEUP_INNOCENT_EYES, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_group_style_innocent_eyes,
                config -> config.innerText.beautyStyleMakeupInnocentEyes),
            ContextCompat.getDrawable(context, R.drawable.beauty_style_makeup_innocent_eyes),
            ZegoBeautyPluginEffectsType.STYLE_MAKEUP_INNOCENT_EYES));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.STYLE_MAKEUP_MILKY_EYES, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_group_style_milky_eyes,
                config -> config.innerText.beautyStyleMakeupMilkyEyes),
            ContextCompat.getDrawable(context, R.drawable.beauty_style_makeup_milky_eyes),
            ZegoBeautyPluginEffectsType.STYLE_MAKEUP_MILKY_EYES));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.STYLE_MAKEUP_CUTIE_COOL, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_group_style_cutie_cool,
                config -> config.innerText.beautyStyleMakeupCutieCool),
            ContextCompat.getDrawable(context, R.drawable.beauty_style_makeup_cutie_cool),
            ZegoBeautyPluginEffectsType.STYLE_MAKEUP_CUTIE_COOL));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.STYLE_MAKEUP_PURE_SEXY, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_group_style_pure_sexy,
                config -> config.innerText.beautyStyleMakeupPureSexy),
            ContextCompat.getDrawable(context, R.drawable.beauty_style_makeup_pure_sexy),
            ZegoBeautyPluginEffectsType.STYLE_MAKEUP_PURE_SEXY));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.STYLE_MAKEUP_FLAWLESS, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_group_style_flawless,
                config -> config.innerText.beautyStyleMakeupFlawless),
            ContextCompat.getDrawable(context, R.drawable.beauty_style_makeup_flawless),
            ZegoBeautyPluginEffectsType.STYLE_MAKEUP_FLAWLESS));

        List<BeautyFeatureItem> beautyFeatureItemList = new ArrayList<>();
        for (ZegoBeautyPluginEffectsType effectsType : effectsTypes) {
            BeautyFeatureItem beautyFeatureItem = beautyFeatureItemMap.get(effectsType);
            if (beautyFeatureItem != null) {
                beautyFeatureItemList.add(beautyFeatureItem);
            }
        }
        if (!beautyFeatureItemList.isEmpty()) {
            beautyFeatureItemList.add(0, new BeautyFeatureItem(
                getStringOrDefault(context, R.string.beauty_none, config -> config.innerText.beautyNone),
                ContextCompat.getDrawable(context, R.drawable.beauty_none_rect),
                new BeautyFeature(BeautyGroup.STYLE_MAKEUP, new StyleMakeupEditor())));
        }
        return beautyFeatureItemList;
    }

    public static List<BeautyFeatureItem> getFilterItems(Context context,
        List<ZegoBeautyPluginEffectsType> effectsTypes) {
        Map<ZegoBeautyPluginEffectsType, BeautyFeatureItem> beautyFeatureItemMap = new HashMap<>();

        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.FILTER_NATURAL_CREAMY, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_filter_natural_creamy,
                config -> config.innerText.filterNaturalCreamy),
            ContextCompat.getDrawable(context, R.drawable.beauty_filter_natural_creamy),
            ZegoBeautyPluginEffectsType.FILTER_NATURAL_CREAMY));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.FILTER_NATURAL_BRIGHTEN, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_filter_natural_brighten,
                config -> config.innerText.filterNaturalBrighten),
            ContextCompat.getDrawable(context, R.drawable.beauty_filter_natural_brighten),
            ZegoBeautyPluginEffectsType.FILTER_NATURAL_BRIGHTEN));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.FILTER_NATURAL_FRESH, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_filter_natural_fresh,
                config -> config.innerText.filterNaturalFresh),
            ContextCompat.getDrawable(context, R.drawable.beauty_filter_natural_fresh),
            ZegoBeautyPluginEffectsType.FILTER_NATURAL_FRESH));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.FILTER_NATURAL_AUTUMN, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_filter_natural_autumn,
                config -> config.innerText.filterNaturalAutumn),
            ContextCompat.getDrawable(context, R.drawable.beauty_filter_natural_autumn),
            ZegoBeautyPluginEffectsType.FILTER_NATURAL_AUTUMN));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.FILTER_GRAY_MONET, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_filter_gray_monet, config -> config.innerText.filterGrayMonet),
            ContextCompat.getDrawable(context, R.drawable.beauty_filter_gray_monet),
            ZegoBeautyPluginEffectsType.FILTER_GRAY_MONET));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.FILTER_GRAY_NIGHT, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_filter_gray_night, config -> config.innerText.filterGrayNight),
            ContextCompat.getDrawable(context, R.drawable.beauty_filter_gray_night),
            ZegoBeautyPluginEffectsType.FILTER_GRAY_NIGHT));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.FILTER_GRAY_FILMLIKE, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_filter_gray_filmlike,
                config -> config.innerText.filterGrayFilmlike),
            ContextCompat.getDrawable(context, R.drawable.beauty_filter_gray_filmlike),
            ZegoBeautyPluginEffectsType.FILTER_GRAY_FILMLIKE));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.FILTER_DREAMY_SUNSET, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_filter_dreamy_sunset,
                config -> config.innerText.filterDreamySunset),
            ContextCompat.getDrawable(context, R.drawable.beauty_filter_dreamy_sunset),
            ZegoBeautyPluginEffectsType.FILTER_DREAMY_SUNSET));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.FILTER_DREAMY_COZILY, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_filter_dreamy_cozily,
                config -> config.innerText.filterDreamyCozily),
            ContextCompat.getDrawable(context, R.drawable.beauty_filter_dreamy_cozily),
            ZegoBeautyPluginEffectsType.FILTER_DREAMY_COZILY));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.FILTER_DREAMY_SWEET, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_filter_dreamy_sweet,
                config -> config.innerText.filterDreamySweet),
            ContextCompat.getDrawable(context, R.drawable.beauty_filter_dreamy_sweet),
            ZegoBeautyPluginEffectsType.FILTER_DREAMY_SWEET));

        List<BeautyFeatureItem> beautyFeatureItemList = new ArrayList<>();
        for (ZegoBeautyPluginEffectsType effectsType : effectsTypes) {
            BeautyFeatureItem beautyFeatureItem = beautyFeatureItemMap.get(effectsType);
            if (beautyFeatureItem != null) {
                beautyFeatureItemList.add(beautyFeatureItem);
            }
        }
        if (!beautyFeatureItemList.isEmpty()) {
            beautyFeatureItemList.add(0, new BeautyFeatureItem(
                getStringOrDefault(context, R.string.beauty_none, config -> config.innerText.beautyNone),
                ContextCompat.getDrawable(context, R.drawable.beauty_none_rect),
                new BeautyFeature(BeautyGroup.FILTERS, new FilterEditor())));
        }
        return beautyFeatureItemList;
    }

    public static List<BeautyFeatureItem> getStickerItems(Context context,
        List<ZegoBeautyPluginEffectsType> effectsTypes) {
        Map<ZegoBeautyPluginEffectsType, BeautyFeatureItem> beautyFeatureItemMap = new HashMap<>();

        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.STICKER_ANIMAL, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_sticker_animal, config -> config.innerText.stickerAnimal),
            ContextCompat.getDrawable(context, R.drawable.beauty_sticker_animal),
            ZegoBeautyPluginEffectsType.STICKER_ANIMAL));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.STICKER_DIVE, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_sticker_dive, config -> config.innerText.stickerDive),
            ContextCompat.getDrawable(context, R.drawable.beauty_sticker_dive),
            ZegoBeautyPluginEffectsType.STICKER_DIVE));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.STICKER_CAT, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_sticker_cat, config -> config.innerText.stickerCat),
            ContextCompat.getDrawable(context, R.drawable.beauty_sticker_cat),
            ZegoBeautyPluginEffectsType.STICKER_CAT));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.STICKER_WATERMELON, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_sticker_watermelon,
                config -> config.innerText.stickerWatermelon),
            ContextCompat.getDrawable(context, R.drawable.beauty_sticker_watermelon),
            ZegoBeautyPluginEffectsType.STICKER_WATERMELON));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.STICKER_DEER, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_sticker_deer, config -> config.innerText.stickerDeer),
            ContextCompat.getDrawable(context, R.drawable.beauty_sticker_deer),
            ZegoBeautyPluginEffectsType.STICKER_DEER));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.STICKER_COOL_GIRL, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_sticker_cool_girl, config -> config.innerText.stickerCoolGirl),
            ContextCompat.getDrawable(context, R.drawable.beauty_sticker_cool_girl),
            ZegoBeautyPluginEffectsType.STICKER_COOL_GIRL));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.STICKER_CLOWN, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_sticker_clown, config -> config.innerText.stickerClown),
            ContextCompat.getDrawable(context, R.drawable.beauty_sticker_clown),
            ZegoBeautyPluginEffectsType.STICKER_CLOWN));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.STICKER_CLAW_MACHINE, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_sticker_claw_machine,
                config -> config.innerText.stickerClawMachine),
            ContextCompat.getDrawable(context, R.drawable.beauty_sticker_claw_machine),
            ZegoBeautyPluginEffectsType.STICKER_CLAW_MACHINE));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.STICKER_SAILOR_MOON, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_sticker_sailor_moon,
                config -> config.innerText.stickerSailorMoon),
            ContextCompat.getDrawable(context, R.drawable.beauty_sticker_sailor_moon),
            ZegoBeautyPluginEffectsType.STICKER_SAILOR_MOON));

        List<BeautyFeatureItem> beautyFeatureItemList = new ArrayList<>();
        for (ZegoBeautyPluginEffectsType effectsType : effectsTypes) {
            BeautyFeatureItem beautyFeatureItem = beautyFeatureItemMap.get(effectsType);
            if (beautyFeatureItem != null) {
                beautyFeatureItemList.add(beautyFeatureItem);
            }
        }
        if (!beautyFeatureItemList.isEmpty()) {
            beautyFeatureItemList.add(0, new BeautyFeatureItem(
                getStringOrDefault(context, R.string.beauty_none, config -> config.innerText.beautyNone),
                ContextCompat.getDrawable(context, R.drawable.beauty_none_rect),
                new BeautyFeature(BeautyGroup.STICKERS, new StickerEditor())));
        }
        return beautyFeatureItemList;
    }

    public static List<BeautyFeatureItem> getBackgroundItems(Context context,
        List<ZegoBeautyPluginEffectsType> effectsTypes) {
        Map<ZegoBeautyPluginEffectsType, BeautyFeatureItem> beautyFeatureItemMap = new HashMap<>();
//        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.BACKGROUND_GREEN_SCREEN_SEGMENTATION, new BeautyFeatureItem(
//            getStringOrDefault(context, R.string.beauty_background_green_screen_segmentation,
//                config -> config.innerText.backgroundGreenScreenSegmentation),
//            ContextCompat.getDrawable(context, R.drawable.beauty_background_green_screen_segmentation),
//            ZegoBeautyPluginEffectsType.BACKGROUND_GREEN_SCREEN_SEGMENTATION));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.BACKGROUND_PORTRAIT_SEGMENTATION, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_background_portrait_segmentation,
                config -> config.innerText.backgroundPortraitSegmentation),
            ContextCompat.getDrawable(context, R.drawable.beauty_background_portrait_segmentation),
            ZegoBeautyPluginEffectsType.BACKGROUND_PORTRAIT_SEGMENTATION));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.BACKGROUND_MOSAICING, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_background_mosaicing,
                config -> config.innerText.backgroundMosaicing),
            ContextCompat.getDrawable(context, R.drawable.beauty_background_mosaicing),
            ZegoBeautyPluginEffectsType.BACKGROUND_MOSAICING));
        beautyFeatureItemMap.put(ZegoBeautyPluginEffectsType.BACKGROUND_GAUSSIAN_BLUR, new BeautyFeatureItem(
            getStringOrDefault(context, R.string.beauty_background_gaussian_blur,
                config -> config.innerText.backgroundGaussianBlur),
            ContextCompat.getDrawable(context, R.drawable.beauty_background_gaussian_blur),
            ZegoBeautyPluginEffectsType.BACKGROUND_GAUSSIAN_BLUR));

        List<BeautyFeatureItem> beautyFeatureItemList = new ArrayList<>();
        for (ZegoBeautyPluginEffectsType effectsType : effectsTypes) {
            BeautyFeatureItem beautyFeatureItem = beautyFeatureItemMap.get(effectsType);
            if (beautyFeatureItem != null) {
                beautyFeatureItemList.add(beautyFeatureItem);
            }
        }
        if (!beautyFeatureItemList.isEmpty()) {
            beautyFeatureItemList.add(0, new BeautyFeatureItem(
                getStringOrDefault(context, R.string.beauty_none, config -> config.innerText.beautyNone),
                ContextCompat.getDrawable(context, R.drawable.beauty_none_rect),
                new BeautyFeature(BeautyGroup.BACKGROUND, new SimpleEditor())));
        }
        return beautyFeatureItemList;
    }

    private static String getStringOrDefault(Context context, int resId,
        Function<ZegoBeautyPluginConfig, String> getter) {
        String original = context.getString(resId);
        ZegoBeautyPluginConfig beautyPluginConfig = ZegoUIKitBeautyPlugin.getInstance().getBeautyPluginConfig();
        if (beautyPluginConfig != null && beautyPluginConfig.innerText != null) {
            String configValue = getter.apply(beautyPluginConfig);
            if (configValue != null) {
                return configValue;
            }
        }
        return original;
    }

}
