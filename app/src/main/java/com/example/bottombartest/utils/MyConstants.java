package com.example.bottombartest.utils;

import android.util.Log;

/*
 * author：xuziwei
 * date：2020-07-13 12:06
 * description：
 * God bless my code!!
 */
public class MyConstants {

  //调取系统摄像头的请求码
  public static final int REQUEST_CAMERA = 0;
  //打开相册的请求码
  public static final int REQUEST_PHOTO = 1;

  public static final boolean DEBUG = Boolean.parseBoolean("true");

  //和风天气的key
  public static final String KEY = "d63dc874c9894eda8043b0b3a29caa0f";

  public static final String WEATHER_URL = "https://devapi.heweather.net/v7/";

  public static final String CITY_URL ="https://geoapi.heweather.net/v2/";

  public static final String ZG_API = "http://39.103.132.187:8000/swagger/";

  public static final String IS_LOGIN = "isLogin";//是否登录

  public static final String USER_EMAIL = "user_email";//用户ID

  public static final String USER_NAME= "userName";

  public static final String PASSWORD = "password";//密码

}
