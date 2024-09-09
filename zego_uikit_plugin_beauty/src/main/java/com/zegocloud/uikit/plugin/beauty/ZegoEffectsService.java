package com.zegocloud.uikit.plugin.beauty;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.tencent.mmkv.MMKV;
import com.zegocloud.uikit.plugin.adapter.plugins.beauty.BeautyPluginLicenseSetter;
import com.zegocloud.uikit.plugin.adapter.plugins.beauty.IBeautyEventHandler;
import com.zegocloud.uikit.plugin.adapter.plugins.beauty.LicenceProvider;
import com.zegocloud.uikit.plugin.adapter.plugins.beauty.ZegoBeautyPluginEffectsType;
import com.zegocloud.uikit.plugin.adapter.plugins.beauty.ZegoBeautyPluginFaceDetectionResult;
import com.zegocloud.uikit.plugin.adapter.plugins.beauty.ZegoBeautyPluginRect;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.PortraitSegmentationEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyEditor.StickerEditor;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyFeature;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyGroup;
import com.zegocloud.uikit.plugin.beauty.net.IGetLicenseCallback;
import com.zegocloud.uikit.plugin.beauty.net.License;
import im.zego.effects.ZegoEffects;
import im.zego.effects.callback.ZegoEffectsEventHandler;
import im.zego.effects.entity.ZegoEffectsFaceDetectionResult;
import im.zego.effects.entity.ZegoEffectsRect;
import im.zego.effects.entity.ZegoEffectsVideoFrameParam;
import im.zego.effects.enums.ZegoEffectsScaleMode;
import im.zego.effects.enums.ZegoEffectsVideoFrameFormat;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import timber.log.Timber;

public class ZegoEffectsService {

    public final static String BACKEND_API_URL = "https://aieffects-api.zego.im/?Action=DescribeEffectsLicense";

    private ZegoEffects zegoEffects;

    private Map<BeautyGroup, List<BeautyFeature>> beautyFeaturesMap = new HashMap<>();
    private Map<ZegoBeautyPluginEffectsType, Integer> beautyParams = new HashMap<>();

    private static String resourceRootFolder = "";
    private IBeautyEventHandler eventHandler;
    private boolean enableFaceDetection;
    private LicenceProvider provider;
    private boolean saveLastBeautyParam;
    private MMKV mmkv;

    public static String getResourceRootFolder() {
        return resourceRootFolder;
    }

    public void init(Context context, long appID, String appSign, IGetLicenseCallback callback) {
        String cacheDir = context.getExternalFilesDir(null).getPath();
        String resourceFolderName = "BeautyResources";
        resourceRootFolder = cacheDir + File.separator + resourceFolderName;
        // step 1, setResources
        EffectSDKHelper.setResources(context, cacheDir, resourceFolderName);
        MMKV.initialize(context);

        if (!TextUtils.isEmpty(appSign)) {
            // step 2, getLicence
            EffectSDKHelper.getLicense(context, BACKEND_API_URL, appID, appSign, new IGetLicenseCallback() {
                @Override
                public void onGetLicense(int code, String message, License license) {
                    if (code == 0) {
                        initEffectsSDKInner(context, license.getLicense());
                    }
                    if (callback != null) {
                        callback.onGetLicense(code, message, license);
                    }
                    if (eventHandler != null) {
                        eventHandler.onInitResult(code, message);
                    }
                }
            });
        } else {
            if (provider != null) {
                provider.onLicenseRequired(new BeautyPluginLicenseSetter() {
                    @Override
                    public void setLicence(Context context, String license) {
                        initEffectsSDKInner(context, license);
                    }
                });
            } else {
                if (eventHandler != null) {
                    eventHandler.onInitResult(-1,
                        "Please init with appSign or set a LicenceProvider to pass a license");
                }
            }
        }
    }

    private static final String TAG = "ZegoEffectsService";

