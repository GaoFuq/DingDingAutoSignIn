package com.gfq.usefuldemocollections.view;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gfq.usefuldemocollections.R;

/**
 * DATE_TIME 2021/1/15
 * AUTH gaofuq
 * DESCRIPTION
 */
public class FormView extends FrameLayout {

    public TextView tvDate, tvSB, tvXB, tvTime;

    public FormView(Context context) {
        super(context);
        init(context);
    }

    void init(Context context) {
        View inflate = inflate(context, R.layout.form, this);
        tvDate = inflate.findViewById(R.id.tvDate);
        tvSB = inflate.findViewById(R.id.tvSB);
        tvXB = inflate.findViewById(R.id.tvXB);
        tvTime = inflate.findViewById(R.id.tvTime);
    }


}
