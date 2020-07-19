package com.example.bottombartest.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
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
import com.example.bottombartest.utils.MotionUtils;
import com.example.bottombartest.utils.MyConstants;
import com.example.bottombartest.utils.UIHelper;
import com.example.bottombartest.view.CustomPopWindow;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 创建时间: 2020/07/19 11:58 <br>
 * 作者: xuziwei <br>
 * 描述:
 * God bless my code!!
 */
public class SportResultActivity extends AppCompatActivity implements View.OnClickListener {

  public static String SPORT_START = "SPORT_START";
  public static String SPORT_END = "SPORT_END";

  private ImageView mIvStar1;
  private ImageView mIvStar2;
  private ImageView mIvStar3;
  private TextView mTvResult;
  private TextView mTvDistance;
  private TextView mTvDuration;
  private TextView mTvCalorie;
  private MapView mapView;
  private RelativeLayout mRlBack;

  private LinearLayout mllShare;
  private LinearLayout mllDetail;


  private final int AMAP_LOADED = 0x0088;

  private Handler handler = new Handler(Looper.getMainLooper()) {

    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      switch (msg.what) {
        case AMAP_LOADED:
          setupRecord();
          break;
        default:
          break;
      }
    }
  };

  private AMap aMap;

  private PathRecord pathRecord = null;

  private DataManager dataManager = null;

  private ExecutorService mThreadPool;
  private List<LatLng> mOriginLatLngList;
  private Marker mOriginStartMarker, mOriginEndMarker;
  private Polyline mOriginPolyline;
  private PathSmoothTool mpathSmoothTool = null;
  private PolylineOptions polylineOptions;

  private DecimalFormat decimalFormat = new DecimalFormat("0.00");
  private DecimalFormat intFormat = new DecimalFormat("#");

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sport_result);

    initView();
    mapView.onCreate(savedInstanceState);// 此方法必须重写
    initData();
    initEvent();
  }

  private void initView() {
    mIvStar1 = findViewById(R.id.ivStar1);
    mIvStar2 = findViewById(R.id.ivStar2);
    mIvStar3 = findViewById(R.id.ivStar3);
    mTvResult = findViewById(R.id.tvResult);
    mTvDistance = findViewById(R.id.tvDistance);
    mTvDuration = findViewById(R.id.tvDuration);
    mTvCalorie = findViewById(R.id.tvCalorie);
    mllShare = findViewById(R.id.ll_share);
    mllDetail = findViewById(R.id.ll_details);
    mapView = findViewById(R.id.res_mapView);
    mRlBack = findViewById(R.id.re_back);
  }

  private void initData() {

    dataManager = new DataManager(new RealmHelper());

    if (!getIntent().hasExtra(SPORT_START) || !getIntent().hasExtra(SPORT_END)) {
      ToastUtils.showShort("参数错误!");
      finish();
    }

    int threadPoolSize = Runtime.getRuntime().availableProcessors() * 2 + 3;
    mThreadPool = Executors.newFixedThreadPool(threadPoolSize);

    initPolyline();

    if (aMap == null)
      aMap = mapView.getMap();

    setUpMap();
  }

  public void initEvent(){
    aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
      @Override
      public void onMapLoaded() {
        Message msg = handler.obtainMessage();
        msg.what = AMAP_LOADED;
        handler.sendMessage(msg);
      }
    });

    mTvResult.setOnClickListener(this);
    mllShare.setOnClickListener(this);
    mllDetail.setOnClickListener(this);
    mRlBack.setOnClickListener(this);
  }

  private void setUpMap() {
    MyLocationStyle myLocationStyle = new MyLocationStyle();
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
//                .fromResource(R.drawable.mylocation_point));// 设置小蓝点的图标
    myLocationStyle.strokeColor(Color.TRANSPARENT);// 设置圆形的边框颜色
    myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
    // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
    myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
    aMap.setMyLocationStyle(myLocationStyle);
    aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
    aMap.getUiSettings().setScaleControlsEnabled(true);// 设置比例尺显示
    aMap.getUiSettings().setZoomControlsEnabled(false);// 设置默认缩放按钮是否显示
    aMap.getUiSettings().setCompassEnabled(false);// 设置默认指南针是否显示
    aMap.setMyLocationEnabled(false);// 设置为true表示显示定位层并可触发定位
  }

  public static void startActivityWithData(Activity activity, long mStartTime, long mEndTime) {
    Intent intent = new Intent();
    intent.putExtra(SPORT_START, mStartTime);
    intent.putExtra(SPORT_END, mEndTime);
    intent.setClass(activity, SportResultActivity.class);
    activity.startActivity(intent);
  }

  private void initPolyline() {
    polylineOptions = new PolylineOptions();
    polylineOptions.color(getResources().getColor(R.color.colorAccent));
    polylineOptions.width(20f);
    polylineOptions.useGradient(true);

    mpathSmoothTool = new PathSmoothTool();
    mpathSmoothTool.setIntensity(4);
  }

  private void setupRecord() {
    try {
      SportMotionRecord records = dataManager.queryRecord(
              (SPUtils.getInstance().getString(MyConstants.USER_EMAIL, "123@qq.com")),
              getIntent().getLongExtra(SPORT_START, 0),
              getIntent().getLongExtra(SPORT_END, 0));
      if (null != records) {
        pathRecord = new PathRecord();
        pathRecord.setId(records.getId());
        pathRecord.setDistance(records.getDistance());
        pathRecord.setDuration(records.getDuration());
        pathRecord.setPathLinePoints(MotionUtils.parseLatLngLocations(records.getPathLine()));
        pathRecord.setStartPoint(MotionUtils.parseLatLngLocation(records.getStartPoint()));
        pathRecord.setEndPoint(MotionUtils.parseLatLngLocation(records.getEndPoint()));
        pathRecord.setStartTime(records.getStartTime());
        pathRecord.setEndTime(records.getEndTime());
        pathRecord.setCalorie(records.getCalorie());
        pathRecord.setSpeed(records.getSpeed());
        pathRecord.setDistribution(records.getDistribution());
        pathRecord.setDateTag(records.getDateTag());

        upDataUI();
      } else {
        pathRecord = null;
        ToastUtils.showShort("获取运动数据失败!");
      }
    } catch (Exception e) {
      pathRecord = null;
      ToastUtils.showShort("获取运动数据失败!");
      LogUtils.e("获取运动数据失败", e);
    }
  }

  private void upDataUI() {
    mTvDistance.setText(decimalFormat.format(pathRecord.getDistance() / 1000d));
    mTvDuration.setText(MotionUtils.formatSeconds(pathRecord.getDuration()));
    mTvCalorie.setText(intFormat.format(pathRecord.getCalorie()));

    //评分规则：依次判断 距离大于0 ★；运动时间大于40分钟 ★★；速度在3~6km/h之间 ★★★
    if (pathRecord.getDuration() > (40 * 60) && pathRecord.getSpeed() > 3) {
      mIvStar1.setImageResource(R.drawable.star);
      mIvStar2.setImageResource(R.drawable.star);
      mIvStar3.setImageResource(R.drawable.star);
      mTvResult.setText("跑步效果完美");
    } else if (pathRecord.getDuration() > (40 * 60)) {
      mIvStar1.setImageResource(R.drawable.star);
      mIvStar2.setImageResource(R.drawable.star);
      mIvStar3.setImageResource(R.drawable.no_star);
      mTvResult.setText("跑步效果不错");
    } else {
      mIvStar1.setImageResource(R.drawable.star);
      mIvStar2.setImageResource(R.drawable.no_star);
      mIvStar3.setImageResource(R.drawable.no_star);
      mTvResult.setText("跑步效果一般");
    }

    {
      List<LatLng> recordList = pathRecord.getPathLinePoints();
      LatLng startLatLng = pathRecord.getStartPoint();
      LatLng endLatLng = pathRecord.getEndPoint();
      if (recordList == null || startLatLng == null || endLatLng == null) {
        return;
      }
      mOriginLatLngList = mpathSmoothTool.pathOptimize(recordList);
      addOriginTrace(startLatLng, endLatLng, mOriginLatLngList);
    }
  }

  /**
   * 地图上添加原始轨迹线路及起终点、轨迹动画小人
   *
   * @param startPoint
   * @param endPoint
   * @param originList
   */
  private void addOriginTrace(LatLng startPoint, LatLng endPoint,
                              List<LatLng> originList) {
    polylineOptions.addAll(originList);
    mOriginPolyline = aMap.addPolyline(polylineOptions);
    mOriginStartMarker = aMap.addMarker(new MarkerOptions().position(
            startPoint).icon(
            BitmapDescriptorFactory.fromResource(R.drawable.sport_start)));
    mOriginEndMarker = aMap.addMarker(new MarkerOptions().position(
            endPoint).icon(
            BitmapDescriptorFactory.fromResource(R.drawable.sport_end)));

    try {
      aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(getBounds(), 16));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private LatLngBounds getBounds() {
    LatLngBounds.Builder b = LatLngBounds.builder();
    if (mOriginLatLngList == null) {
      return b.build();
    }
    for (LatLng latLng : mOriginLatLngList) {
      b.include(latLng);
    }
    return b.build();
  }

  /**
   * 调用系统分享文本
   */
  private void systemShareTxt() {
    Intent intent = new Intent(Intent.ACTION_SEND);
    intent.setType("text/plain");
    intent.putExtra(Intent.EXTRA_SUBJECT, UIHelper.getString(R.string.app_name) + "运动");
    intent.putExtra(Intent.EXTRA_TEXT, "我在" + UIHelper.getString(R.string.app_name) + "运动跑了" + decimalFormat.format(pathRecord.getDistance())
            + "公里,运动了" + decimalFormat.format(pathRecord.getDuration() / 60) + "分钟!快来加入吧!");
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(Intent.createChooser(intent, "分享到"));
  }

  /**
   * 调用系统分享图片
   */
  private void systemSharePic(String imagePath) {
    //由文件得到uri
    Uri imageUri = Uri.fromFile(new File(imagePath));
    Intent shareIntent = new Intent();
    shareIntent.setAction(Intent.ACTION_SEND);
    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
    shareIntent.setType("image/*");
    startActivity(Intent.createChooser(shareIntent, "分享到"));
  }

  @Override
  protected void onDestroy() {
    mapView.onDestroy();

    if (mThreadPool != null)
      mThreadPool.shutdownNow();

    if (null != dataManager)
      dataManager.closeRealm();

    super.onDestroy();
  }

  @Override
  public void onResume() {
    mapView.onResume();
    super.onResume();
  }

  @Override
  public void onPause() {
    mapView.onPause();
    super.onPause();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }


  @Override
  public void onClick(View view) {
    switch (view.getId()){
      case R.id.tvResult:
        new CustomPopWindow.PopupWindowBuilder(this)
                .setView(R.layout.layout_sport_result_tip)
                .setFocusable(true)
                .setOutsideTouchable(true)
                .create()
                .showAsDropDown(mTvResult, -200, 10);
        break;
      case R.id.ll_share:
        if (null != pathRecord) {
          systemShareTxt();
        } else {
          ToastUtils.showShort("获取运动数据失败!");
        }
        break;
      case R.id.re_back:
        setResult(RESULT_OK);
        finish();
        break;
    }
  }
}
