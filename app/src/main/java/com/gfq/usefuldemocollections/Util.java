package com.gfq.usefuldemocollections;

import android.content.Context;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class Util {
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @BindingAdapter("adapter")
    public static void setAdapter(RecyclerView rv,RecyclerView.Adapter<RecyclerView.ViewHolder> adapter){
        rv.setAdapter(adapter);
    }
}
