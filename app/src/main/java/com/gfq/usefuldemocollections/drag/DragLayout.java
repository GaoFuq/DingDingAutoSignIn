package com.gfq.usefuldemocollections.drag;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.gfq.usefuldemocollections.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DragLayout extends ViewGroup {
    private int margin;
    private Context context;
    private int childHeight;
    private int childWidth;

    private List<ChildBean> childBeanList = new ArrayList<>();
    private final ChildTouch childTouch = new ChildTouch();
    private final MyDragListener dragListener = new MyDragListener();


    private class ChildBean {
        Rect rect;
        int position;
        String data;
        View view;

        @Override
        public String toString() {
            return "ChildBean{" +
                    "rect=" + rect +
                    ", position=" + position +
                    ", data='" + data + '\'' +
                    '}';
        }
    }

    public DragLayout(Context context) {
        super(context);
        this.context = context;
        margin = Util.dp2px(context, 10);
        setOnDragListener(dragListener);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        margin = Util.dp2px(context, 10);
        setOnDragListener(dragListener);


    }

    public void setData(List<String> dataList) {
        for (String o : dataList) {
            TextView textView = new TextView(context);
            textView.setText(o);
            textView.setBackgroundColor(Color.GRAY);
            setChildView(textView, o);
            addView(textView);
        }
        notifyDataChanged();
    }


    private void notifyDataChanged() {
        int dataSize = childBeanList.size();
        int childCount = getChildCount();
        int size = Math.min(dataSize, childCount);
        for (int i = 0; i < size; i++) {
            notifyItemDataChanged(i);
        }
    }

    private void notifyItemDataChanged(int tag) {
        TextView view = (TextView) getChildAt(tag);
        view.setText(childBeanList.get(tag).data);
        Log.e("accessibility_service_config",childBeanList.get(tag).toString());
    }


    private void setChildView(View child, String todo) {
        int width = getWidth();
        int height = getHeight();
        childWidth = (width - (margin * 2)) / 3;
        childHeight = (height - margin) / 2;

        ChildBean childBean = new ChildBean();
        Rect rect = new Rect();
        int childCount = getChildCount();
        getRect(rect, childCount);

        childBean.rect = rect;
        childBean.position = childCount;
        childBean.view = child;
        childBean.data = childCount + "data" + todo;

        childBeanList.add(childBean);

        child.setOnTouchListener(childTouch);
        child.setTag(childCount);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            Rect rect = childBeanList.get(i).rect;
            child.layout(rect.left, rect.top, rect.right, rect.bottom);
        }
    }


    private void getRect(Rect rect, int pos) {
        int margin = Util.dp2px(context, 10);
        if (pos < 3) {
            rect.left = (childWidth + margin) * pos;
            rect.top = 0;
            rect.right = childWidth + (childWidth + margin) * pos;
            rect.bottom = childHeight;
        } else {
            rect.left = (childWidth + margin) * (pos - 3);
            rect.top = childHeight + margin;
            rect.right = childWidth + (childWidth + margin) * (pos - 3);
            rect.bottom = 2 * childHeight + margin;
        }
    }


    @Override
    public void addView(View child) {
        if (getChildCount() > 5) {
            return;
        }
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (getChildCount() > 5) {
            return;
        }
        super.addView(child, index);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount() > 5) {
            return;
        }
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 5) {
            throw new IllegalStateException("DragLayout can host no more than direct child");
        }
        super.addView(child, index, params);
    }


    private class ChildTouch implements OnTouchListener {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Integer tag = (Integer) v.getTag();
                v.startDragAndDrop(null, new DragShadowBuilder(v), childBeanList.get(tag), 0);
                v.setVisibility(INVISIBLE);
                return true;
            }
            return false;
        }
    }


    private class MyDragListener implements OnDragListener {
        Rect receivedDragEventViewRect = new Rect();
        ChildBean swapBean = null;


        @Override
        public boolean onDrag(View receivedDragEventView, DragEvent event) {
            final ChildBean dragChildBean = (ChildBean) event.getLocalState();
            final int x = (int) event.getX();
            final int y = (int) event.getY();
            getHitRect(receivedDragEventViewRect);

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    onStartDrag(dragChildBean, x, y);
                    return true;

                case DragEvent.ACTION_DRAG_ENTERED:
                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:
                    onDragLocation(dragChildBean, x, y);
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:
                    return true;

                case DragEvent.ACTION_DROP:
                    onDragDrop(dragChildBean);
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    swapBean = null;
                    return true;
            }


            return false;
        }

        private void onDragDrop(ChildBean dragChildBean) {
            if (swapBean != null) {
                swapBean.view.setBackgroundColor(Color.GRAY);
                childBeanList.set(swapBean.position,dragChildBean);
                childBeanList.set(dragChildBean.position,swapBean);
//                Collections.swap(childBeanList,swapBean.position,dragChildBean.position);
                notifyItemDataChanged(swapBean.position);
                notifyItemDataChanged(dragChildBean.position);
                dragChildBean.view.setVisibility(VISIBLE);
            }
        }

        private void onDragLocation(ChildBean dragChildBean, int x, int y) {
            for (ChildBean bean : childBeanList) {
                if (bean.view != dragChildBean.view && bean.rect.contains(x, y)) {
                    bean.view.setBackgroundColor(Color.RED);
                    swapBean = bean;
                } else {
                    bean.view.setBackgroundColor(Color.GRAY);
                }
            }
        }

        private void onStartDrag(ChildBean dragChildBean, int x, int y) {
            dragChildBean.view.setBackgroundColor(Color.YELLOW);
        }
    }
}
