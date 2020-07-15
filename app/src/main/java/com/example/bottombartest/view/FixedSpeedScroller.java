package com.example.bottombartest.view;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/*
 * author：xuziwei
 * date：2020-07-15 22:49
 * description：控制viewpager滑动速度的帮助类
 * God bless my code!!
 */
public class FixedSpeedScroller extends Scroller {

  private int mDuration = 2000;

  public FixedSpeedScroller(Context context) {
    super(context,null);
  }

  public FixedSpeedScroller(Context context, Interpolator interpolator) {
    super(context, interpolator);
  }

  public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
    super(context, interpolator, flywheel);

  }

  public void setDuration(int time) {
    mDuration = time;
  }

  public int getmDuration() {
    return mDuration;
  }
}
