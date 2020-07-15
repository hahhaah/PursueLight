package com.example.bottombartest.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.bottombartest.MyApplication;

/*
 * author：xuziwei
 * date：2020-07-13 18:00
 * description：全局帮助类
 * God bless my code!!
 */
public class UIHelper {

  /**
   * 资源ID获取String
   */
  public static String getString(int stringId) {
    Context context = MyApplication.getAppContext();
    if (context != null) {
      return context.getString(stringId);
    }
    return " ";
  }

  public static String getString(int stringId, Object... formatArgs) {
    Context context = MyApplication.getAppContext();
    if (context != null) {
      return context.getString(stringId, formatArgs);
    }
    return "";
  }

  /**
   * 获取手机的密度
   */
  public static float getDensity(Context context) {
    DisplayMetrics dm = context.getResources().getDisplayMetrics();
    return dm.density;
  }

  public static int dp2px(Context context, float dp) {
    return (int) Math.ceil((double) (context.getResources().getDisplayMetrics().density * dp));
  }

  public static String getPackageName() {
    return MyApplication.getAppContext().getPackageName();
  }

  public static void setLeftDrawable(TextView textView, int draw) {
    Drawable drawable = UIHelper.getDrawable(draw);
    try {
      assert drawable != null;
      drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
      textView.setCompoundDrawables(drawable, null, null, null);
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
  }

  /**
   * 获取Drawable
   */
  public static Drawable getDrawable(int drawable) {
    return ContextCompat.getDrawable(MyApplication.getAppContext(), drawable);
  }

  public static int dip2px(Context context,float dpValue) {
    float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }
}
