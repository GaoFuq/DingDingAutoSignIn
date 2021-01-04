package com.gfq.usefuldemocollections.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.customview.widget.ViewDragHelper


class DragView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private val dragHelper: ViewDragHelper = ViewDragHelper.create(this,object :ViewDragHelper.Callback(){
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
          return  child.tag == "canDrag"
        }
        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return left
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return top
        }
    })


    private val r = Rect()



    fun expandWidth(): Unit {

        getHitRect(r)
        r.right += 100
        layout(r.left, r.top, r.right, r.bottom)
        Log.e("TAG", "expandWidth: ${r.toString()}")
    }


//    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
//        return dragHelper.shouldInterceptTouchEvent(ev)
//    }
//
//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        dragHelper.processTouchEvent(event)
//        return true
//    }


}