package com.example.bottombartest;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import io.realm.Realm;

/*
 * author：xuziwei
 * date：2020-07-13 18:00
 * description：
 * God bless my code!!
 */
public class MyApplication extends Application {

  private static Context sContext = null;
  private static Handler sHandler = null;

  @Override
  public void onCreate() {
    super.onCreate();
    sContext = getBaseContext();
    sHandler = new Handler();
    Realm.init(sContext);
  }

  public static Context getAppContext(){
    return sContext;
  }

  public static Handler getHandler(){
    return sHandler;
  }

}
