package com.zegocloud.uikit.plugin.beauty;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.net.Uri;
import android.text.TextUtils;
import com.zegocloud.uikit.plugin.adapter.plugins.beauty.ZegoBeautyPluginEffectsType;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.BlurEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.BlusherEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.BlusherMakeupEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.CheekboneSlimmingEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.ChinLengtheningEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.ColoredContactsEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.DarkCirclesEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.EyelashesEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.EyelinerEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.EyesBrighteningEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.EyesEnlargingEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.EyeshadowEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.FaceShorteningEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.FaceSlimmingEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.FilterEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.ForeheadSlimmingEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.LipstickEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.MandibleSlimmingEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.MosaicEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.MouthReshapeEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.NoseLengtheningEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.NoseSlimmingEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.PortraitSegmentationEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.SharpeningEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.SkinToneEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.SmoothingEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.StickerEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.StyleMakeupEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.TeethWhiteningEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.WrinklesEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyFeature;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyGroup;
import com.zegocloud.uikit.plugin.beauty.net.APIBase;
import com.zegocloud.uikit.plugin.beauty.net.IGetLicenseCallback;
import com.zegocloud.uikit.plugin.beauty.net.License;
import im.zego.effects.ZegoEffects;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EffectSDKHelper {

    public static void setResources(Context context, String rootFolder, String resourceFolderName) {
        boolean assetFileExists = isAssetFileExists(context, resourceFolderName);
        String resourceRootFolder = rootFolder + File.separator + resourceFolderName;
        File file = new File(resourceRootFolder);
        if (assetFileExists && !file.exists()) {
            ZegoUtil.copyFileFromAssets(context, resourceFolderName, resourceRootFolder);
        }

        ArrayList<String> resources = new ArrayList<>();

        String faceDetectionModel = resourceRootFolder + File.separator + "FaceDetection.model";
        String commonResources = resourceRootFolder + File.separator + "CommonResources.bundle";
        String faceWhiteningResources = commonResources + File.separator + "FaceWhiteningResources";
        String rosyResources = commonResources + File.separator + "RosyResources";
        String teethWhiteningResources = commonResources + File.separator + "TeethWhiteningResources";
        String stickerPath = resourceRootFolder + File.separator + "StickerBaseResources.bundle";
        String segmentationPath = resourceRootFolder + File.separator + "BackgroundSegmentation.model";

        resources.add(faceDetectionModel);
        resources.add(commonResources);
        resources.add(faceWhiteningResources);
        resources.add(rosyResources);
        resources.add(teethWhiteningResources);
        resources.add(stickerPath);
        resources.add(segmentationPath);
        ZegoEffects.setResources(resources);
    }

    private static boolean isAssetFileExists(Context context, String fileName) {
        AssetManager assetManager = context.getAssets();
        try {
            String[] files = assetManager.list("");
            for (String file : files) {
                if (file.equals(fileName)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void getLicense(Context context, String baseUrl, long appID, String appSign,
        IGetLicenseCallback callback) {
        SharedPreferences sp = context.getSharedPreferences("effect", Context.MODE_PRIVATE);
        if (sp.contains("licence")) {
            long timestamp = sp.getLong("timestamp", 0L);
            String string = sp.getString("licence", "");
            if (System.currentTimeMillis() - timestamp < 24 * 60 * 60 * 1000) {
                License license = new License();
                license.setLicense(string);
                if (callback != null) {
                    callback.onGetLicense(0, "success from local", license);
                }
                return;
            }
        }

        String authInfo = ZegoEffects.getAuthInfo(appSign, context);
        Uri.Builder builder = Uri.parse(baseUrl).buildUpon();
        builder.appendQueryParameter("AppId", String.valueOf(appID));
        builder.appendQueryParameter("AuthInfo", authInfo);
        String url = builder.build().toString();

        APIBase.asyncGet(url, License.class, (code, message, responseJsonBean) -> {
            if (code == 0) {
                Editor edit = sp.edit();
                edit.putString("licence", responseJsonBean.getLicense());
                edit.putLong("timestamp", System.currentTimeMillis());
                edit.apply();
            }
            if (callback != null) {
                callback.onGetLicense(code, message, responseJsonBean);
            }
        });
    }

    public static Map<BeautyGroup, List<BeautyFeature>> getAllFeatures() {
        Map<BeautyGroup, List<BeautyFeature>> results = new HashMap<>();
        results.put(BeautyGroup.BASIC, getBeautyBasics());
        results.put(BeautyGroup.ADVANCED, getBeautyAdvanced());
        results.put(BeautyGroup.MAKEUPS, getBeautyMakeups());
        results.put(BeautyGroup.STYLE_MAKEUP, getBeautyStyleMakeUps());
        results.put(BeautyGroup.FILTERS, getFilters());
        results.put(BeautyGroup.STICKERS, getStickers());
        results.put(BeautyGroup.BACKGROUND, getBackgrounds());
        return results;
    }

    public static List<BeautyFeature> getBeautyBasics() {
        List<BeautyFeature> basics = new ArrayList<>();
        basics.add(
            new BeautyFeature(ZegoBeautyPluginEffectsType.BASIC_SMOOTHING, BeautyGroup.BASIC, new SmoothingEditor()));
        basics.add(
            new BeautyFeature(ZegoBeautyPluginEffectsType.BASIC_SKIN_TONE, BeautyGroup.BASIC, new SkinToneEditor()));
        basics.add(
            new BeautyFeature(ZegoBeautyPluginEffectsType.BASIC_BLUSHER, BeautyGroup.BASIC, new BlusherEditor()));
        basics.add(
            new BeautyFeature(ZegoBeautyPluginEffectsType.BASIC_SHARPENING, BeautyGroup.BASIC, new SharpeningEditor()));
        basics.add(
            new BeautyFeature(ZegoBeautyPluginEffectsType.BASIC_WRINKLES, BeautyGroup.BASIC, new WrinklesEditor()));
        basics.add(new BeautyFeature(ZegoBeautyPluginEffectsType.BASIC_DARK_CIRCLES, BeautyGroup.BASIC,
            new DarkCirclesEditor()));
        return basics;
    }

    public static List<BeautyFeature> getBeautyAdvanced() {
        List<BeautyFeature> advanced = new ArrayList<>();
        advanced.add(new BeautyFeature(ZegoBeautyPluginEffectsType.ADVANCED_FACE_SLIMMING, BeautyGroup.ADVANCED,
            new FaceSlimmingEditor()));
        advanced.add(new BeautyFeature(ZegoBeautyPluginEffectsType.ADVANCED_EYES_ENLARGING, BeautyGroup.ADVANCED,
            new EyesEnlargingEditor()));
        advanced.add(new BeautyFeature(ZegoBeautyPluginEffectsType.ADVANCED_EYES_BRIGHTENING, BeautyGroup.ADVANCED,
            new EyesBrighteningEditor()));
        advanced.add(
            new BeautyFeature(ZegoBeautyPluginEffectsType.ADVANCED_CHIN_LENGTHENING, BeautyGroup.ADVANCED, -100, 100,
                50, new ChinLengtheningEditor()));
        advanced.add(
            new BeautyFeature(ZegoBeautyPluginEffectsType.ADVANCED_MOUTH_RESHAPE, BeautyGroup.ADVANCED, -100, 100, 50,
                new MouthReshapeEditor()));
        advanced.add(new BeautyFeature(ZegoBeautyPluginEffectsType.ADVANCED_TEETH_WHITENING, BeautyGroup.ADVANCED,
            new TeethWhiteningEditor()));
        advanced.add(new BeautyFeature(ZegoBeautyPluginEffectsType.ADVANCED_NOSE_SLIMMING, BeautyGroup.ADVANCED,
            new NoseSlimmingEditor()));
        advanced.add(
            new BeautyFeature(ZegoBeautyPluginEffectsType.ADVANCED_NOSE_LENGTHENING, BeautyGroup.ADVANCED, -100, 100,
                50, new NoseLengtheningEditor()));
        advanced.add(new BeautyFeature(ZegoBeautyPluginEffectsType.ADVANCED_FACE_SHORTENING, BeautyGroup.ADVANCED,
            new FaceShorteningEditor()));
        advanced.add(new BeautyFeature(ZegoBeautyPluginEffectsType.ADVANCED_MANDIBLE_SLIMMING, BeautyGroup.ADVANCED,
            new MandibleSlimmingEditor()));
        advanced.add(new BeautyFeature(ZegoBeautyPluginEffectsType.ADVANCED_CHEEKBONE_SLIMMING, BeautyGroup.ADVANCED,
            new CheekboneSlimmingEditor()));
        advanced.add(
            new BeautyFeature(ZegoBeautyPluginEffectsType.ADVANCED_FOREHEAD_SLIMMING, BeautyGroup.ADVANCED, -100, 100,
                50, new ForeheadSlimmingEditor()));
        return advanced;
    }

    public static List<BeautyFeature> getBeautyMakeups() {
        List<BeautyFeature> beautyFeatures = new ArrayList<>();
        beautyFeatures.add(new BeautyFeature(ZegoBeautyPluginEffectsType.MAKEUP_LIPSTICK,
            Arrays.asList(ZegoBeautyPluginEffectsType.MAKEUP_LIPSTICK_CAMEO_PINK,
                ZegoBeautyPluginEffectsType.MAKEUP_LIPSTICK_SWEET_ORANGE,
                ZegoBeautyPluginEffectsType.MAKEUP_LIPSTICK_RUST_RED, ZegoBeautyPluginEffectsType.MAKEUP_LIPSTICK_CORAL,
                ZegoBeautyPluginEffectsType.MAKEUP_LIPSTICK_RED_VELVET), BeautyGroup.MAKEUPS, new LipstickEditor()));
        beautyFeatures.add(new BeautyFeature(ZegoBeautyPluginEffectsType.MAKEUP_BLUSHER,
            Arrays.asList(ZegoBeautyPluginEffectsType.MAKEUP_BLUSHER_SLIGHTLY_DRUNK,
                ZegoBeautyPluginEffectsType.MAKEUP_BLUSHER_PEACH,
                ZegoBeautyPluginEffectsType.MAKEUP_BLUSHER_MILKY_ORANGE,
                ZegoBeautyPluginEffectsType.MAKEUP_BLUSHER_APRICOT_PINK,
                ZegoBeautyPluginEffectsType.MAKEUP_BLUSHER_SWEET_ORANGE), BeautyGroup.MAKEUPS,
            new BlusherMakeupEditor()));
        beautyFeatures.add(new BeautyFeature(ZegoBeautyPluginEffectsType.MAKEUP_EYELASHES,
            Arrays.asList(ZegoBeautyPluginEffectsType.MAKEUP_EYELASHES_NATURAL,
                ZegoBeautyPluginEffectsType.MAKEUP_EYELASHES_TENDER, ZegoBeautyPluginEffectsType.MAKEUP_EYELASHES_CURL,
                ZegoBeautyPluginEffectsType.MAKEUP_EYELASHES_EVERLONG,
                ZegoBeautyPluginEffectsType.MAKEUP_EYELASHES_THICK), BeautyGroup.MAKEUPS, new EyelashesEditor()));
        beautyFeatures.add(new BeautyFeature(ZegoBeautyPluginEffectsType.MAKEUP_EYELINER,
            Arrays.asList(ZegoBeautyPluginEffectsType.MAKEUP_EYELINER_NATURAL,
                ZegoBeautyPluginEffectsType.MAKEUP_EYELINER_CAT_EYE,
                ZegoBeautyPluginEffectsType.MAKEUP_EYELINER_NAUGHTY,
                ZegoBeautyPluginEffectsType.MAKEUP_EYELINER_INNOCENT,
                ZegoBeautyPluginEffectsType.MAKEUP_EYELINER_DIGNIFIED), BeautyGroup.MAKEUPS, new EyelinerEditor()));
        beautyFeatures.add(new BeautyFeature(ZegoBeautyPluginEffectsType.MAKEUP_EYESHADOW,
            Arrays.asList(ZegoBeautyPluginEffectsType.MAKEUP_EYESHADOW_PINK_MIST,
                ZegoBeautyPluginEffectsType.MAKEUP_EYESHADOW_SHIMMER_PINK,
                ZegoBeautyPluginEffectsType.MAKEUP_EYESHADOW_TEA_BROWN,
                ZegoBeautyPluginEffectsType.MAKEUP_EYESHADOW_BRIGHT_ORANGE,
                ZegoBeautyPluginEffectsType.MAKEUP_EYESHADOW_MOCHA_BROWN), BeautyGroup.MAKEUPS, new EyeshadowEditor()));
        beautyFeatures.add(new BeautyFeature(ZegoBeautyPluginEffectsType.MAKEUP_COLORED_CONTACTS,
            Arrays.asList(ZegoBeautyPluginEffectsType.MAKEUP_COLORED_CONTACTS_DARKNIGHT_BLACK,
                ZegoBeautyPluginEffectsType.MAKEUP_COLORED_CONTACTS_STARRY_BLUE,
                ZegoBeautyPluginEffectsType.MAKEUP_COLORED_CONTACTS_BROWN_GREEN,
                ZegoBeautyPluginEffectsType.MAKEUP_COLORED_CONTACTS_LIGHTS_BROWN,
                ZegoBeautyPluginEffectsType.MAKEUP_COLORED_CONTACTS_CHOCOLATE_BROWN), BeautyGroup.MAKEUPS,
            new ColoredContactsEditor()));
        beautyFeatures.addAll(getBeautyMakeupLipsticks());
        beautyFeatures.addAll(getBeautyMakeupBlushers());
        beautyFeatures.addAll(getBeautyMakeupEyelashes());
        beautyFeatures.addAll(getBeautyMakeupEyeliners());
        beautyFeatures.addAll(getBeautyMakeupEyeshadows());
        beautyFeatures.addAll(getBeautyMakeupColoredContact());
        return beautyFeatures;
    }

    private static List<BeautyFeature> getBeautyMakeupLipsticks() {
        List<ZegoBeautyPluginEffectsType> beautyTypes = Arrays.asList(
            ZegoBeautyPluginEffectsType.MAKEUP_LIPSTICK_CAMEO_PINK,
            ZegoBeautyPluginEffectsType.MAKEUP_LIPSTICK_SWEET_ORANGE,
            ZegoBeautyPluginEffectsType.MAKEUP_LIPSTICK_RUST_RED, ZegoBeautyPluginEffectsType.MAKEUP_LIPSTICK_CORAL,
            ZegoBeautyPluginEffectsType.MAKEUP_LIPSTICK_RED_VELVET);
        List<BeautyFeature> lipsticks = new ArrayList<>();
        for (ZegoBeautyPluginEffectsType type : beautyTypes) {
            lipsticks.add(new BeautyFeature(type, ZegoBeautyPluginEffectsType.MAKEUP_LIPSTICK, BeautyGroup.MAKEUPS,
                new LipstickEditor(getResourcePath(type))));
        }
        return lipsticks;
    }

    private static List<BeautyFeature> getBeautyMakeupBlushers() {
        List<ZegoBeautyPluginEffectsType> beautyTypes = Arrays.asList(
            ZegoBeautyPluginEffectsType.MAKEUP_BLUSHER_SLIGHTLY_DRUNK, ZegoBeautyPluginEffectsType.MAKEUP_BLUSHER_PEACH,
            ZegoBeautyPluginEffectsType.MAKEUP_BLUSHER_MILKY_ORANGE,
            ZegoBeautyPluginEffectsType.MAKEUP_BLUSHER_APRICOT_PINK,
            ZegoBeautyPluginEffectsType.MAKEUP_BLUSHER_SWEET_ORANGE);
        List<BeautyFeature> blushers = new ArrayList<>();
        for (ZegoBeautyPluginEffectsType type : beautyTypes) {
            blushers.add(new BeautyFeature(type, ZegoBeautyPluginEffectsType.MAKEUP_BLUSHER, BeautyGroup.MAKEUPS,
                new BlusherMakeupEditor(getResourcePath(type))));
        }
        return blushers;
    }

    private static List<BeautyFeature> getBeautyMakeupEyelashes() {
        List<ZegoBeautyPluginEffectsType> beautyTypes = Arrays.asList(
            ZegoBeautyPluginEffectsType.MAKEUP_EYELASHES_NATURAL, ZegoBeautyPluginEffectsType.MAKEUP_EYELASHES_TENDER,
            ZegoBeautyPluginEffectsType.MAKEUP_EYELASHES_CURL, ZegoBeautyPluginEffectsType.MAKEUP_EYELASHES_EVERLONG,
            ZegoBeautyPluginEffectsType.MAKEUP_EYELASHES_THICK);
        List<BeautyFeature> eyelashes = new ArrayList<>();
        for (ZegoBeautyPluginEffectsType type : beautyTypes) {
            eyelashes.add(new BeautyFeature(type, ZegoBeautyPluginEffectsType.MAKEUP_EYELASHES, BeautyGroup.MAKEUPS,
                new EyelashesEditor(getResourcePath(type))));
        }
        return eyelashes;
    }

    private static List<BeautyFeature> getBeautyMakeupEyeliners() {
        List<ZegoBeautyPluginEffectsType> beautyTypes = Arrays.asList(
            ZegoBeautyPluginEffectsType.MAKEUP_EYELINER_NATURAL, ZegoBeautyPluginEffectsType.MAKEUP_EYELINER_CAT_EYE,
            ZegoBeautyPluginEffectsType.MAKEUP_EYELINER_NAUGHTY, ZegoBeautyPluginEffectsType.MAKEUP_EYELINER_INNOCENT,
            ZegoBeautyPluginEffectsType.MAKEUP_EYELINER_DIGNIFIED);
        List<BeautyFeature> beauties = new ArrayList<>();
        for (ZegoBeautyPluginEffectsType beautyType : beautyTypes) {
            beauties.add(new BeautyFeature(beautyType, ZegoBeautyPluginEffectsType.MAKEUP_EYELINER, BeautyGroup.MAKEUPS,
                new EyelinerEditor(getResourcePath(beautyType))));
        }
        return beauties;
    }

    private static List<BeautyFeature> getBeautyMakeupEyeshadows() {
        List<ZegoBeautyPluginEffectsType> beautyTypes = Arrays.asList(
            ZegoBeautyPluginEffectsType.MAKEUP_EYESHADOW_PINK_MIST,
            ZegoBeautyPluginEffectsType.MAKEUP_EYESHADOW_SHIMMER_PINK,
            ZegoBeautyPluginEffectsType.MAKEUP_EYESHADOW_TEA_BROWN,
            ZegoBeautyPluginEffectsType.MAKEUP_EYESHADOW_BRIGHT_ORANGE,
            ZegoBeautyPluginEffectsType.MAKEUP_EYESHADOW_MOCHA_BROWN);
        List<BeautyFeature> beauties = new ArrayList<>();
        for (ZegoBeautyPluginEffectsType beautyType : beautyTypes) {
            beauties.add(
                new BeautyFeature(beautyType, ZegoBeautyPluginEffectsType.MAKEUP_EYESHADOW, BeautyGroup.MAKEUPS,
                    new EyeshadowEditor(getResourcePath(beautyType))));
        }
        return beauties;
    }

    private static List<BeautyFeature> getBeautyMakeupColoredContact() {
        List<ZegoBeautyPluginEffectsType> beautyTypes = Arrays.asList(
            ZegoBeautyPluginEffectsType.MAKEUP_COLORED_CONTACTS_DARKNIGHT_BLACK,
            ZegoBeautyPluginEffectsType.MAKEUP_COLORED_CONTACTS_STARRY_BLUE,
            ZegoBeautyPluginEffectsType.MAKEUP_COLORED_CONTACTS_BROWN_GREEN,
            ZegoBeautyPluginEffectsType.MAKEUP_COLORED_CONTACTS_LIGHTS_BROWN,
            ZegoBeautyPluginEffectsType.MAKEUP_COLORED_CONTACTS_CHOCOLATE_BROWN);
        List<BeautyFeature> beauties = new ArrayList<>();
        for (ZegoBeautyPluginEffectsType beautyType : beautyTypes) {
            beauties.add(
                new BeautyFeature(beautyType, ZegoBeautyPluginEffectsType.MAKEUP_COLORED_CONTACTS, BeautyGroup.MAKEUPS,
                    new ColoredContactsEditor(getResourcePath(beautyType))));
        }
        return beauties;
    }

    public static List<BeautyFeature> getBeautyStyleMakeUps() {
        List<ZegoBeautyPluginEffectsType> beautyTypes = Arrays.asList(
            ZegoBeautyPluginEffectsType.STYLE_MAKEUP_INNOCENT_EYES, ZegoBeautyPluginEffectsType.STYLE_MAKEUP_MILKY_EYES,
            ZegoBeautyPluginEffectsType.STYLE_MAKEUP_CUTIE_COOL, ZegoBeautyPluginEffectsType.STYLE_MAKEUP_PURE_SEXY,
            ZegoBeautyPluginEffectsType.STYLE_MAKEUP_FLAWLESS);
        List<BeautyFeature> beauties = new ArrayList<>();
        for (ZegoBeautyPluginEffectsType beautyType : beautyTypes) {
            beauties.add(new BeautyFeature(beautyType, BeautyGroup.STYLE_MAKEUP,
                new StyleMakeupEditor(getResourcePath(beautyType))));
        }
        return beauties;
    }

    public static List<BeautyFeature> getFilters() {
        List<ZegoBeautyPluginEffectsType> beautyTypes = Arrays.asList(ZegoBeautyPluginEffectsType.FILTER_NATURAL_CREAMY,
            ZegoBeautyPluginEffectsType.FILTER_NATURAL_BRIGHTEN, ZegoBeautyPluginEffectsType.FILTER_NATURAL_FRESH,
            ZegoBeautyPluginEffectsType.FILTER_NATURAL_AUTUMN, ZegoBeautyPluginEffectsType.FILTER_GRAY_MONET,
            ZegoBeautyPluginEffectsType.FILTER_GRAY_NIGHT, ZegoBeautyPluginEffectsType.FILTER_GRAY_FILMLIKE,
            ZegoBeautyPluginEffectsType.FILTER_DREAMY_SUNSET, ZegoBeautyPluginEffectsType.FILTER_DREAMY_COZILY,
            ZegoBeautyPluginEffectsType.FILTER_DREAMY_SWEET);
        List<BeautyFeature> beauties = new ArrayList<>();
        for (ZegoBeautyPluginEffectsType beautyType : beautyTypes) {
            beauties.add(
                new BeautyFeature(beautyType, BeautyGroup.FILTERS, new FilterEditor(getResourcePath(beautyType))));
        }
        return beauties;
    }

    public static List<BeautyFeature> getStickers() {
        List<ZegoBeautyPluginEffectsType> beautyTypes = Arrays.asList(ZegoBeautyPluginEffectsType.STICKER_ANIMAL,
            ZegoBeautyPluginEffectsType.STICKER_DIVE, ZegoBeautyPluginEffectsType.STICKER_CAT,
            ZegoBeautyPluginEffectsType.STICKER_WATERMELON, ZegoBeautyPluginEffectsType.STICKER_DEER,
            ZegoBeautyPluginEffectsType.STICKER_COOL_GIRL, ZegoBeautyPluginEffectsType.STICKER_CLOWN,
            ZegoBeautyPluginEffectsType.STICKER_CLAW_MACHINE, ZegoBeautyPluginEffectsType.STICKER_SAILOR_MOON);
        List<BeautyFeature> beauties = new ArrayList<>();
        for (ZegoBeautyPluginEffectsType beautyType : beautyTypes) {
            beauties.add(new BeautyFeature(beautyType, BeautyGroup.STICKERS, 0, 0, 0,
                new StickerEditor(getResourcePath(beautyType))));
        }
        return beauties;
    }

    public static List<BeautyFeature> getBackgrounds() {
        List<BeautyFeature> background = new ArrayList<>();
//        background.add(
//            new BeautyFeature(ZegoBeautyPluginEffectsType.BACKGROUND_GREEN_SCREEN_SEGMENTATION, BeautyGroup.BACKGROUND, new GreenScreenSegmentationEditor(
//                ZegoEffectsService.getResourceRootFolder() + File.separator + portraitSegmentationImagePath)));
        background.add(
            new BeautyFeature(ZegoBeautyPluginEffectsType.BACKGROUND_PORTRAIT_SEGMENTATION, BeautyGroup.BACKGROUND, 100,
                100, 100, new PortraitSegmentationEditor()));
        background.add(new BeautyFeature(ZegoBeautyPluginEffectsType.BACKGROUND_MOSAICING, BeautyGroup.BACKGROUND,
            new MosaicEditor()));
        background.add(new BeautyFeature(ZegoBeautyPluginEffectsType.BACKGROUND_GAUSSIAN_BLUR, BeautyGroup.BACKGROUND,
            new BlurEditor()));
        return background;
    }

    private static String getResourcePath(ZegoBeautyPluginEffectsType beautyType) {
        if (beautyType.name().endsWith("_RESET") || beautyType.name().endsWith("_NONE")) {
            return "";
        }
        String bundleName;
        if (beautyType.name().startsWith("BASIC_") || beautyType.name().startsWith("ADVANCED_") || beautyType.name()
            .startsWith("MAKEUP_") || beautyType.name().startsWith("STYLE_")) {
            bundleName = toCamelCase("beauty" + beautyType.name(), true, '_');
        } else {
            bundleName = toCamelCase(beautyType.name(), true, '_');
        }
        return ZegoEffectsService.getResourceRootFolder() + File.separator + "AdvancedResources" + File.separator
            + bundleName + ".bundle";
    }

    public static String toCamelCase(String str, final boolean capitalizeFirstLetter, final char... delimiters) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        str = str.toLowerCase();
        final int strLen = str.length();
        final int[] newCodePoints = new int[strLen];
        int outOffset = 0;
        final Set<Integer> delimiterSet = toDelimiterSet(delimiters);
        boolean capitalizeNext = capitalizeFirstLetter;
        for (int index = 0; index < strLen; ) {
            final int codePoint = str.codePointAt(index);

            if (delimiterSet.contains(codePoint)) {
                capitalizeNext = outOffset != 0;
                index += Character.charCount(codePoint);
            } else if (capitalizeNext || outOffset == 0 && capitalizeFirstLetter) {
                final int titleCaseCodePoint = Character.toTitleCase(codePoint);
                newCodePoints[outOffset++] = titleCaseCodePoint;
                index += Character.charCount(titleCaseCodePoint);
                capitalizeNext = false;
            } else {
                newCodePoints[outOffset++] = codePoint;
                index += Character.charCount(codePoint);
            }
        }

        return new String(newCodePoints, 0, outOffset);
    }

    private static Set<Integer> toDelimiterSet(final char[] delimiters) {
        final Set<Integer> delimiterHashSet = new HashSet<>();
        delimiterHashSet.add(Character.codePointAt(new char[]{' '}, 0));
        if (delimiters == null || delimiters.length == 0) {
            return delimiterHashSet;
        }

        for (int index = 0; index < delimiters.length; index++) {
            delimiterHashSet.add(Character.codePointAt(delimiters, index));
        }
        return delimiterHashSet;
    }
}
