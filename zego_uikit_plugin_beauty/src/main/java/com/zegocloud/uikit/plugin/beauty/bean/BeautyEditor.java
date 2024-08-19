package com.zegocloud.uikit.plugin.beauty.bean;

import android.text.TextUtils;
import com.zegocloud.uikit.plugin.adapter.plugins.beauty.ZegoBeautyPluginConfig;
import com.zegocloud.uikit.plugin.beauty.ZegoEffectsService;
import com.zegocloud.uikit.plugin.beauty.ZegoUIKitBeautyPlugin;
import im.zego.effects.ZegoEffects;
import im.zego.effects.entity.ZegoEffectsBigEyesParam;
import im.zego.effects.entity.ZegoEffectsBlurParam;
import im.zego.effects.entity.ZegoEffectsBlusherParam;
import im.zego.effects.entity.ZegoEffectsCheekboneSlimmingParam;
import im.zego.effects.entity.ZegoEffectsChromaKeyParam;
import im.zego.effects.entity.ZegoEffectsColoredcontactsParam;
import im.zego.effects.entity.ZegoEffectsDarkCirclesRemovingParam;
import im.zego.effects.entity.ZegoEffectsEyelashesParam;
import im.zego.effects.entity.ZegoEffectsEyelinerParam;
import im.zego.effects.entity.ZegoEffectsEyesBrighteningParam;
import im.zego.effects.entity.ZegoEffectsEyeshadowParam;
import im.zego.effects.entity.ZegoEffectsFaceLiftingParam;
import im.zego.effects.entity.ZegoEffectsFaceShorteningParam;
import im.zego.effects.entity.ZegoEffectsFilterParam;
import im.zego.effects.entity.ZegoEffectsForeheadShorteningParam;
import im.zego.effects.entity.ZegoEffectsLipstickParam;
import im.zego.effects.entity.ZegoEffectsLongChinParam;
import im.zego.effects.entity.ZegoEffectsMakeupParam;
import im.zego.effects.entity.ZegoEffectsMandibleSlimmingParam;
import im.zego.effects.entity.ZegoEffectsMosaicParam;
import im.zego.effects.entity.ZegoEffectsNoseLengtheningParam;
import im.zego.effects.entity.ZegoEffectsNoseNarrowingParam;
import im.zego.effects.entity.ZegoEffectsRosyParam;
import im.zego.effects.entity.ZegoEffectsSharpenParam;
import im.zego.effects.entity.ZegoEffectsSmallMouthParam;
import im.zego.effects.entity.ZegoEffectsSmoothParam;
import im.zego.effects.entity.ZegoEffectsTeethWhiteningParam;
import im.zego.effects.entity.ZegoEffectsWhitenParam;
import im.zego.effects.entity.ZegoEffectsWrinklesRemovingParam;
import im.zego.effects.enums.ZegoEffectsMosaicType;
import im.zego.effects.enums.ZegoEffectsScaleMode;
import java.io.File;

public abstract class BeautyEditor {

    protected String path;

    public BeautyEditor() {

    }

    public BeautyEditor(String path) {
        this.path = path;
    }

    public void enable(boolean enable) {

    }

    public static ZegoEffects getZEGOEffects() {
        return ZegoUIKitBeautyPlugin.getInstance().getEffectSDK();
    }

    public void apply(int value) {

    }


    // beauty- basic
    public static class SmoothingEditor extends BeautyEditor {

        @Override
        public void enable(boolean enable) {
            getZEGOEffects().enableSmooth(enable);
        }

        @Override
        public void apply(int value) {
            ZegoEffectsSmoothParam skinSmoothingParam = new ZegoEffectsSmoothParam();
            skinSmoothingParam.intensity = value;
            getZEGOEffects().setSmoothParam(skinSmoothingParam);
        }
    }

    public static class SkinToneEditor extends BeautyEditor {

        @Override
        public void enable(boolean enable) {
            getZEGOEffects().enableWhiten(enable);
        }

