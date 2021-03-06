package com.example.bottombartest.utils;

import android.util.Log;

/*
 * author：xuziwei
 * date：2020-07-13 13:41
 * description：自定义log类
 * God bless my code!!
 */
public class LogUtils {

  public static void d(String tag,String msg) {
    if (MyConstants.DEBUG) {
      Log.d(tag, msg);
    }
  }

  public static void i(String tag,String msg) {
    if (MyConstants.DEBUG) {
      Log.i(tag, msg);
    }
  }

  public static void e(String tag,String msg) {
    if (MyConstants.DEBUG) {
      Log.e(tag, msg);
    }
  }
}