    private void initEffectsSDKInner(Context context, String license) {
        // step 3 create effect
        zegoEffects = ZegoEffects.create(license, context);
        zegoEffects.enableFaceDetection(enableFaceDetection);
        zegoEffects.setEventHandler(new ZegoEffectsEventHandler() {
            @Override
            public void onError(ZegoEffects effects, int errorCode, String desc) {
                super.onError(effects, errorCode, desc);
                Timber.d(
                    "initEffectsSDK onError() called with: = [" + "], errorCode = [" + errorCode + "], desc = [" + desc
                        + "]");
                if (eventHandler != null) {
                    eventHandler.onError(errorCode, desc);
                }
            }

            @Override
            public void onFaceDetectionResult(ZegoEffectsFaceDetectionResult[] results, ZegoEffects effects) {
                //                for (ZegoEffectsFaceDetectionResult result : results) {
                //                    Log.d(TAG,
                //                        "onFaceDetectionResult,results.size:" + results.length + ",rect : point(" + result.rect.x + ","
                //                            + result.rect.y + "),width:" + result.rect.width + ",height:" + result.rect.height
                //                            + ",getScore: " + result.getScore());
                //                }
                if (eventHandler != null) {
                    ZegoBeautyPluginFaceDetectionResult[] detectionResults = new ZegoBeautyPluginFaceDetectionResult[results.length];
                    for (int i = 0; i < results.length; i++) {
                        ZegoEffectsFaceDetectionResult result = results[i];
                        detectionResults[i] = new ZegoBeautyPluginFaceDetectionResult();
                        detectionResults[i].setScore(result.getScore());
                        ZegoEffectsRect rect = result.getRect();
                        ZegoBeautyPluginRect pluginRect = new ZegoBeautyPluginRect();
                        pluginRect.setX(rect.getX());
                        pluginRect.setY(rect.getY());
                        pluginRect.setWidth(rect.getWidth());
                        pluginRect.setHeight(rect.getHeight());
                        detectionResults[i].setRect(pluginRect);
                    }
                    eventHandler.onFaceDetectionResult(detectionResults);
                }
            }
        });
        // step 4 enableAbilities
        beautyFeaturesMap = EffectSDKHelper.getAllFeatures();
        mmkv = MMKV.mmkvWithID("beauty");
        if (saveLastBeautyParam) {
            String lastFilter = null;
            String lastStyle = null;
            for (String string : mmkv.allKeys()) {
                if ("last_filter".equals(string)) {
                    lastFilter = mmkv.getString(string, null);
                } else if ("last_style".equals(string)) {
                    lastStyle = mmkv.getString(string, null);
                } else {
                    int value = mmkv.decodeInt(string);
                    ZegoBeautyPluginEffectsType beautyType = ZegoBeautyPluginEffectsType.getByName(string);
                    BeautyFeature beautyFeature = getBeautyFeature(beautyType);
                    if (beautyFeature.getParentGroup() == BeautyGroup.FILTERS
                        || beautyFeature.getParentGroup() == BeautyGroup.STYLE_MAKEUP) {
                        // set later.
                    } else {
                        enableBeauty(beautyType, true);
                        beautyFeature.getEditor().apply(value);
                    }
                    beautyParams.put(beautyType, value);
                }
            }

            if (!TextUtils.isEmpty(lastStyle)) {
                ZegoBeautyPluginEffectsType lastStyleType = ZegoBeautyPluginEffectsType.getByName(lastStyle);
                enableBeauty(lastStyleType, true);
                setBeautyValue(lastStyleType, beautyParams.get(lastStyleType));
            }

            String finalLastFilter = lastFilter;
            new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!TextUtils.isEmpty(finalLastFilter)) {
                        ZegoBeautyPluginEffectsType lastFilterType = ZegoBeautyPluginEffectsType.getByName(
                            finalLastFilter);
                        enableBeauty(lastFilterType, true);
                        setBeautyValue(lastFilterType, beautyParams.get(lastFilterType));
                    }
                }
            },1000);

        } else {
            resetBeautyValueToDefault(null);
            mmkv.clear();
        }
    }

    public void removeBackgrounds() {
        if (zegoEffects == null) {
            return;
        }
        zegoEffects.setPortraitSegmentationBackgroundPath(null, ZegoEffectsScaleMode.ASPECT_FILL);
        zegoEffects.enablePortraitSegmentation(false);
        zegoEffects.enablePortraitSegmentationBackground(false);
        zegoEffects.enablePortraitSegmentationBackgroundMosaic(false);
        zegoEffects.enablePortraitSegmentationBackgroundBlur(false);
        zegoEffects.enableChromaKey(false);
        zegoEffects.enableChromaKeyBackground(false);
        zegoEffects.enableChromaKeyBackgroundBlur(false);
        zegoEffects.enableChromaKeyBackgroundMosaic(false);

        if (saveLastBeautyParam) {
            List<String> removeKeys = new ArrayList<>();
            for (String key : mmkv.allKeys()) {
                ZegoBeautyPluginEffectsType beautyType = ZegoBeautyPluginEffectsType.getByName(key);
                BeautyFeature beautyFeature = getBeautyFeature(beautyType);
                if (beautyFeature != null && beautyFeature.getParentGroup() == BeautyGroup.BACKGROUND) {
                    removeKeys.add(key);
                }
            }
            for (String removeKey : removeKeys) {
                mmkv.remove(removeKey);
            }
        }
    }

    public BeautyFeature getBeautyFeature(ZegoBeautyPluginEffectsType beautyType) {
        for (List<BeautyFeature> value : beautyFeaturesMap.values()) {
            for (BeautyFeature ability : value) {
                if (ability.getBeautyType() == beautyType) {
                    return ability;
                }
            }
        }
        return null;
    }

    public List<BeautyFeature> getGroupFeatures(BeautyGroup beautyGroup) {
        return beautyFeaturesMap.get(beautyGroup);
    }

    public void enableBeauty(ZegoBeautyPluginEffectsType beautyType, boolean enable) {
        if (zegoEffects == null) {
            return;
        }
        BeautyFeature beautyFeature = getBeautyFeature(beautyType);
        beautyFeature.getEditor().enable(enable);
        if (!enable) {
            beautyParams.remove(beautyType);
            mmkv.remove(beautyType.name());

            if (beautyType.name().equals(mmkv.getString("last_filter", null))) {
                mmkv.remove("last_filter");
            }
            if (beautyType.name().equals(mmkv.getString("last_style", null))) {
                mmkv.remove("last_style");
            }
        }
    }

    public void setBeautyValue(ZegoBeautyPluginEffectsType beautyType, int value) {
        if (zegoEffects == null) {
            return;
        }
        BeautyFeature beautyFeature = getBeautyFeature(beautyType);
        beautyFeature.getEditor().apply(value);
        beautyParams.put(beautyType, value);
        if (saveLastBeautyParam) {
            if (mmkv != null) {
                mmkv.encode(beautyType.toString(), value);
                if (beautyFeature.getParentGroup() == BeautyGroup.FILTERS) {
                    mmkv.putString("last_filter", beautyFeature.getBeautyType().name());
                }
                if (beautyFeature.getParentGroup() == BeautyGroup.STYLE_MAKEUP) {
                    mmkv.putString("last_style", beautyFeature.getBeautyType().name());
                }
            }
        }

    }

    public int getBeautyValue(ZegoBeautyPluginEffectsType beautyType) {
        Integer integer = beautyParams.get(beautyType);
        if (integer == null) {
            BeautyFeature beautyFeature = getBeautyFeature(beautyType);
            return beautyFeature.getDefaultValue();
        }
        return integer;
    }

    public void resetBeautyValueToDefault(ZegoBeautyPluginEffectsType beautyType) {
        if (zegoEffects == null) {
            return;
        }
        if (beautyType != null) {
            resetBeautyValueToDefaultInner(beautyType);
        } else {
            for (List<BeautyFeature> value : beautyFeaturesMap.values()) {
                for (BeautyFeature beautyFeature : value) {
                    resetBeautyValueToDefaultInner(beautyFeature.getBeautyType());
                }
            }
        }
    }

    private void resetBeautyValueToDefaultInner(ZegoBeautyPluginEffectsType beautyType) {
        BeautyFeature beautyFeature = getBeautyFeature(beautyType);
        beautyFeature.getEditor().apply(beautyFeature.getDefaultValue());
        beautyParams.remove(beautyType);
        if (saveLastBeautyParam && mmkv != null) {
            mmkv.remove(beautyType.toString());
        }
    }

    private void resetBeautyValueToNoneInner(ZegoBeautyPluginEffectsType beautyType) {
        BeautyFeature beautyFeature = getBeautyFeature(beautyType);
        if (beautyFeature.getEditor() instanceof StickerEditor
            || beautyFeature.getEditor() instanceof PortraitSegmentationEditor) {
            enableBeauty(beautyType, false);
        } else {
            setBeautyValue(beautyType, beautyFeature.getMinValue());
        }
    }


    public void initEnv(int captureWidth, int captureHeight) {
        if (zegoEffects == null) {
            return;
        }
        zegoEffects.initEnv(captureWidth, captureHeight);
    }

    public void uninitEnv() {
        if (zegoEffects == null) {
            return;
        }
        zegoEffects.uninitEnv();
    }

    public boolean isEffectSDKInit() {
        return zegoEffects != null;
    }

    public void saveLastBeautyParam(boolean saveLastBeautyParam) {
        this.saveLastBeautyParam = saveLastBeautyParam;
    }

    public boolean isLastBeautyParamSaved() {
        return saveLastBeautyParam;
    }

    public void enableFaceDetection(boolean enable) {
        enableFaceDetection = enable;
        if (zegoEffects == null) {
            return;
        }
        zegoEffects.enableFaceDetection(enable);
    }

    public ZegoEffects getEffectSDK() {
        return zegoEffects;
    }

    public int processTexture(int textureID, int width, int height) {
        if (zegoEffects == null) {
            return textureID;
        }
        ZegoEffectsVideoFrameParam effectsVideoFrameParam = new ZegoEffectsVideoFrameParam();
        effectsVideoFrameParam.format = ZegoEffectsVideoFrameFormat.RGBA32;
        effectsVideoFrameParam.width = width;
        effectsVideoFrameParam.height = height;
        return zegoEffects.processTexture(textureID, effectsVideoFrameParam);
    }

    public void resetAndDisableAllAbilities() {
        if (zegoEffects != null) {
            for (List<BeautyFeature> value : beautyFeaturesMap.values()) {
                for (BeautyFeature beautyFeature : value) {
                    BeautyEditor editor = beautyFeature.getEditor();
                    editor.apply(beautyFeature.getDefaultValue());
                    editor.enable(false);
                }
            }
            beautyParams.clear();
        }
    }

    public void unInit() {
        resetAndDisableAllAbilities();
        if (zegoEffects != null) {
            zegoEffects.destroy();
            zegoEffects = null;
        }
    }

    public void setEventHandler(IBeautyEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public void setLicenceProvider(LicenceProvider provider) {
        this.provider = provider;
    }

    public void resetBeautyValueToNone(ZegoBeautyPluginEffectsType beautyType) {
        if (zegoEffects == null) {
            return;
        }
        if (beautyType != null) {
            resetBeautyValueToNoneInner(beautyType);
        } else {
            for (List<BeautyFeature> value : beautyFeaturesMap.values()) {
                for (BeautyFeature beautyFeature : value) {
                    resetBeautyValueToNoneInner(beautyFeature.getBeautyType());
                }
            }
        }
    }
}
