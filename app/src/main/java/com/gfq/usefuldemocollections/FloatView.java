package com.gfq.usefuldemocollections;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class FloatView extends AbsFloatBase {
    private TextView view;
    private Thread thread;

    public FloatView(Context context) {
        super(context);
    }

    @Override
    public void create() {
        super.create();
        thread = Thread.currentThread();
        mViewMode = WRAP_CONTENT_TOUCHABLE;
        mGravity = Gravity.TOP | Gravity.LEFT;

        inflate(R.layout.tip);

        view = findView(R.id.tvTip);

    }

    public void setText(CharSequence text) {
        if (view != null) {
            if (Thread.currentThread() != thread) {
                view.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        view.setText(text);
                    }
                });
            }else {
                view.setText(text);
            }
        }
    }
}