package com.example.bottombartest.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bottombartest.R;
import com.example.bottombartest.utils.permission.PermissionHelper;
import com.example.bottombartest.utils.permission.PermissionListener;
import com.example.bottombartest.view.CountDownProgressView;
import com.gyf.immersionbar.ImmersionBar;

/*
 * author：xuziwei
 * date：2020-07-13 17:39
 * description：
 * God bless my code!!
 */
public class SplashActivity extends AppCompatActivity {

  /**
   * 上次点击返回键的时间
   */
  private long lastBackPressed;
  /**
   * 上次点击返回键的时间
   */
  private static final int QUIT_INTERVAL = 3000;

  private ImageView mStartImg;
  private CountDownProgressView mProgressView;


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.layout_splash);
    initView();

    initData();

    initEvent();
  }

  private void initView() {
    mStartImg = findViewById(R.id.im_url);
    mStartImg.setImageResource(R.mipmap.cover);
    mProgressView = findViewById(R.id.countdownProgressView);
  }

  private void initData() {
    mProgressView.setTimeMillis(3000);
    mProgressView.setProgressType(CountDownProgressView.ProgressType.COUNT_BACK);
    mProgressView.start();
  }

  private static String[] PERMISSIONS_STORAGE = {
          Manifest.permission.WRITE_EXTERNAL_STORAGE,
          Manifest.permission.READ_EXTERNAL_STORAGE,
          Manifest.permission.ACCESS_FINE_LOCATION,
          Manifest.permission.ACCESS_COARSE_LOCATION};

  private void initEvent() {
    mProgressView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mProgressView.stop();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          // 获取权限
          PermissionHelper.requestPermissions(SplashActivity.this, PERMISSIONS_STORAGE, new PermissionListener() {
            @Override
            public void onPassed() {
              startActivity();
            }
          });
        } else {
          startActivity();
        }
      }
    });
  }

  public void startActivity() {
    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
    finish();
    mProgressView.stop();
  }

  //沉浸式
  protected boolean isImmersionBarEnabled() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
  }

  protected void initImmersionBar() {
    //在BaseActivity里初始化
    ImmersionBar.with(this).init();
  }
}
