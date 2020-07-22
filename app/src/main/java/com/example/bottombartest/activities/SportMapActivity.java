package com.example.bottombartest.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.bottombartest.R;
import com.example.bottombartest.db.DataManager;
import com.example.bottombartest.db.RealmHelper;
import com.example.bottombartest.entity.PathRecord;
import com.example.bottombartest.entity.SportMotionRecord;
import com.example.bottombartest.sports.PathSmoothTool;
import com.example.bottombartest.utils.CountTimerUtil;
import com.example.bottombartest.utils.DateUtil;
import com.example.bottombartest.utils.MotionUtils;
import com.example.bottombartest.utils.MyConstants;
import com.example.bottombartest.utils.UIHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间: 2020/07/18 19:21 <br>
 * 作者: xuziwei <br>
 * 描述:
 * God bless my code!!
 */
public class SportMapActivity extends AppCompatActivity implements View.OnClickListener {

  private static final String TAG = "SportMapActivity";

  private RelativeLayout sportContent;
  private MapView mMapView;
  private TextView mTvFinish;
  private TextView mTvPause;
  private TextView mTvContinue;
  private TextView mTvMode;
  private TextView mTvMile;
  private TextView mTvSpeed;
  private Chronometer mCurTime;
  private FrameLayout mCountTimer;
  private TextView mTvNumber;


  private PolylineOptions polylineOptions;
  private Polyline mOriginPolyline;
  private PathRecord record;
  private DataManager dataManager = null;
  private PathSmoothTool mpathSmoothTool = null;
  private List<LatLng> mSportLatLngs = new ArrayList<>(0);

  private Dialog tipDialog = null;

  //运动计算相关
  private DecimalFormat decimalFormat = new DecimalFormat("0.00");
  private Handler mHandler = new Handler(Looper.getMainLooper());

  private boolean IS_START = false;

  private AMap aMap;

  private ValueAnimator appearAnim1;
  private ValueAnimator hiddenAnim1;

  private ValueAnimator appearAnim2;
  private ValueAnimator hiddenAnim2;

  private ValueAnimator appearAnim3;
  private ValueAnimator hiddenAnim3;

  private long seconds = 0;//秒数(时间)
  private long mStartTime = 0;
  private long mEndTime = 0;
  private double distance;//路程

  private boolean mode = true;