        @Override
        public void apply(int value) {
            ZegoEffectsWhitenParam param = new ZegoEffectsWhitenParam();
            param.intensity = value;
            getZEGOEffects().setWhitenParam(param);
        }
    }


    public static class BlusherEditor extends BeautyEditor {

        @Override
        public void enable(boolean enable) {
            getZEGOEffects().enableRosy(enable);
        }

        @Override
        public void apply(int value) {
            ZegoEffectsRosyParam cheekBlusherParam = new ZegoEffectsRosyParam();
            cheekBlusherParam.intensity = value;
            getZEGOEffects().setRosyParam(cheekBlusherParam);
        }
    }

    public static class SharpeningEditor extends BeautyEditor {

        @Override
        public void enable(boolean enable) {
            getZEGOEffects().enableSharpen(enable);
        }

        @Override
        public void apply(int value) {
            ZegoEffectsSharpenParam imageSharpeningParam = new ZegoEffectsSharpenParam();
            imageSharpeningParam.intensity = value;
            getZEGOEffects().setSharpenParam(imageSharpeningParam);
        }
    }

    public static class WrinklesEditor extends BeautyEditor {

        @Override
        public void enable(boolean enable) {
            getZEGOEffects().enableWrinklesRemoving(enable);
        }

        @Override
        public void apply(int value) {
            ZegoEffectsWrinklesRemovingParam wrinklesRemovingParam = new ZegoEffectsWrinklesRemovingParam();
            wrinklesRemovingParam.intensity = value;
            getZEGOEffects().setWrinklesRemovingParam(wrinklesRemovingParam);
        }
    }

    public static class DarkCirclesEditor extends BeautyEditor {

        @Override
        public void enable(boolean enable) {
            getZEGOEffects().enableDarkCirclesRemoving(enable);
        }

        @Override
        public void apply(int value) {
            ZegoEffectsDarkCirclesRemovingParam param = new ZegoEffectsDarkCirclesRemovingParam();
            param.intensity = value;
            getZEGOEffects().setDarkCirclesRemovingParam(param);
        }
    }


    // beauty-advanced
    public static class FaceSlimmingEditor extends BeautyEditor {

        @Override
        public void enable(boolean enable) {
            getZEGOEffects().enableFaceLifting(true);
        }

        @Override
        public void apply(int value) {
            ZegoEffectsFaceLiftingParam faceSlimmingParam = new ZegoEffectsFaceLiftingParam();
            faceSlimmingParam.intensity = value;
            getZEGOEffects().setFaceLiftingParam(faceSlimmingParam);
        }
    }

    public static class EyesEnlargingEditor extends BeautyEditor {

        @Override
        public void enable(boolean enable) {
            getZEGOEffects().enableBigEyes(enable);
        }

        @Override
        public void apply(int value) {
            ZegoEffectsBigEyesParam eyesEnlargingParam = new ZegoEffectsBigEyesParam();
            eyesEnlargingParam.intensity = value;
            getZEGOEffects().setBigEyesParam(eyesEnlargingParam);
        }
    }

    public static class EyesBrighteningEditor extends BeautyEditor {

        @Override
        public void enable(boolean enable) {
            getZEGOEffects().enableEyesBrightening(enable);
        }

        @Override
        public void apply(int value) {
            ZegoEffectsEyesBrighteningParam eyesBrighteningParam = new ZegoEffectsEyesBrighteningParam();
            eyesBrighteningParam.intensity = value;
            getZEGOEffects().setEyesBrighteningParam(eyesBrighteningParam);
        }
    }

    public static class ChinLengtheningEditor extends BeautyEditor {

        @Override
        public void enable(boolean enable) {
            getZEGOEffects().enableLongChin(enable);
        }

        @Override
        public void apply(int value) {
            ZegoEffectsLongChinParam chinLengtheningParam = new ZegoEffectsLongChinParam();
            chinLengtheningParam.intensity = value;
            getZEGOEffects().setLongChinParam(chinLengtheningParam);
        }
    }

    public static class MouthReshapeEditor extends BeautyEditor {

        @Override
        public void enable(boolean enable) {
            getZEGOEffects().enableSmallMouth(enable);
        }

