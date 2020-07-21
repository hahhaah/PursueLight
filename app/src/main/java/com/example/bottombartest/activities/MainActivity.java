package com.example.bottombartest.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.bottombartest.R;
import com.example.bottombartest.fragments.index.IndexFragment;
import com.example.bottombartest.fragments.mine.MineFragment;
import com.example.bottombartest.fragments.sport.SportFragment;
import com.example.bottombartest.utils.LogUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/*
 * author：xuziwei
 * description：主界面
 * God bless my code!!
 */

public class MainActivity extends AppCompatActivity {

  private IndexFragment mIndexFragment = new IndexFragment();
  private SportFragment mSportFragment = new SportFragment();
  private MineFragment mMineFragment = new MineFragment();

  private Fragment mCurFragment = mIndexFragment;
  private LinearLayout mIndexTab;
  private LinearLayout mMineTab;
  private LinearLayout mSportTab;

  private FragmentManager mFragmentManager;

  private static final String TAG = "MainActivity";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    initView();

    initEvent();
  }

  private void initView() {
    mIndexTab = findViewById(R.id.tab_index);
    mSportTab = findViewById(R.id.tab_sport);
    mMineTab = findViewById(R.id.tab_mine);
    mFragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction = mFragmentManager.beginTransaction();
    transaction.add(R.id.fragment_container,mIndexFragment);
    transaction.commit();
  }

  private void initEvent() {
    mIndexTab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        LogUtils.d(TAG,"index clicked");
        showFragment(mIndexFragment);
      }
    });

    mSportTab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        LogUtils.d(TAG,"sport clicked");
        if(mSportFragment == null){
          mSportFragment = new SportFragment();
        }
        showFragment(mSportFragment);
      }
    });

    mMineTab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        LogUtils.d(TAG,"mine clicked");
        if(mMineFragment == null){
          mMineFragment = new MineFragment();
        }
        showFragment(mMineFragment);
      }
    });

  }


  @Override
  protected void onResume() {
    super.onResume();
  }

  private void showFragment(Fragment fragment){
    if(fragment != mCurFragment){
      FragmentTransaction transaction = mFragmentManager.beginTransaction();
      transaction.hide(mCurFragment);
      if(fragment.isAdded()) {
        transaction.show(fragment).commit();
      } else {
        transaction.add(R.id.fragment_container,fragment).commit();
      }
      mCurFragment = fragment;
    }
  }



  private void showFragment(int index){
    switch (index) {
      case 0:
        showFragment(mIndexFragment);
        break;
      case 1:
        showFragment(mSportFragment);
        break;
      case 2:
        showFragment(mMineFragment);
        break;
      default:
        showFragment(mCurFragment);
    }
  }
}