  //地图中定位的类
  private LocationSource.OnLocationChangedListener mListener = null;
  private AMapLocationClient mLocationClient;
  private AMapLocationClientOption mLocationOption;
  private final Long interval = 4000L;//定位时间间隔

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sport_map);

    initView();
    mMapView.onCreate(savedInstanceState);
    initEvent();
    initData();
  }


  private class MyRunnable implements Runnable {
    @Override
    public void run() {
      mCurTime.setText(formatSeconds());
      mHandler.postDelayed(this, 1000);
    }
  }

  private MyRunnable mRunnable = null;

  private void initView() {
    sportContent = findViewById(R.id.sport_content);
    mMapView = findViewById(R.id.mapView);

    mTvFinish = findViewById(R.id.tv_finish);
    mTvPause = findViewById(R.id.tv_pause);
    mTvContinue = findViewById(R.id.tv_continue);
    mTvMode = findViewById(R.id.tv_mode);
    mTvMile = findViewById(R.id.tv_mile);
    mTvSpeed = findViewById(R.id.tv_speed);
    mCurTime = findViewById(R.id.real_time);
    mCountTimer = findViewById(R.id.fl_count_timer);
    mTvNumber = findViewById(R.id.tv_number_anim);
  }

  private void initEvent() {
    mTvMode.setOnClickListener(this);
    mTvContinue.setOnClickListener(this);
    mTvFinish.setOnClickListener(this);
    mTvPause.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tv_mode:
        setMode();
        break;
      case R.id.tv_finish:
        IS_START = false;

        unBindService();

        if (null != mRunnable) {
          mHandler.removeCallbacks(mRunnable);
          mRunnable = null;
        }

        if (null != record && null != record.getPathLinePoints() && !record.getPathLinePoints().isEmpty()) {
          saveRecord();
        } else {
          ToastUtils.showShort("没有记录到路径!");
          finish();
        }
        break;


      case R.id.tv_pause:
        IS_START = false;

        if (null != mRunnable) {
          mHandler.removeCallbacks(mRunnable);
          mRunnable = null;
        }
        mEndTime = System.currentTimeMillis();

        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(getBounds(mSportLatLngs), 20));

        appearAnim1.start();
        hiddenAnim2.start();
        appearAnim3.start();
        break;
      case R.id.tv_continue:
        IS_START = true;

        if (mRunnable == null)
          mRunnable = new MyRunnable();
        mHandler.postDelayed(mRunnable, 0);

        startUpLocation();

        hiddenAnim1.start();
        appearAnim2.start();
        hiddenAnim3.start();
        break;

      default:break;
    }
  }

  private void saveRecord() {
    ToastUtils.showShort("正在保存运动数据!");

    try {
      SportMotionRecord sportMotionRecord = new SportMotionRecord();

      List<LatLng> locations = record.getPathLinePoints();
      LatLng firstLocaiton = locations.get(0);
      LatLng lastLocaiton = locations.get(locations.size() - 1);

      sportMotionRecord.setId(System.currentTimeMillis());
      sportMotionRecord.setEmail(SPUtils.getInstance().getString(MyConstants.USER_EMAIL, "123@qq.com"));
      sportMotionRecord.setDistance(distance);
      sportMotionRecord.setDuration(seconds);
      sportMotionRecord.setStartTime(mStartTime);
      sportMotionRecord.setEndTime(mEndTime);
      sportMotionRecord.setStartPoint(MotionUtils.amapLocationToString(firstLocaiton));
      sportMotionRecord.setEndPoint(MotionUtils.amapLocationToString(lastLocaiton));
      sportMotionRecord.setPathLine(MotionUtils.getLatLngPathLineString(locations));
      double sportMile = distance / 1000d;
      //体重先写120斤
      sportMotionRecord.setCalorie(MotionUtils.calculateCalorie(60, sportMile));
      sportMotionRecord.setSpeed(sportMile / ((double) seconds / 3600));
      sportMotionRecord.setDistribution(record.getDistribution());
      sportMotionRecord.setDateTag(DateUtil.getStringDateShort(mEndTime));

      dataManager.insertSportRecord(sportMotionRecord);

    } catch (Exception e) {
      LogUtils.e("保存运动数据失败", e);
    }

    mHandler.postDelayed(new Runnable() {
      @Override
      public void run() {
        setResult(RESULT_OK);
        SportResultActivity.startActivityWithData(SportMapActivity.this, mStartTime, mEndTime);
        finish();
      }
    }, 1500);

  }

  private void initData() {
    record = new PathRecord();
    dataManager = new DataManager(new RealmHelper());
    CountTimerUtil.start(mTvNumber, new CountTimerUtil.AnimationState() {
      @Override
      public void start() {

      }

      @Override
      public void repeat() {

      }

      @Override
      public void end() {
        mCountTimer.setVisibility(View.GONE);
        hiddenAnim1.start();
        hiddenAnim3.start();

        IS_START = true;

        seconds = 0;
        mCurTime.setBase(SystemClock.elapsedRealtime());

        mStartTime = System.currentTimeMillis();
        if (record==null) {
          record = new PathRecord();
        }
        record.setStartTime(mStartTime);

        if (mRunnable == null) {
          mRunnable = new MyRunnable();
        }
        mHandler.postDelayed(mRunnable, 0);

        startUpLocation();

      }
    });

    if (aMap == null) {
      aMap = mMapView.getMap();
      setUpMap();
    }

    setMode();
  }

  private void startUpLocation() {
  }

  public void setHiddenAnimation() {
    hiddenAnim1 = ValueAnimator.ofFloat(0,mTvFinish.getHeight()*2);
    hiddenAnim1.setDuration(500);
    hiddenAnim1.setTarget(mTvFinish);
    hiddenAnim1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        mTvFinish.setTranslationY((Float)valueAnimator.getAnimatedValue());
      }
    });
    hiddenAnim1.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationCancel(Animator animation) {
        mTvFinish.setEnabled(true);
      }

      @Override
      public void onAnimationStart(Animator animation) {
        mTvFinish.setEnabled(false);
      }

      @Override
      public void onAnimationEnd(Animator animation) {
        mTvFinish.setEnabled(true);
      }

      @Override
      public void onAnimationRepeat(Animator animation) {
      }
    });

    hiddenAnim2 = ValueAnimator.ofFloat(0,mTvPause.getHeight()*2);
    hiddenAnim2.setDuration(500);
    hiddenAnim2.setTarget(mTvPause);
    hiddenAnim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        mTvPause.setTranslationY((Float)valueAnimator.getAnimatedValue());
      }
    });

    hiddenAnim2.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationCancel(Animator animation) {
        mTvPause.setEnabled(true);
      }

      @Override
      public void onAnimationStart(Animator animation) {
        mTvPause.setEnabled(false);
      }

      @Override
      public void onAnimationEnd(Animator animation) {
        mTvPause.setEnabled(true);
      }

      @Override
      public void onAnimationRepeat(Animator animation) {
      }
    });


    hiddenAnim3 = ValueAnimator.ofFloat(0,mTvContinue.getHeight()*2);
    hiddenAnim3.setDuration(500);
    hiddenAnim3.setTarget(mTvContinue);

    hiddenAnim3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        mTvContinue.setTranslationY((Float)valueAnimator.getAnimatedValue());
      }
    });

    hiddenAnim3.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationCancel(Animator animation) {
        mTvContinue.setEnabled(true);
      }

      @Override
      public void onAnimationStart(Animator animation) {
        mTvContinue.setEnabled(false);
      }

      @Override
      public void onAnimationEnd(Animator animation) {
        mTvContinue.setEnabled(true);
      }

      @Override
      public void onAnimationRepeat(Animator animation) {
      }
    });
  }


  public void setAppearAnimation() {
    appearAnim1 = ValueAnimator.ofFloat(mTvFinish.getHeight()*2, 0);
    appearAnim1.setDuration(500);
    appearAnim1.setTarget(mTvFinish);
    appearAnim1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        mTvFinish.setTranslationY((Float)valueAnimator.getAnimatedValue());
      }
    });
    appearAnim1.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationCancel(Animator animation) {
        mTvFinish.setEnabled(true);
      }

      @Override
      public void onAnimationEnd(Animator animation) {
        mTvFinish.setEnabled(true);
      }

      @Override
      public void onAnimationRepeat(Animator animation) {
      }

      @Override
      public void onAnimationStart(Animator animation) {
        mTvFinish.setEnabled(false);
      }
    });


    appearAnim2 = ValueAnimator.ofFloat(mTvPause.getHeight()*2, 0);
    appearAnim2.setDuration(500);
    appearAnim2.setTarget(mTvPause);
    appearAnim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        mTvPause.setTranslationY((Float)valueAnimator.getAnimatedValue());
      }
    });
    appearAnim2.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationCancel(Animator animation) {
        mTvPause.setEnabled(true);
      }

      @Override
      public void onAnimationEnd(Animator animation) {
        mTvPause.setEnabled(true);
      }

      @Override
      public void onAnimationRepeat(Animator animation) {
      }

      @Override
      public void onAnimationStart(Animator animation) {
        mTvPause.setEnabled(false);
      }
    });


    appearAnim3 = ValueAnimator.ofFloat(mTvContinue.getHeight()*2, 0);
    appearAnim3.setDuration(500);
    appearAnim3.setTarget(mTvContinue);
    appearAnim3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        mTvContinue.setTranslationY((Float)valueAnimator.getAnimatedValue());
      }
    });
    appearAnim3.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationCancel(Animator animation) {
        mTvContinue.setEnabled(true);
      }

      @Override
      public void onAnimationEnd(Animator animation) {
        mTvContinue.setEnabled(true);
      }

      @Override
      public void onAnimationRepeat(Animator animation) {
      }

      @Override
      public void onAnimationStart(Animator animation) {
        mTvContinue.setEnabled(false);
      }
    });

  }

  public String formatSeconds() {
    String hh = seconds / 3600 > 9 ? seconds / 3600 + "" : "0" + seconds
            / 3600;
    String mm = (seconds % 3600) / 60 > 9 ? (seconds % 3600) / 60 + ""
            : "0" + (seconds % 3600) / 60;
    String ss = (seconds % 3600) % 60 > 9 ? (seconds % 3600) % 60 + ""
            : "0" + (seconds % 3600) % 60;

    seconds++;

    return hh + ":" + mm + ":" + ss;
  }

  private void setMode() {
    if (mode) {
      mTvMode.setText("地图模式");
      UIHelper.setLeftDrawable(mTvMode, R.drawable.map_mode);
      mMapView.setVisibility(View.GONE);
    } else {
      mTvMode.setText("跑步模式");
      UIHelper.setLeftDrawable(mTvMode, R.drawable.run_mode);
      mMapView.setVisibility(View.VISIBLE);
    }
    mode = !mode;
  }

  private float getDistance(List<LatLng> list) {
    float distance = 0;
    if (list == null || list.size() == 0) {
      return distance;
    }
    for (int i = 0; i < list.size() - 1; i++) {
      LatLng firstLatLng = list.get(i);
      LatLng secondLatLng = list.get(i + 1);
      double betweenDis = AMapUtils.calculateLineDistance(firstLatLng,
              secondLatLng);
      distance = (float) (distance + betweenDis);
    }
    return distance;
  }

  @Override
  public void onBackPressed() {
    if (IS_START) {
      ToastUtils.showShort("退出请点击暂停按钮，再结束运动!");
      return;
    }

    if (null != record && null != record.getPathLinePoints() && !record.getPathLinePoints().isEmpty()) {
      showTipDialog("确定退出?", "退出将删除本次运动记录,如要保留运动数据,请点击完成!",
            new TipCallBack() {
              @Override
              public void confirm() {finish();}

              @Override
              public void cancel() { }
            });
      return;
    }
    super.onBackPressed();
  }

  private void showTipDialog(String title, String tips, final TipCallBack tipCallBack) {
    tipDialog = new Dialog(SportMapActivity.this, R.style.matchDialog);
    View view = LayoutInflater.from(SportMapActivity.this).inflate(R.layout.tip_dialog_layout, null);
    ((TextView) (view.findViewById(R.id.title))).setText(title);
    ((TextView) (view.findViewById(R.id.tips))).setText(tips);
    view.findViewById(R.id.cancelTV).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        tipCallBack.cancel();
        tipDialog.dismiss();
      }
    });
    view.findViewById(R.id.confirmTV).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        tipCallBack.confirm();
        tipDialog.dismiss();
      }
    });
    tipDialog.setContentView(view);
    tipDialog.show();
  }

  private LocationSource locationSource = new LocationSource() {
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
      mListener = onLocationChangedListener;
      startLocation();
    }

    @Override
    public void deactivate() {
      mListener = null;
      if (mLocationClient != null) {
        mLocationClient.stopLocation();
        mLocationClient.onDestroy();
      }
      mLocationClient = null;
    }
  };

  private LatLngBounds getBounds(List<LatLng> pointlist) {
    LatLngBounds.Builder b = LatLngBounds.builder();
    if (pointlist == null) {
      return b.build();
    }
    for (LatLng latLng : pointlist) {
      b.include(latLng);
    }
    return b.build();

  }

  private void setUpMap() {
    mMapView.setVisibility(View.VISIBLE);
    aMap.setLocationSource(locationSource);// 设置定位监听
    // 自定义系统定位小蓝点
    MyLocationStyle myLocationStyle = new MyLocationStyle();
    myLocationStyle.strokeColor(Color.TRANSPARENT);// 设置圆形的边框颜色
    myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
    // 设置定位的类型为定位模式 ，定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。
    myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
//        myLocationStyle.interval(interval);//设置发起定位请求的时间间隔
//        myLocationStyle.showMyLocation(true);//设置是否显示定位小蓝点，true 显示，false不显示
    myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
    aMap.setMyLocationStyle(myLocationStyle);
    aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
    aMap.getUiSettings().setZoomControlsEnabled(false);// 设置默认缩放按钮是否显示
    aMap.getUiSettings().setCompassEnabled(false);// 设置默认指南针是否显示
    aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
  }

  /**
   * 开始定位。
   */
  private void startLocation() {
    if (mLocationClient == null) {
      mLocationClient = new AMapLocationClient(this);
      //设置定位属性
      mLocationOption = new AMapLocationClientOption();
      mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
      mLocationOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
      mLocationOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
      mLocationOption.setInterval(interval);//可选，设置定位间隔。默认为2秒
      mLocationOption.setNeedAddress(false);//可选，设置是否返回逆地理地址信息。默认是true
      mLocationOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
      mLocationOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
      AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
      mLocationOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
      mLocationOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
      mLocationOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
      mLocationOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.ZH);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
      mLocationClient.setLocationOption(mLocationOption);

      // 设置定位监听
      mLocationClient.setLocationListener(aMapLocationListener);
      //开始定位
      mLocationClient.startLocation();
    }
  }

  /**
   * 定位结果回调
   *
   * @param aMapLocation 位置信息类
   */
  private AMapLocationListener aMapLocationListener = new AMapLocationListener() {
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
      if (null == aMapLocation)
        return;
      if (aMapLocation.getErrorCode() == 0) {
        //先暂时获得经纬度信息，并将其记录在List中
        LogUtils.d("纬度信息为" + aMapLocation.getLatitude() + "\n经度信息为" + aMapLocation.getLongitude());

        //定位成功
        updateLocation(aMapLocation);

      } else {
        String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
        LogUtils.e("AmapErr", errText);
      }
    }
  };

  private void updateLocation(AMapLocation aMapLocation) {
    //原始轨迹
//        if (mOriginList != null && mOriginList.size() > 0) {
//            mOriginPolyline = aMap.addPolyline(new PolylineOptions().addAll(mOriginList).color(Color.GREEN));
//            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(getBounds(mOriginList), 200));
//        }


    record.addPoint(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));

    //计算配速
    distance = getDistance(record.getPathLinePoints());

    double sportMile = distance / 1000d;
    //运动距离大于0.2公里再计算配速
    if (seconds > 0 && sportMile > 0.2) {
      double distribution = (double) seconds / 60d / sportMile;
      record.setDistribution(distribution);
      mTvSpeed.setText(decimalFormat.format(distribution));
      mTvMile.setText(decimalFormat.format(sportMile));
    } else {
      record.setDistribution(0d);
      mTvSpeed.setText(String.valueOf("0.00"));
      mTvMile.setText(String.valueOf("0.00"));
    }

    mSportLatLngs.clear();
    //轨迹平滑优化
    mSportLatLngs = new ArrayList<>(mpathSmoothTool.pathOptimize(record.getPathLinePoints()));
    //抽稀
