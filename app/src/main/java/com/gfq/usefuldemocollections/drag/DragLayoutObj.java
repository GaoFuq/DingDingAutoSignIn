package com.gfq.usefuldemocollections.drag;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.RectKt;
import androidx.customview.widget.ViewDragHelper;

import static androidx.customview.widget.ViewDragHelper.STATE_DRAGGING;
import static androidx.customview.widget.ViewDragHelper.STATE_IDLE;
import static androidx.customview.widget.ViewDragHelper.STATE_SETTLING;

public class DragLayoutObj extends LinearLayout {
    private Context context;
    private ViewDragHelper mDragHelper;
    private ScaleGestureDetector scaleGestureDetector;
    private View dragView;
    private Rect rect = new Rect();
    private int width;
    private int height;
    private float mScale = 1.0f;
    private Scroller scroller;
    private VelocityTracker mVelocityTracker;
    private int mMaxVelocity;


    public DragLayoutObj(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mDragHelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());
        scaleGestureDetector = new ScaleGestureDetector(context, new MyScaleGD());
        scroller = new Scroller(context);
        ViewConfiguration config = ViewConfiguration.get(context);
        mMaxVelocity = config.getScaledMinimumFlingVelocity();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mDragHelper.shouldInterceptTouchEvent(event);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        dragView = getChildAt(0);
    }


    int lastX, lastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        if (mVelocityTracker == null) {
//            mVelocityTracker = VelocityTracker.obtain();
//        }
//        mVelocityTracker.addMovement(event);
//
//        int x = (int) event.getX();
//        int y = (int) event.getY();
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                if (!scroller.isFinished()) {
//                    scroller.abortAnimation();
//                }
//                break;
//            case MotionEvent.ACTION_MOVE:
//
//                int dx = lastX - x;
//                int dy = lastY - y;
//                int pointerCount = event.getPointerCount();
//                if (pointerCount == 1) {
//                    scrollBy(dx, dy);
////                    mDragHelper.processTouchEvent(event);
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//
//                float x1 = getScrollX();
//
//                Log.e("x",x1+"");
////                final VelocityTracker velocityTracker = mVelocityTracker;
////                velocityTracker.computeCurrentVelocity(1000);
////                int xVelocity = (int) velocityTracker.getXVelocity();
////                int yVelocity = (int) velocityTracker.getYVelocity();
//
//
//
//
//                if (x1 > 0) {
//                    scroller.startScroll(getScrollX(), getScrollY(), left, 0, Math.abs(left) * 2);
//                }
//                if(top>0){
//                    scroller.startScroll(getScrollX(), getScrollY(), 0, top, Math.abs(top) * 2);
//                }
////
//
////
////                if (xVelocity < 0) {//继续右滑
////                } else if (xVelocity > 0) {//继续左滑
////                    scroller.startScroll(getScrollX(),getScrollY(),500,0,Math.abs(500)*2);
////                }else {
////
////                }
////                invalidate();
////                if (mVelocityTracker != null) {
////                    mVelocityTracker.recycle();
////                    mVelocityTracker = null;
////                }
//                break;
//        }
//
//        lastX = x;
//        lastY = y;

        mDragHelper.processTouchEvent(event);
        return scaleGestureDetector.onTouchEvent(event);

    }


    //    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }

    public void expandWidth() {
        dragView.getHitRect(rect);
        rect.right += 100;
        dragView.layout(rect.left, rect.top, rect.right, rect.bottom);
    }

    public void expandHeight() {
        dragView.getHitRect(rect);
        rect.bottom += 100;
        dragView.layout(rect.left, rect.top, rect.right, rect.bottom);

        Log.e("dd", "expandHeight");
        Log.e("dd", "dragView W = " + dragView.getWidth());
        Log.e("dd", "dragView H = " + dragView.getHeight());
    }

    public void reset() {
        mScale = 1.0f;
        setScaleX(mScale);
        setScaleY(mScale);
        dragView.layout(0, 0, dragView.getWidth(), dragView.getHeight());

    }


    private class MyScaleGD implements ScaleGestureDetector.OnScaleGestureListener {
        Rect rect = new Rect();

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Log.e("dd", "onScale");
            float scaleFactor = detector.getScaleFactor(); // 缩放的倍数  初始为1.0

//            mScale = mScale + scaleFactor - 1;
            mScale *= scaleFactor;


            if (mScale <= 0.2) {
                mScale = 0.2f;
            }
            if (mScale >= 1.5) {
                mScale = 1.5f;
            }


            dragView.setScaleX(mScale);
            dragView.setScaleY(mScale);


//            Log.e("onScale", mScale + "");

            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            Log.e("dd", "onScaleBegin");
            Log.e("dd", "dragView W = " + dragView.getWidth());
            Log.e("dd", "dragView H = " + dragView.getHeight());


            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {


        }
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {
        int left, top;

        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return child == dragView;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            switch (state) {
                case STATE_IDLE:
//                    scroller.fling(getScrollX(),getScrollY(),10,10,0,1000,0,1000);
//                    invalidate();
                Log.e("accessibility_service_config","STATE_IDLE");
                    break;
                case STATE_DRAGGING:
                    Log.e("accessibility_service_config","STATE_DRAGGING");
                    break;


            }
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            this.left = left;
            this.top = top;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return left;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            return top;
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
        }
    }


}
