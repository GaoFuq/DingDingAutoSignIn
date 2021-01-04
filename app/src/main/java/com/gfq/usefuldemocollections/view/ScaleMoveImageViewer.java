package com.gfq.usefuldemocollections.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

/**
* 自定义手势放大  缩小  平移view
*/

public class ScaleMoveImageViewer extends AppCompatImageView implements View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener {
   private ScaleGestureDetector sgc;
   private GestureDetector gd;
   private float SOURCE_SCALE;
   private Matrix matrix=new Matrix();
   private float[] values=new float[9];
   private boolean once=true;
   private float preX,preY,currentX,currentY;
   private int prePointerCount;

   private static final int REQUESTCODE_BIGER=1;
   private static final int REQUESTCODE_SMALLER=2;
   private static final float BIGER_TMP_SCALE=1.06f;
   private static final float SMALLER_TMP_SCALE=0.94f;
   private static final float MAX_SCALE=4.0F;
   private static final float MIN_SCALE=0.2F;
   private Activity imageShower;
   public ScaleMoveImageViewer(Context context) {
       this(context,null);
   }
   public ScaleMoveImageViewer(final Context context, AttributeSet attrs) {
       super(context, attrs);
       this.imageShower= (Activity) context;
       super.setScaleType(ScaleType.MATRIX);
       this.setOnTouchListener(this);
       sgc=new ScaleGestureDetector(context, this);
       gd=new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
           @Override
           public boolean onDoubleTap(MotionEvent e) {
               Log.i("TAG","onDoubleTap");
               //处理双击事件
               float x=e.getX();
               float y=e.getY();
               setDobleTapScale(x, y);
               return true;
           }
           @Override
           public boolean onSingleTapConfirmed(MotionEvent e) {
               //处理单击关闭页面
               imageShower.finish();
               return super.onSingleTapConfirmed(e);
           }
       });

   }

   //手指缩放
   @Override
   public boolean onScale(ScaleGestureDetector detector) {
       float scaleFactor=detector.getScaleFactor();
       float currentScale=getScale();//相对原图的缩放比例
       if(currentScale>MAX_SCALE && scaleFactor<1.0f || currentScale<MIN_SCALE
               && scaleFactor>1.0f || currentScale<MAX_SCALE && currentScale>MIN_SCALE){
           matrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
       }
//       ImagePositonManager.setShowPosition(getDrawable(), matrix, getWidth(), getHeight());
       setImageMatrix(matrix);
       return true;
   }

   //移动
   @Override
   public boolean onTouch(View v, MotionEvent event) {
       currentX=0;currentY=0;
       int pointerCount=event.getPointerCount();
       for(int i=0;i<pointerCount;i++){
           currentX+=event.getX();
           currentY+=event.getY();
       }
       currentX/=pointerCount;
       currentY/=pointerCount;
       if (pointerCount!=prePointerCount) {
           preX=currentX;
           preY=currentY;
           prePointerCount=pointerCount;
       }
       switch (event.getAction()) {
           case MotionEvent.ACTION_MOVE:
               float dx=currentX-preX;
               float dy=currentY-preY;
//               ImagePositonManager.setMovePosition(getDrawable(), matrix, dx, dy, getWidth(), getHeight());
               setImageMatrix(matrix);
               preX=currentX;
               preY=currentY;
               break;
           case MotionEvent.ACTION_UP://有多根手指触摸屏幕时，只有当所有的手指抬起时这里才执行
               prePointerCount=0;
               break;
       }
       gd.onTouchEvent(event);
       return sgc.onTouchEvent(event);
   }
   //双击缩放
   public void setDobleTapScale(float px,float py){
       float currectScale=getScale();
       if(currectScale<SOURCE_SCALE){
           ScaleMoveImageViewer.this.postDelayed(new AutoScaleRunnable(SOURCE_SCALE, px, py,REQUESTCODE_BIGER), 10);
       }
       if(currectScale==SOURCE_SCALE){
           ScaleMoveImageViewer.this.postDelayed(new AutoScaleRunnable(MAX_SCALE-1, px, py,REQUESTCODE_BIGER), 10);
       }
       if(currectScale>SOURCE_SCALE){
           ScaleMoveImageViewer.this.postDelayed(new AutoScaleRunnable(SOURCE_SCALE, px, py,REQUESTCODE_SMALLER), 10);
       }
//       /ImagePositonManager.setShowPosition(getDrawable(), matrix, getWidth(), getHeight());
       setImageMatrix(matrix);
   }

   private class AutoScaleRunnable implements Runnable{

       float targetScale=0;
       float px=0;
       float py=0;
       int requestCode=0;
       public AutoScaleRunnable(float targetScale,float px,float py,int requestCode){
           this.targetScale=targetScale;
           this.px=px;
           this.py=py;
           this.requestCode=requestCode;
       }

       @Override
       public void run() {
           if(requestCode==REQUESTCODE_BIGER){
               matrix.postScale(BIGER_TMP_SCALE, BIGER_TMP_SCALE, px, py);
//               ImagePositonManager.setShowPosition(getDrawable(), matrix, getWidth(), getHeight());
               setImageMatrix(matrix);
               float currentScale = getScale();
               if (currentScale<targetScale) {
                   ScaleMoveImageViewer.this.postDelayed(this, 10);
               }else {
                   while(getScale()!=targetScale){
                       matrix.postScale(targetScale/getScale(), targetScale/getScale(), px, py);
//                       ImagePositonManager.setShowPosition(getDrawable(), matrix, getWidth(), getHeight());
                       setImageMatrix(matrix);
                   }
               }
           }
           else if (requestCode==REQUESTCODE_SMALLER) {
               matrix.postScale(SMALLER_TMP_SCALE, SMALLER_TMP_SCALE, px, py);
//               ImagePositonManager.setShowPosition(getDrawable(), matrix, getWidth(), getHeight());
               setImageMatrix(matrix);
               float currentScale = getScale();
               if(currentScale>targetScale){
                   ScaleMoveImageViewer.this.postDelayed(this, 10);
               }else {
                   while(getScale()!=targetScale){
                       matrix.postScale(targetScale/getScale(), targetScale/getScale(), px, py);
                       //ImagePositonManager.setShowPosition(getDrawable(), matrix, getWidth(), getHeight());
                       setImageMatrix(matrix);
                   }
               }
           }
       }
   }

   @Override
   protected void onDraw(Canvas canvas) {
       if(once){
          if (getDrawable()==null){
               return;
           }
           matrix=getImageMatrix();
           once=false;
           Drawable drawable=getDrawable();
           //获取图片的宽和高
           int dw=drawable.getIntrinsicWidth();
           int dh=drawable.getIntrinsicHeight();
           int w=getWidth();
           int h=getHeight();
           float scale=Math.min(1.0f*w/dw, 1.0f*h/dh);
           SOURCE_SCALE=scale;
           matrix.postTranslate(w/2-dw/2, h/2-dh/2);
           matrix.postScale(scale, scale, w/2, h/2);
           setImageMatrix(matrix);
       }
       super.onDraw(canvas);
   }

   private float getScale(){
       matrix.getValues(values);
       return values[Matrix.MSCALE_X];
   }

   @Override
   public boolean onScaleBegin(ScaleGestureDetector detector) {
       return true;

   }

   @Override
   public void onScaleEnd(ScaleGestureDetector detector) {

   }
}
