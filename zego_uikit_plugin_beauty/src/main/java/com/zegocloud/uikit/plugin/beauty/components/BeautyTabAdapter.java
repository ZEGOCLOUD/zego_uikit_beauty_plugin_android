package com.zegocloud.uikit.plugin.beauty.components;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.zegocloud.uikit.plugin.beauty.ZegoUtil;

public class BeautyTabAdapter extends RecyclerView.Adapter<ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        TextView textView = new TextView(context);
        int padding = ZegoUtil.dp2px(4, context.getResources().getDisplayMetrics());
        textView.setPadding(padding, padding, padding, padding);
        textView.setTextColor(Color.parseColor("#4DFFFFFF"));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

        MarginLayoutParams layoutParams = new MarginLayoutParams(-2, -2);
        layoutParams.leftMargin = ZegoUtil.dp2px(8, context.getResources().getDisplayMetrics());
        textView.setLayoutParams(layoutParams);
        return new ViewHolder(textView) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
