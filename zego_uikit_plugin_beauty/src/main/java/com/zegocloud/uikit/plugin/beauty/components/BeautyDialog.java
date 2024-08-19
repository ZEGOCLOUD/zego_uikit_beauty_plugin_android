package com.zegocloud.uikit.plugin.beauty.components;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;
import com.google.android.material.tabs.TabLayout.Tab;
import com.tencent.mmkv.MMKV;
import com.zegocloud.uikit.plugin.adapter.plugins.beauty.ZegoBeautyPluginConfig;
import com.zegocloud.uikit.plugin.adapter.plugins.beauty.ZegoBeautyPluginEffectsType;
import com.zegocloud.uikit.plugin.beauty.R;
import com.zegocloud.uikit.plugin.beauty.ZegoUIKitBeautyPlugin;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyFeature;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyGroup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BeautyDialog extends BottomSheetDialog {

    private BeautyAdapter beautyAdapter;
    private SeekBarWithNumber seekBarWithNumber;
    private TabLayout tabLayout;

    private List<BeautyGroupItem> configGroupItems;

    // selected beauty features of groups,if group is makeup,don't draw selected circles
    private Map<BeautyGroup, Integer> groupSelectedPositions = new HashMap<>();

    // selected beauty features of makeup's subtypes,lipstick_xxx,blusher_xxx.etc.
    private Map<ZegoBeautyPluginEffectsType, Integer> subTypeSelectedPositions = new HashMap<>();

    // current show and operate subtypes;
    private ZegoBeautyPluginEffectsType currentSelectedSubType;

    public BeautyDialog(@NonNull Context context) {
        super(context, R.style.TransparentDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beauty_dialog_pop);

        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.dimAmount = 0.1f;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
        setCanceledOnTouchOutside(true);
        window.setBackgroundDrawable(new ColorDrawable());

        tabLayout = findViewById(R.id.beauty_tab_layout);

        tabLayout.setTabTextColors(Color.parseColor("#4dffffff"), Color.parseColor("#ffffffff"));

        List<BeautyFeatureItem> configFeatureItems = new ArrayList<>();
        ZegoBeautyPluginConfig beautyPluginConfig = ZegoUIKitBeautyPlugin.getInstance().getBeautyPluginConfig();
        if (beautyPluginConfig != null && beautyPluginConfig.effectsTypes != null) {
            configFeatureItems.addAll(
                BeautyDialogHelper.getBeautyFeatureItems(getContext(), beautyPluginConfig.effectsTypes));
        }

        configGroupItems = BeautyDialogHelper.getBeautyGroupItems(getContext(), configFeatureItems);

        for (BeautyGroupItem groupItem : configGroupItems) {
            Tab tab = tabLayout.newTab();
            TextView customView = new TextView(getContext());
            customView.setGravity(Gravity.CENTER);
            customView.setText(groupItem.groupName);
            customView.setTextColor(Color.parseColor("#4dffffff"));
            tab.setCustomView(customView);
            tab.setTag(groupItem);
            tabLayout.addTab(tab);
        }

        View featureRoot = findViewById(R.id.layout_feature_root);
        if (beautyPluginConfig != null && beautyPluginConfig.uiConfig != null) {
            if (beautyPluginConfig.uiConfig.backgroundColor != 0) {
                featureRoot.setBackgroundColor(beautyPluginConfig.uiConfig.backgroundColor);
            }
            if (beautyPluginConfig.uiConfig.selectedTabIndicatorColor != 0) {
                tabLayout.setSelectedTabIndicatorColor(beautyPluginConfig.uiConfig.selectedTabIndicatorColor);
            }
        }

        tabLayout.addOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                updateTabTitle(tab, true);

                BeautyGroupItem groupItem = (BeautyGroupItem) tab.getTag();
                if (groupItem.beautyGroup == BeautyGroup.MAKEUPS) {
                    List<BeautyFeatureItem> makeupItems = new ArrayList<>();
                    for (BeautyGroupItem configGroupItem : configGroupItems) {
                        if (configGroupItem.beautyGroup == BeautyGroup.MAKEUPS) {
                            for (BeautyFeatureItem beautyFeatureItem : configGroupItem.beautyFeatureItems) {
                                if (beautyFeatureItem.beautyFeature.getParentType() == null) {
                                    makeupItems.add(beautyFeatureItem);
                                }
                            }
                        }
                    }
                    beautyAdapter.setBeautyItems(makeupItems);
                } else {
                    beautyAdapter.setBeautyItems(groupItem.beautyFeatureItems);
                }
                if (groupItem.beautyGroup != BeautyGroup.MAKEUPS) {
                    Integer selectedItem = groupSelectedPositions.get(groupItem.beautyGroup);
                    if (selectedItem != null) {
                        beautyAdapter.setSelectedItemIndex(selectedItem);
                        seekBarWithNumber.setVisibility(View.VISIBLE);
                        BeautyFeatureItem featureItem = groupItem.beautyFeatureItems.get(selectedItem);
                        ZegoBeautyPluginEffectsType beautyType = featureItem.beautyFeature.getBeautyType();
                        int value = ZegoUIKitBeautyPlugin.getInstance().getBeautyFeatureValue(beautyType);
                        seekBarWithNumber.setProgress(value);
                    } else {
                        beautyAdapter.removeSelectedItem();
                        seekBarWithNumber.setVisibility(View.INVISIBLE);
                    }
                } else {
                    List<Integer> showDots = new ArrayList<>();
                    if (!subTypeSelectedPositions.isEmpty()) {
                        for (BeautyGroupItem configGroupItem : configGroupItems) {
                            if (configGroupItem.beautyGroup == BeautyGroup.MAKEUPS) {
                                for (BeautyFeatureItem beautyFeatureItem : configGroupItem.beautyFeatureItems) {
                                    BeautyFeature beautyFeature = beautyFeatureItem.beautyFeature;
                                    if (beautyFeature.getParentType() == null) {
                                        if (subTypeSelectedPositions.containsKey(beautyFeature.getBeautyType())) {
                                            showDots.add(configGroupItem.beautyFeatureItems.indexOf(beautyFeatureItem));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    beautyAdapter.removeSelectedItem();
                    beautyAdapter.showDot(showDots);
                    seekBarWithNumber.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onTabUnselected(Tab tab) {
                updateTabTitle(tab, false);
            }

            @Override
            public void onTabReselected(Tab tab) {
            }
        });

        seekBarWithNumber = findViewById(R.id.seekBarWithNumber);
        if (beautyPluginConfig != null && beautyPluginConfig.uiConfig != null) {
            if (beautyPluginConfig.uiConfig.seekBarTextSize != 0) {
                seekBarWithNumber.setTextSize(beautyPluginConfig.uiConfig.seekBarTextSize);
            }
            if (beautyPluginConfig.uiConfig.seekBarTextColor != 0) {
                seekBarWithNumber.setTextColor(beautyPluginConfig.uiConfig.seekBarTextColor);
            }
            if (beautyPluginConfig.uiConfig.seekBarTextBackground != null) {
                seekBarWithNumber.setTextBackground(beautyPluginConfig.uiConfig.seekBarTextBackground);
            }
            if (beautyPluginConfig.uiConfig.seekBarProgressDrawable != null) {
                seekBarWithNumber.setProgressDrawable(beautyPluginConfig.uiConfig.seekBarProgressDrawable);
            }
            if (beautyPluginConfig.uiConfig.seekBarThumbDrawable != null) {
                seekBarWithNumber.setThumb(beautyPluginConfig.uiConfig.seekBarThumbDrawable);
            }
        }
        seekBarWithNumber.setVisibility(View.INVISIBLE);
        seekBarWithNumber.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                if (currentSelectedSubType != null) {
                    ZegoUIKitBeautyPlugin.getInstance().setBeautyFeatureValue(currentSelectedSubType, progress);
                } else {
                    Tab selectedTab = tabLayout.getTabAt(tabLayout.getSelectedTabPosition());
                    BeautyGroupItem groupItem = (BeautyGroupItem) selectedTab.getTag();
                    Integer selectedItem = groupSelectedPositions.get(groupItem.beautyGroup);
                    if (selectedItem != null) {
                        BeautyFeature beautyFeature = groupItem.beautyFeatureItems.get(selectedItem).beautyFeature;
                        ZegoUIKitBeautyPlugin.getInstance()
                            .setBeautyFeatureValue(beautyFeature.getBeautyType(), progress);
                    }
                }
            }
        });

        TextView beautySubTypeTitleBack = findViewById(R.id.beauty_subtype_title);
        if (beautyPluginConfig != null && beautyPluginConfig.uiConfig != null) {
            if (beautyPluginConfig.uiConfig.backIcon != null) {
                beautySubTypeTitleBack.setCompoundDrawablesWithIntrinsicBounds(beautyPluginConfig.uiConfig.backIcon,
                    null, null, null);
            }
        }

        beautySubTypeTitleBack.setOnClickListener(v -> {
            beautySubTypeTitleBack.setVisibility(View.GONE);
            tabLayout.setVisibility(View.VISIBLE);
            List<BeautyFeatureItem> makeupItems = new ArrayList<>();
            for (BeautyGroupItem configGroupItem : configGroupItems) {
                if (configGroupItem.beautyGroup == BeautyGroup.MAKEUPS) {
                    for (BeautyFeatureItem beautyFeatureItem : configGroupItem.beautyFeatureItems) {
                        if (beautyFeatureItem.beautyFeature.getParentType() == null) {
                            makeupItems.add(beautyFeatureItem);
                        }
                    }
                }
            }
            beautyAdapter.setBeautyItems(makeupItems);
            beautyAdapter.removeSelectedItem();
            seekBarWithNumber.setVisibility(View.INVISIBLE);
            List<Integer> showDots = new ArrayList<>();
            if (!subTypeSelectedPositions.isEmpty()) {
                for (BeautyGroupItem configGroupItem : configGroupItems) {
                    if (configGroupItem.beautyGroup == BeautyGroup.MAKEUPS) {
                        for (BeautyFeatureItem beautyFeatureItem : configGroupItem.beautyFeatureItems) {
                            BeautyFeature beautyFeature = beautyFeatureItem.beautyFeature;
                            if (beautyFeature.getParentType() == null) {
                                if (subTypeSelectedPositions.containsKey(beautyFeature.getBeautyType())) {
                                    showDots.add(configGroupItem.beautyFeatureItems.indexOf(beautyFeatureItem));
                                }
                            }
                        }
                    }
                }
            }
            beautyAdapter.showDot(showDots);
        });

        beautyAdapter = new BeautyAdapter();
        Tab selectedTab = tabLayout.getTabAt(tabLayout.getSelectedTabPosition());
        updateTabTitle(selectedTab, true);
        BeautyGroupItem groupItem = (BeautyGroupItem) selectedTab.getTag();
        if (groupItem.beautyGroup == BeautyGroup.MAKEUPS) {
            List<BeautyFeatureItem> makeupItems = new ArrayList<>();
            for (BeautyGroupItem configGroupItem : configGroupItems) {
                if (configGroupItem.beautyGroup == BeautyGroup.MAKEUPS) {
                    for (BeautyFeatureItem beautyFeatureItem : configGroupItem.beautyFeatureItems) {
                        if (beautyFeatureItem.beautyFeature.getParentType() == null) {
                            makeupItems.add(beautyFeatureItem);
                        }
                    }
                }
            }
            beautyAdapter.setBeautyItems(makeupItems);
        } else {
            beautyAdapter.setBeautyItems(groupItem.beautyFeatureItems);
        }

        MMKV mmkv = MMKV.mmkvWithID("beauty_selected");
        if (beautyPluginConfig != null) {
            if (beautyPluginConfig.saveLastBeautyParam) {
                String current = mmkv.getString("current", null);
                if (current != null) {
                    currentSelectedSubType = ZegoBeautyPluginEffectsType.getByName(current);
                } else {
                    currentSelectedSubType = null;
                }
                for (String key : mmkv.allKeys()) {
                    if (!"current".equals(key)) {
                        ZegoBeautyPluginEffectsType type = ZegoBeautyPluginEffectsType.getByName(key);
                        if (type != null) {
                            subTypeSelectedPositions.put(type, mmkv.getInt(type.name(), 0));
                        } else {
                            BeautyGroup group = BeautyGroup.getByName(key);
                            if (group != null) {
                                groupSelectedPositions.put(group, mmkv.getInt(group.name(), 0));
                            }
                        }
                    }
                }
            } else {
                subTypeSelectedPositions.clear();
                groupSelectedPositions.clear();
                currentSelectedSubType = null;
                ZegoUIKitBeautyPlugin.getInstance().resetBeautyValueToDefault(null);
            }
        }

        RecyclerView recyclerviewItems = findViewById(R.id.recyclerview_items);
        recyclerviewItems.setAdapter(beautyAdapter);
        recyclerviewItems.setLayoutManager(
            new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerviewItems.addOnItemTouchListener(new OnRecyclerViewItemTouchListener(recyclerviewItems) {
            @Override
            public void onItemClick(ViewHolder vh) {
                int position = vh.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    BeautyFeatureItem clickItem = beautyAdapter.getBeautyItem(position);
                    BeautyFeature clickFeature = clickItem.beautyFeature;
                    BeautyGroup clickFeatureParentGroup = clickFeature.getParentGroup();
                    ZegoBeautyPluginEffectsType clickFeatureParentType = clickFeature.getParentType();
                    MMKV mmkv = MMKV.mmkvWithID("beauty_selected");
                    if (position == 0) {
                        beautyAdapter.removeSelectedItem();
                        seekBarWithNumber.setVisibility(View.INVISIBLE);
                        currentSelectedSubType = null;
                        mmkv.remove("current");
                        // if is subTypeFeatureItems, ...
                        if (clickFeatureParentGroup == BeautyGroup.MAKEUPS && clickFeatureParentType != null) {
                            subTypeSelectedPositions.remove(clickFeatureParentType);
                            mmkv.remove(clickFeatureParentType.name());
                            BeautyFeature parentFeature = ZegoUIKitBeautyPlugin.getInstance()
                                .getBeautyFeature(clickFeatureParentType);
                            for (ZegoBeautyPluginEffectsType subType : parentFeature.getSubTypes()) {
                                ZegoUIKitBeautyPlugin.getInstance().resetBeautyValueToDefault(subType);
                            }
                            ZegoUIKitBeautyPlugin.getInstance()
                                .enableBeautyFeature(clickFeature.getParentType(), false);
                        } else {
                            groupSelectedPositions.remove(clickFeatureParentGroup);
                            mmkv.remove(clickFeatureParentGroup.name());
                            if (clickFeatureParentGroup == BeautyGroup.MAKEUPS) {
                                resetMakeups();
                            } else if (clickFeatureParentGroup == BeautyGroup.BASIC
                                || clickFeatureParentGroup == BeautyGroup.ADVANCED) {
                                resetBeautyFeatures(clickFeatureParentGroup);
                            } else if (clickFeatureParentGroup == BeautyGroup.BACKGROUND) {
                                ZegoUIKitBeautyPlugin.getInstance().removeBackgrounds();
                            } else {
                                // filter,sticker,style-makeup
                                noneBeautyFeatures(clickFeatureParentGroup);
                            }
                        }
                    } else {
                        // if is subTypeFeatureItems,click to show seekbar to modify value
                        if (clickFeatureParentGroup == BeautyGroup.MAKEUPS && clickFeatureParentType != null) {
                            // click subType,need reset style makeup
                            noneBeautyFeatures(BeautyGroup.STYLE_MAKEUP);

                            currentSelectedSubType = clickFeature.getBeautyType();
                            mmkv.putString("current", currentSelectedSubType.name());
                            seekBarWithNumber.setVisibility(View.VISIBLE);
                            seekBarWithNumber.setOffsetValue(clickFeature.getMinValue());
                            seekBarWithNumber.setMax(clickFeature.getMaxValue());
                            ZegoUIKitBeautyPlugin.getInstance().enableBeautyFeature(currentSelectedSubType, true);
                            int value = ZegoUIKitBeautyPlugin.getInstance()
                                .getBeautyFeatureValue(currentSelectedSubType);
                            ZegoUIKitBeautyPlugin.getInstance().setBeautyFeatureValue(currentSelectedSubType, value);
                            seekBarWithNumber.setProgress(value);
                            beautyAdapter.setSelectedItemIndex(position);

                            // if click lipstick_xxx, subTypeSelectedPositions stores BeautyType.MAKEUP_LIPSTICK
                            // if click blusher_xxx, subTypeSelectedPositions stores BeautyType.MAKEUP_BLUSHER
                            subTypeSelectedPositions.put(clickFeatureParentType, position);
                            mmkv.putInt(clickFeatureParentType.name(), position);
                        } else {
                            currentSelectedSubType = null;
                            mmkv.remove("current");
                            // when select STICKERS , unselect STYLE_MAKEUP
                            if (clickFeatureParentGroup == BeautyGroup.STICKERS) {
                                noneBeautyFeatures(BeautyGroup.STYLE_MAKEUP);
                            }
                            // when select STYLE_MAKEUP, unselect  MAKEUPS | STICKERS
                            if (clickFeatureParentGroup == BeautyGroup.STYLE_MAKEUP) {
                                resetMakeups();
                                noneBeautyFeatures(BeautyGroup.STICKERS);
                            }

                            if (clickFeatureParentGroup != BeautyGroup.MAKEUPS) {
                                groupSelectedPositions.put(clickFeatureParentGroup, position);
                                if (clickFeatureParentGroup != BeautyGroup.STICKERS) {
                                    // sticker ,not saved
                                    mmkv.putInt(clickFeatureParentGroup.name(), position);
                                }

                                beautyAdapter.setSelectedItemIndex(position);
                                ZegoBeautyPluginEffectsType beautyType = clickFeature.getBeautyType();
                                ZegoUIKitBeautyPlugin.getInstance().enableBeautyFeature(beautyType, true);
                                if (clickFeature.getMaxValue() == clickFeature.getMinValue()) {
                                    // no value to adjust, stickers for example.
                                    seekBarWithNumber.setVisibility(View.INVISIBLE);
                                } else {
                                    seekBarWithNumber.setOffsetValue(clickFeature.getMinValue());
                                    seekBarWithNumber.setMax(clickFeature.getMaxValue());
                                    int value = ZegoUIKitBeautyPlugin.getInstance().getBeautyFeatureValue(beautyType);
                                    seekBarWithNumber.setProgress(value);
                                    ZegoUIKitBeautyPlugin.getInstance().setBeautyFeatureValue(beautyType, value);
                                    seekBarWithNumber.setVisibility(View.VISIBLE);
                                }
                            } else {
                                // click makeup types
                                // fill with subType feature items
                                List<BeautyFeatureItem> subTypeFeatureItems = new ArrayList<>();
                                for (BeautyFeatureItem item : configFeatureItems) {
                                    if (item.beautyFeature.getParentType() == clickFeature.getBeautyType()) {
                                        subTypeFeatureItems.add(item);
                                    }
                                }
                                beautyAdapter.setBeautyItems(subTypeFeatureItems);

                                // show subType feature title
                                beautySubTypeTitleBack.setVisibility(View.VISIBLE);
                                beautySubTypeTitleBack.setText(clickItem.name);
                                tabLayout.setVisibility(View.INVISIBLE);

                                Integer selectedSubTypeIndex = subTypeSelectedPositions.get(
                                    clickFeature.getBeautyType());
                                if (selectedSubTypeIndex != null) {
                                    //already has selected subType,need reset styleMakeup
                                    noneBeautyFeatures(BeautyGroup.STYLE_MAKEUP);

                                    beautyAdapter.setSelectedItemIndex(selectedSubTypeIndex);
                                    seekBarWithNumber.setVisibility(View.VISIBLE);
                                    seekBarWithNumber.setOffsetValue(clickFeature.getMinValue());
                                    seekBarWithNumber.setMax(clickFeature.getMaxValue());
                                    ZegoBeautyPluginEffectsType beautyType = clickFeature.getSubTypes()
                                        .get(selectedSubTypeIndex);
                                    int value = ZegoUIKitBeautyPlugin.getInstance().getBeautyFeatureValue(beautyType);
                                    seekBarWithNumber.setProgress(value);
                                } else {
                                    beautyAdapter.removeSelectedItem();
                                    seekBarWithNumber.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private void updateTabTitle(Tab tab, boolean selected) {
        ZegoBeautyPluginConfig beautyPluginConfig = ZegoUIKitBeautyPlugin.getInstance().getBeautyPluginConfig();
        TextView customView = (TextView) tab.getCustomView();

        if (selected) {
            customView.setTextColor(Color.WHITE);
            if (beautyPluginConfig != null && beautyPluginConfig.uiConfig != null) {
                if (beautyPluginConfig.uiConfig.selectedHeaderTitleTextColor != 0) {
                    customView.setTextColor(beautyPluginConfig.uiConfig.selectedHeaderTitleTextColor);
                }
                if (beautyPluginConfig.uiConfig.selectedHeaderTitleTextSize != 0) {
                    customView.setTextSize(beautyPluginConfig.uiConfig.selectedHeaderTitleTextSize);
                }
            }
        } else {
            customView.setTextColor(Color.parseColor("#4dffffff"));
            if (beautyPluginConfig != null && beautyPluginConfig.uiConfig != null) {
                if (beautyPluginConfig.uiConfig.normalHeaderTitleTextColor != 0) {
                    customView.setTextColor(beautyPluginConfig.uiConfig.normalHeaderTitleTextColor);
                }
                if (beautyPluginConfig.uiConfig.normalHeaderTitleTextSize != 0) {
                    customView.setTextSize(beautyPluginConfig.uiConfig.normalHeaderTitleTextSize);
                }
            }
        }
    }

    private void resetBeautyFeatures(BeautyGroup beautyGroup) {
        List<BeautyFeature> beautyFeatures = ZegoUIKitBeautyPlugin.getInstance().getGroupFeatures(beautyGroup);
        for (BeautyFeature beautyFeature : beautyFeatures) {
            if (beautyGroup == BeautyGroup.MAKEUPS) {
                if (beautyFeature.getSubTypes() != null) {
                    for (ZegoBeautyPluginEffectsType subType : beautyFeature.getSubTypes()) {
                        int value = ZegoUIKitBeautyPlugin.getInstance().getBeautyFeatureValue(subType);
                        if (value != beautyFeature.getDefaultValue()) {
                            ZegoUIKitBeautyPlugin.getInstance().resetBeautyValueToDefault(subType);
                        }
                    }
                }
            } else {
                int value = ZegoUIKitBeautyPlugin.getInstance().getBeautyFeatureValue(beautyFeature.getBeautyType());
                if (value != beautyFeature.getDefaultValue()) {
                    ZegoUIKitBeautyPlugin.getInstance().resetBeautyValueToDefault(beautyFeature.getBeautyType());
                }
            }
        }
    }

    private void noneBeautyFeatures(BeautyGroup beautyGroup) {
        Integer selectedStyleMakeupIndex = groupSelectedPositions.get(beautyGroup);
        if (selectedStyleMakeupIndex != null) {
            groupSelectedPositions.remove(beautyGroup);
            MMKV mmkv = MMKV.mmkvWithID("beauty_selected");
            mmkv.remove(beautyGroup.name());
        }
        List<BeautyFeature> beautyFeatures = ZegoUIKitBeautyPlugin.getInstance().getGroupFeatures(beautyGroup);
        for (BeautyFeature beautyFeature : beautyFeatures) {
            ZegoUIKitBeautyPlugin.getInstance().enableBeautyFeature(beautyFeature.getBeautyType(), false);
        }
    }

    private void resetMakeups() {
        MMKV mmkv = MMKV.mmkvWithID("beauty_selected");
        Integer selectedMakeupIndex = groupSelectedPositions.get(BeautyGroup.MAKEUPS);
        if (selectedMakeupIndex != null) {
            groupSelectedPositions.remove(BeautyGroup.MAKEUPS);
            mmkv.remove(BeautyGroup.MAKEUPS.name());
        }
        for (Entry<ZegoBeautyPluginEffectsType, Integer> entry : subTypeSelectedPositions.entrySet()) {
            mmkv.remove(entry.getKey().name());
        }
        subTypeSelectedPositions.clear();
        currentSelectedSubType = null;
        mmkv.remove("current");
        beautyAdapter.showDot(new ArrayList<>());

        resetBeautyFeatures(BeautyGroup.MAKEUPS);
        List<BeautyFeature> beautyFeatures = ZegoUIKitBeautyPlugin.getInstance().getGroupFeatures(BeautyGroup.MAKEUPS);
        for (BeautyFeature beautyFeature : beautyFeatures) {
            ZegoUIKitBeautyPlugin.getInstance().enableBeautyFeature(beautyFeature.getBeautyType(), false);
        }
    }
}