        @Override
        public void apply(int value) {
            ZegoEffectsSmallMouthParam smallMouthParam = new ZegoEffectsSmallMouthParam();
            smallMouthParam.intensity = value;
            getZEGOEffects().setSmallMouthParam(smallMouthParam);
        }
    }

    public static class TeethWhiteningEditor extends BeautyEditor {

        @Override
        public void enable(boolean enable) {
            getZEGOEffects().enableTeethWhitening(enable);
        }

        @Override
        public void apply(int value) {
            ZegoEffectsTeethWhiteningParam teethWhiteningParam = new ZegoEffectsTeethWhiteningParam();
            teethWhiteningParam.intensity = value;
            getZEGOEffects().setTeethWhiteningParam(teethWhiteningParam);
        }
    }

    public static class NoseSlimmingEditor extends BeautyEditor {

        @Override
        public void enable(boolean enable) {
            getZEGOEffects().enableNoseNarrowing(enable);
        }

        @Override
        public void apply(int value) {
            ZegoEffectsNoseNarrowingParam param = new ZegoEffectsNoseNarrowingParam();
            param.intensity = value;
            getZEGOEffects().setNoseNarrowingParam(param);
        }
    }

    public static class NoseLengtheningEditor extends BeautyEditor {

        @Override
        public void enable(boolean enable) {
            getZEGOEffects().enableNoseLengthening(enable);
        }

        @Override
        public void apply(int value) {
            ZegoEffectsNoseLengtheningParam param = new ZegoEffectsNoseLengtheningParam();
            param.intensity = value;
            getZEGOEffects().setNoseLengtheningParam(param);
        }
    }

    public static class FaceShorteningEditor extends BeautyEditor {

        @Override
        public void enable(boolean enable) {
            getZEGOEffects().enableFaceShortening(enable);
        }

        @Override
        public void apply(int value) {
            ZegoEffectsFaceShorteningParam param = new ZegoEffectsFaceShorteningParam();
            param.intensity = value;
            getZEGOEffects().setFaceShorteningParam(param);
        }
    }

    public static class MandibleSlimmingEditor extends BeautyEditor {

        @Override
        public void enable(boolean enable) {
            getZEGOEffects().enableMandibleSlimming(enable);
        }

        @Override
        public void apply(int value) {
            ZegoEffectsMandibleSlimmingParam param = new ZegoEffectsMandibleSlimmingParam();
            param.intensity = value;
            getZEGOEffects().setMandibleSlimmingParam(param);
        }
    }

    public static class CheekboneSlimmingEditor extends BeautyEditor {

        @Override
        public void enable(boolean enable) {
            getZEGOEffects().enableCheekboneSlimming(enable);
        }

        @Override
        public void apply(int value) {
            ZegoEffectsCheekboneSlimmingParam param = new ZegoEffectsCheekboneSlimmingParam();
            param.intensity = value;
            getZEGOEffects().setCheekboneSlimmingParam(param);
        }
    }

    public static class ForeheadSlimmingEditor extends BeautyEditor {

        @Override
        public void enable(boolean enable) {
            getZEGOEffects().enableForeheadShortening(enable);
        }

        @Override
        public void apply(int value) {
            ZegoEffectsForeheadShorteningParam param = new ZegoEffectsForeheadShorteningParam();
            param.intensity = value;
            getZEGOEffects().setForeheadShorteningParam(param);
        }
    }

    private static final String TAG = "BeautyEditor";
    // MARK: - Filters
    public static class FilterEditor extends BeautyEditor {

        public FilterEditor() {

        }

        public FilterEditor(String path) {
            super(path);
        }

        @Override
        public void enable(boolean enable) {
            if (enable) {
                getZEGOEffects().setFilter(path);
            } else {
                getZEGOEffects().setFilter(null);
            }
        }

        @Override
        public void apply(int value) {
            ZegoEffectsFilterParam param = new ZegoEffectsFilterParam();
            param.intensity = value;
            getZEGOEffects().setFilterParam(param);
        }
    }

