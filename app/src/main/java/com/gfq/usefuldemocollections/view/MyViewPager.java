package com.gfq.usefuldemocollections.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class MyViewPager extends ViewGroup {
 
    private int mLastX,mLastY;
 
    public MyViewPager(Context context) {
        super(context);
        init(context);
    }
 
    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
 
    public MyViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
 
    private void init(Context context) {
 
    }
 
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for(int i = 0; i < count; i++){
            View child = getChildAt(i);
            child.measure(widthMeasureSpec,heightMeasureSpec);
        }
    }
 
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        Log.d("TAG","--l-->"+l+",--t-->"+t+",-->r-->"+r+",--b-->"+b);
        for(int i = 0; i < count; i++){
            View child = getChildAt(i);
            child.layout(i * getWidth(), t, (i+1) * getWidth(), b);
        }
    }
 
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                break;
            case MotionEvent.ACTION_MOVE:
               int dx = mLastX - x;
               int dy = mLastY - y;
                 /*int oldScrollX = getScrollX();//原来的偏移量
                int preScrollX = oldScrollX + dx;//本次滑动后形成的偏移量
                if(preScrollX > (getChildCount() - 1) * getWidth()){
                    preScrollX = (getChildCount() - 1) * getWidth();
                }
                if(preScrollX < 0){
                    preScrollX = 0;
                }
                scrollTo(preScrollX,getScrollY());*/
                scrollBy(dx,dy);

                break;


        }

        mLastX = x;
        mLastY = y;
        return true;
    }
}