//        mSportLatLngs = new ArrayList<>(mpathSmoothTool.reducerVerticalThreshold(MotionUtils.parseLatLngList(record.getPathline())));
    //不做处理
//        mSportLatLngs = new ArrayList<>(MotionUtils.parseLatLngList(record.getPathline()));

    if (!mSportLatLngs.isEmpty()) {
      polylineOptions.add(mSportLatLngs.get(mSportLatLngs.size() - 1));
      if (mListener != null)
        mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
//            aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(getBounds(mSportLatLngs), 18));
      aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 18));
    }
    mOriginPolyline = aMap.addPolyline(polylineOptions);
  }

  @Override
  protected void onDestroy() {
    mMapView.onDestroy();

    if (mTvNumber != null) { mTvNumber.clearAnimation(); }
    if (mTvFinish != null) { mTvFinish.clearAnimation(); }
    if (mTvPause != null) { mTvPause.clearAnimation(); }
    if (mTvContinue != null) { mTvContinue.clearAnimation(); }

    if (mRunnable != null) {
      mHandler.removeCallbacks(mRunnable);
      mRunnable = null;
    }

    unBindService();
    if (null != dataManager) dataManager.closeRealm();
    super.onDestroy();
  }


  private void unBindService() {
    //屏幕取消常亮
    if (null != mMapView)
      sportContent.setKeepScreenOn(false);

    //停止定位
    if (null != mLocationClient) {
      mLocationClient.stopLocation();
      mLocationClient.unRegisterLocationListener(aMapLocationListener);
      mLocationClient.onDestroy();
      mLocationClient = null;
    }
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    if (hasFocus) {
      setAppearAnimation();
      setHiddenAnimation();
    }
    super.onWindowFocusChanged(hasFocus);
  }

  private interface TipCallBack {
    void confirm();

    void cancel();
  }
}
