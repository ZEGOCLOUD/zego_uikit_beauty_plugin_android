package com.zegocloud.uikit.plugin.beauty;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import com.zegocloud.uikit.plugin.adapter.plugins.ZegoPluginType;
import com.zegocloud.uikit.plugin.adapter.plugins.beauty.LicenceProvider;
import com.zegocloud.uikit.plugin.adapter.plugins.beauty.ZegoBeautyPluginConfig;
import com.zegocloud.uikit.plugin.adapter.plugins.beauty.ZegoBeautyPluginEffectsType;
import com.zegocloud.uikit.plugin.adapter.plugins.beauty.ZegoBeautyPluginInitCallback;
import com.zegocloud.uikit.plugin.adapter.plugins.beauty.ZegoBeautyPluginProtocol;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyFeature;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyGroup;
import com.zegocloud.uikit.plugin.beauty.components.BeautyDialog;
import com.zegocloud.uikit.plugin.beauty.net.IGetLicenseCallback;
import com.zegocloud.uikit.plugin.beauty.net.License;
import im.zego.effects.ZegoEffects;
import java.util.List;

public class ZegoUIKitBeautyPlugin implements ZegoBeautyPluginProtocol {

    private static ZegoUIKitBeautyPlugin sInstance;

    private ZegoUIKitBeautyPlugin() {
    }

    public static ZegoUIKitBeautyPlugin getInstance() {
        synchronized (ZegoUIKitBeautyPlugin.class) {
            if (sInstance == null) {
                sInstance = new ZegoUIKitBeautyPlugin();
            }
            return sInstance;
        }
    }

    private ZegoEffectsService effectsService = new ZegoEffectsService();
    private ZegoBeautyPluginConfig beautyPluginConfig;

    @Override
    public ZegoPluginType getPluginType() {
        return ZegoPluginType.BEAUTY;
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public void init(Application application, long appID, String appSign, ZegoBeautyPluginInitCallback callback) {
        effectsService.init(application, appID, appSign, new IGetLicenseCallback() {
            @Override
            public void onGetLicense(int code, String message, License license) {
                if (callback != null) {
                    callback.onResult(code, message);
                }
            }
        });
    }

    @Override
    public void setLicenceProvider(LicenceProvider provider) {
        effectsService.setLicenceProvider(provider);
    }

    @Override
    public void unInit() {
        effectsService.unInit();
    }

    @Override
    public void initEnv(int captureWidth, int captureHeight) {
        effectsService.initEnv(captureWidth, captureHeight);
    }

    @Override
    public void unInitEnv() {
        effectsService.uninitEnv();
    }

    @Override
    public boolean isEffectSDKInit() {
        return effectsService.isEffectSDKInit();
    }

    @Override
    public void setZegoBeautyPluginConfig(ZegoBeautyPluginConfig config) {
        this.beautyPluginConfig = config;
        if (config == null) {
            effectsService.enableFaceDetection(false);
        } else {
            effectsService.enableFaceDetection(config.enableFaceDetection);
            effectsService.setEventHandler(config.beautyEventHandler);
        }
    }

    public ZegoBeautyPluginConfig getBeautyPluginConfig() {
        return beautyPluginConfig;
    }

    @Override
    public int processTexture(int textureID, int width, int height) {
        return effectsService.processTexture(textureID, width, height);
    }

    public void removeBackgrounds() {
        effectsService.removeBackgrounds();
    }

    @Override
    public void resetAllFeatures() {
        effectsService.resetAndDisableAllAbilities();
    }

    @Override
    public Dialog getBeautyDialog(Context context) {
        if (effectsService.isEffectSDKInit()) {
            return new BeautyDialog(context);
        } else {
            return null;
        }
    }

    public BeautyFeature getBeautyFeature(ZegoBeautyPluginEffectsType beautyType) {
        return effectsService.getBeautyFeature(beautyType);
    }

    public ZegoEffects getEffectSDK() {
        return effectsService.getEffectSDK();
    }

    public void enableBeautyFeature(ZegoBeautyPluginEffectsType beautyType, boolean enable) {
        effectsService.enableBeauty(beautyType, enable);
    }

    public int getBeautyFeatureValue(ZegoBeautyPluginEffectsType beautyType) {
        return effectsService.getBeautyValue(beautyType);
    }

    public void setBeautyFeatureValue(ZegoBeautyPluginEffectsType beautyType, int value) {
        effectsService.setBeautyValue(beautyType, value);
    }

    public void resetBeautyValue(ZegoBeautyPluginEffectsType beautyType) {
        effectsService.resetBeautyValue(beautyType);
    }

    public List<BeautyFeature> getGroupFeatures(BeautyGroup beautyGroup) {
        return effectsService.getGroupFeatures(beautyGroup);
    }
}
