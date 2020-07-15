package com.example.bottombartest.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.bottombartest.R;
import com.example.bottombartest.utils.LogUtils;

import java.lang.reflect.Field;

/*
 * author：xuziwei
 * date：2020-07-15 21:58
 * description：自动轮播
 * God bless my code!!
 */
public class MyViewPager extends ViewPager {

  private Handler mHandler;

  public void setDuration(int duration) {
    mDuration = duration;
  }

  private int mDuration;

  private static final String TAG = "MyViewPager";

  public MyViewPager(@NonNull Context context) {
    this(context,null);
    setPageTransformer(true,new PageTransformer() {
      @Override
      public void transformPage(@NonNull View page, float position) {

      }
    });
  }

  public MyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    mHandler = new Handler(Looper.getMainLooper());

    setOnTouchListener(new OnTouchListener() {
      @Override
      public boolean onTouch(View view, MotionEvent motionEvent) {
        //不处理事件
        int action = motionEvent.getAction();
        switch(action) {
          case MotionEvent.ACTION_DOWN:
            pauseLooper();
            break;
          case MotionEvent.ACTION_CANCEL:
          case MotionEvent.ACTION_UP:
            resumeLooper();
            break;
        }
        return false;
      }
    });
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    this.resumeLooper();
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    this.pauseLooper();
  }

  private void resumeLooper() {
    //继续轮播
    mHandler.postDelayed(mTask,mDuration);
  }

  private Runnable mTask = new Runnable() {
    @Override
    public void run() {
      int currentItem = getCurrentItem();
      currentItem++;
      setCurrentItem(currentItem);
      mHandler.postDelayed(this,mDuration);
    }
  };

  private void pauseLooper() {
    //暂停轮播
    mHandler.removeCallbacks(mTask);
  }


}