    // MARK: - Makeup
    public static class LipstickEditor extends BeautyEditor {

        public LipstickEditor() {
        }

        public LipstickEditor(String path) {
            super(path);
        }

        @Override
        public void enable(boolean enable) {
            if (enable) {
                getZEGOEffects().setLipstick(path);
            } else {
                getZEGOEffects().setLipstick(null);
            }
        }

        @Override
        public void apply(int value) {
            ZegoEffectsLipstickParam param = new ZegoEffectsLipstickParam();
            param.intensity = value;
            getZEGOEffects().setLipstickParam(param);
        }
    }


    public static class BlusherMakeupEditor extends BeautyEditor {

        public BlusherMakeupEditor() {
        }

        public BlusherMakeupEditor(String path) {
            super(path);
        }

        @Override
        public void enable(boolean enable) {
            if (enable) {
                getZEGOEffects().setBlusher(path);
            } else {
                getZEGOEffects().setBlusher(null);
            }
        }

        @Override
        public void apply(int value) {
            ZegoEffectsBlusherParam param = new ZegoEffectsBlusherParam();
            param.intensity = value;
            getZEGOEffects().setBlusherParam(param);
        }
    }

    public static class EyelashesEditor extends BeautyEditor {

        public EyelashesEditor() {
        }

        public EyelashesEditor(String path) {
            super(path);
        }

        @Override
        public void enable(boolean enable) {
            if (enable) {
                getZEGOEffects().setEyelashes(path);
            } else {
                getZEGOEffects().setEyelashes(null);
            }
        }

        @Override
        public void apply(int value) {
            ZegoEffectsEyelashesParam param = new ZegoEffectsEyelashesParam();
            param.intensity = value;
            getZEGOEffects().setEyelashesParam(param);
        }
    }

    public static class EyelinerEditor extends BeautyEditor {

        public EyelinerEditor() {
        }

        public EyelinerEditor(String path) {
            super(path);
        }

        @Override
        public void enable(boolean enable) {
            if (enable) {
                getZEGOEffects().setEyeliner(path);
            } else {
                getZEGOEffects().setEyeliner(null);
            }
        }

        @Override
        public void apply(int value) {
            ZegoEffectsEyelinerParam param = new ZegoEffectsEyelinerParam();
            param.intensity = value;
            getZEGOEffects().setEyelinerParam(param);
        }
    }

    public static class EyeshadowEditor extends BeautyEditor {

        public EyeshadowEditor() {
        }

        public EyeshadowEditor(String path) {
            super(path);
        }

        @Override
        public void enable(boolean enable) {
            if (enable) {
                getZEGOEffects().setEyeshadow(path);
            } else {
                getZEGOEffects().setEyeshadow(null);
            }
        }

        @Override
        public void apply(int value) {
            ZegoEffectsEyeshadowParam param = new ZegoEffectsEyeshadowParam();
            param.intensity = value;
            getZEGOEffects().setEyeshadowParam(param);
        }
    }

    public static class ColoredContactsEditor extends BeautyEditor {

        public ColoredContactsEditor() {
        }

        public ColoredContactsEditor(String path) {
            super(path);
        }

        @Override
        public void enable(boolean enable) {
            if (enable) {
                getZEGOEffects().setColoredcontacts(path);
            } else {
                getZEGOEffects().setColoredcontacts(null);
            }
        }

        @Override
        public void apply(int value) {
            ZegoEffectsColoredcontactsParam param = new ZegoEffectsColoredcontactsParam();
            param.intensity = value;
            getZEGOEffects().setColoredcontactsParam(param);
        }
    }

    // MARK: - Style-Makeup
    public static class StyleMakeupEditor extends BeautyEditor {

        public StyleMakeupEditor() {

        }

        public StyleMakeupEditor(String path) {

            super(path);
        }

        @Override
        public void enable(boolean enable) {
            if (enable) {
                getZEGOEffects().setMakeup(path);
            } else {
                getZEGOEffects().setMakeup(null);
            }
        }

