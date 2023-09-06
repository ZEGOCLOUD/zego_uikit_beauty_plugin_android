package com.zegocloud.uikit.plugin.beauty.components;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.AbsoluteCornerSize;
import com.google.android.material.shape.RelativeCornerSize;
import com.google.android.material.shape.RoundedCornerTreatment;
import com.zegocloud.uikit.plugin.adapter.plugins.beauty.ZegoBeautyPluginConfig;
import com.zegocloud.uikit.plugin.beauty.R;
import com.zegocloud.uikit.plugin.beauty.ZegoUIKitBeautyPlugin;
import com.zegocloud.uikit.plugin.beauty.ZegoUtil;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyFeature;
import com.zegocloud.uikit.plugin.beauty.bean.BeautyGroup;
import java.util.ArrayList;
import java.util.List;

public class BeautyAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<BeautyFeatureItem> beautyFeatureItems = new ArrayList<>();
    private int selectedItemIndex = 0;
    private List<Integer> dotIndexes = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.beauty_item_ability, null, false);
        DisplayMetrics displayMetrics = parent.getContext().getResources().getDisplayMetrics();
        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, displayMetrics);
        LayoutParams layoutParams = new LayoutParams((int) width, LayoutParams.WRAP_CONTENT);
        inflate.setLayoutParams(layoutParams);
        return new ViewHolder(inflate) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShapeableImageView icon = holder.itemView.findViewById(R.id.item_beauty_icon);
        TextView name = holder.itemView.findViewById(R.id.item_beauty_name);
        ImageFilterView selectedHint = holder.itemView.findViewById(R.id.item_selected_hint);
        selectedHint.setBackgroundColor(Color.parseColor("#ffa653ff"));
        if (dotIndexes != null && !dotIndexes.isEmpty()) {
            if (dotIndexes.contains(position)) {
                selectedHint.setVisibility(View.VISIBLE);
            } else {
                selectedHint.setVisibility(View.INVISIBLE);
            }
        } else {
            selectedHint.setVisibility(View.INVISIBLE);
        }

        int[][] states = new int[][]{new int[]{android.R.attr.state_selected},
            new int[]{-android.R.attr.state_selected},};
        int normalTextColor = Color.parseColor("#ffcccccc");
        int selectedTextColor = Color.parseColor("#ffa653ff");
        ZegoBeautyPluginConfig beautyPluginConfig = ZegoUIKitBeautyPlugin.getInstance().getBeautyPluginConfig();
        if (beautyPluginConfig != null && beautyPluginConfig.uiConfig != null) {
            if (beautyPluginConfig.uiConfig.normalTextSize != 0) {
                name.setTextSize(beautyPluginConfig.uiConfig.normalTextSize);
            }
            if (beautyPluginConfig.uiConfig.normalTextColor != 0) {
                normalTextColor = beautyPluginConfig.uiConfig.normalTextColor;
            }
            if (beautyPluginConfig.uiConfig.selectedTextColor != 0) {
                selectedTextColor = beautyPluginConfig.uiConfig.selectedTextColor;
            }
            if (beautyPluginConfig.uiConfig.selectedIconDotColor != 0) {
                selectedHint.setBackgroundColor(beautyPluginConfig.uiConfig.selectedIconDotColor);
            }
            if (beautyPluginConfig.uiConfig.selectedIconBorderColor != 0) {
                icon.setStrokeColor(ColorStateList.valueOf(beautyPluginConfig.uiConfig.selectedIconBorderColor));
            }

        }
        int[] colors = new int[]{selectedTextColor, normalTextColor};
        ColorStateList colorStateList = new ColorStateList(states, colors);
        name.setTextColor(colorStateList);

        BeautyFeatureItem beautyFeatureItem = beautyFeatureItems.get(position);
        name.setText(beautyFeatureItem.name);
        icon.setImageDrawable(beautyFeatureItem.drawable);

        icon.setClipToOutline(true);
        if (position != 0) {
            boolean selected = selectedItemIndex == position;
            name.setSelected(selected);
            BeautyFeature beautyFeature = beautyFeatureItem.beautyFeature;

            if (beautyFeature.getParentGroup() == BeautyGroup.FILTERS
                || beautyFeature.getParentGroup() == BeautyGroup.STYLE_MAKEUP
                || beautyFeature.getParentGroup() == BeautyGroup.STICKERS
                || beautyFeature.getParentGroup() == BeautyGroup.BACKGROUND) {
                int cornerSize = ZegoUtil.dp2px(4, icon.getResources().getDisplayMetrics());
                icon.setShapeAppearanceModel(
                    // rect
                    icon.getShapeAppearanceModel().toBuilder().setAllCornerSizes(new AbsoluteCornerSize(cornerSize))
                        .setAllCorners(new RoundedCornerTreatment()).build());
            } else {
                // round
                icon.setShapeAppearanceModel(
                    icon.getShapeAppearanceModel().toBuilder().setAllCornerSizes(new RelativeCornerSize(0.5f))
                        .setAllCorners(new RoundedCornerTreatment()).build());
            }
            if (selected) {
                int strokeWidth = ZegoUtil.dp2px(4, icon.getResources().getDisplayMetrics());
                icon.setStrokeWidth(strokeWidth);
            } else {
                icon.setStrokeWidth(0);
            }
        } else {
            name.setSelected(false);
            icon.setStrokeWidth(0);
        }
        if (beautyPluginConfig != null && beautyPluginConfig.uiConfig != null) {
            if (name.isSelected()) {
                if (beautyPluginConfig.uiConfig.selectedTextSize != 0) {
                    name.setTextSize(beautyPluginConfig.uiConfig.selectedTextSize);
                }
            } else {
                if (beautyPluginConfig.uiConfig.normalTextSize != 0) {
                    name.setTextSize(beautyPluginConfig.uiConfig.normalTextSize);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return beautyFeatureItems.size();
    }

    public BeautyFeatureItem getBeautyItem(int position) {
        if (position < 0 || position >= beautyFeatureItems.size()) {
            return null;
        }
        return beautyFeatureItems.get(position);
    }

    public void setBeautyItems(List<BeautyFeatureItem> items) {
        beautyFeatureItems.clear();
        beautyFeatureItems.addAll(items);
        dotIndexes.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    public void setSelectedItemIndex(int selectedItemIndex) {
        this.selectedItemIndex = selectedItemIndex;
        notifyDataSetChanged();
    }

    public void removeSelectedItem() {
        setSelectedItemIndex(0);
    }

    public void showDot(List<Integer> dotIndexes) {
        this.dotIndexes.clear();
        this.dotIndexes.addAll(dotIndexes);
        notifyDataSetChanged();
    }
}