        @Override
        public void apply(int value) {
            ZegoEffectsMakeupParam param = new ZegoEffectsMakeupParam();
            param.intensity = value;
            getZEGOEffects().setMakeupParam(param);
        }
    }

    // MARK: - Stickers
    public static class StickerEditor extends BeautyEditor {

        public StickerEditor() {

        }

        public StickerEditor(String path) {

            super(path);
        }

        @Override
        public void enable(boolean enable) {
            if (enable) {
                getZEGOEffects().setPendant(path);
            } else {
                getZEGOEffects().setPendant(null);
            }
        }
    }

    // MARK: - Background
    public static class GreenScreenSegmentationEditor extends BeautyEditor {

        public GreenScreenSegmentationEditor(String path) {
            super(path);
        }

        @Override
        public void enable(boolean enable) {
            getZEGOEffects().enablePortraitSegmentation(false);
            getZEGOEffects().setChromaKeyBackgroundPath(path, ZegoEffectsScaleMode.SCALE_TO_FILL);
            getZEGOEffects().enableChromaKey(enable);
            getZEGOEffects().enableChromaKeyBackground(enable);

            //            getZEGOEffects().enableChromaKeyBackgroundBlur(enable);
        }

        @Override
        public void apply(int value) {
            //            ZegoEffectsBlurParam param = new ZegoEffectsBlurParam();
            //            param.intensity = value;
            //            getZEGOEffects().setChromaKeyBackgroundBlurParam(param);

            ZegoEffectsChromaKeyParam param = new ZegoEffectsChromaKeyParam();
            //            param.similarity = 0;
            //            param.smoothness = 80;
            param.opacity = 100 - value;
            //            param.borderSize = 1;
            getZEGOEffects().setChromaKeyParam(param);
        }
    }

    public static class PortraitSegmentationEditor extends BeautyEditor {

        public PortraitSegmentationEditor() {

        }

        @Override
        public void enable(boolean enable) {
            ZegoBeautyPluginConfig beautyPluginConfig = ZegoUIKitBeautyPlugin.getInstance().getBeautyPluginConfig();
            String parentPath = ZegoEffectsService.getResourceRootFolder() + File.separator + "BackgroundImages";
            path = parentPath + File.separator + "image1.jpg";
            if (beautyPluginConfig != null && !TextUtils.isEmpty(beautyPluginConfig.segmentationBackgroundImageName)) {
                path = parentPath + File.separator + beautyPluginConfig.segmentationBackgroundImageName;
            }
            getZEGOEffects().enableChromaKey(false);
            getZEGOEffects().setPortraitSegmentationBackgroundPath(path, ZegoEffectsScaleMode.SCALE_TO_FILL);
            getZEGOEffects().enablePortraitSegmentation(enable);
            getZEGOEffects().enablePortraitSegmentationBackground(enable);
        }
    }

    public static class MosaicEditor extends BeautyEditor {

        @Override
        public void enable(boolean enable) {
            getZEGOEffects().enableChromaKey(false);
            getZEGOEffects().enablePortraitSegmentation(enable);
            getZEGOEffects().enablePortraitSegmentationBackgroundMosaic(enable);
        }

        @Override
        public void apply(int value) {
            ZegoEffectsMosaicParam param = new ZegoEffectsMosaicParam();
            param.intensity = value;
            param.type = ZegoEffectsMosaicType.SQUARE;
            getZEGOEffects().setPortraitSegmentationBackgroundMosaicParam(param);
        }
    }

    public static class BlurEditor extends BeautyEditor {

        @Override
        public void enable(boolean enable) {
            getZEGOEffects().enableChromaKey(false);
            getZEGOEffects().enablePortraitSegmentation(enable);
            getZEGOEffects().enablePortraitSegmentationBackgroundBlur(enable);
        }

        @Override
        public void apply(int value) {
            ZegoEffectsBlurParam param = new ZegoEffectsBlurParam();
            param.intensity = value;
            getZEGOEffects().setPortraitSegmentationBackgroundBlurParam(param);
        }
    }

    public static class SimpleEditor extends BeautyEditor {

    }
}
