package com.example.bottombartest.fragments.sport;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.example.bottombartest.R;
import com.example.bottombartest.activities.SportMapActivity;
import com.example.bottombartest.db.DataManager;
import com.example.bottombartest.db.RealmHelper;
import com.example.bottombartest.entity.SportMotionRecord;
import com.example.bottombartest.utils.permission.PermissionHelper;
import com.example.bottombartest.utils.permission.PermissionListener;
import com.example.bottombartest.utils.permission.Permissions;

import java.text.DecimalFormat;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.example.bottombartest.utils.MyConstants.USER_EMAIL;

public class SportFragment extends Fragment {

  private View mRootView;
  private Button mStartBtn;
  private TextView mCountTv;
  private TextView mTimeTv;   //总时长
  private TextView mMileTv;   //总公里数

  private DecimalFormat decimalFormat = new DecimalFormat("0.00");

  private final int SPORT = 0x0012;

  private DataManager dataManager = null;
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mRootView = inflater.inflate(R.layout.fragment_sport,container,false);
    return mRootView;
  }

  //在onCreateView之后执行
  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    initView();

    initData();
  }



  private void initView() {
    mStartBtn = mRootView.findViewById(R.id.start_run_btn);
    mCountTv = mRootView.findViewById(R.id.tv_sport_count);
    mTimeTv = mRootView.findViewById(R.id.tv_sport_time);
    mMileTv = mRootView.findViewById(R.id.tv_sport_mile);

    mStartBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        PermissionHelper.requestPermissions(getActivity(), Permissions.PERMISSIONS_LOCATION,
          getResources().getString(R.string.app_name) + "需要获取位置", new PermissionListener() {
            @Override
            public void onPassed() {
              startActivityForResult(new Intent(getActivity(), SportMapActivity.class), SPORT);
            }
          });
      }
    });
  }

  private void initData() {
    dataManager = new DataManager(new RealmHelper());

    updateUI();
  }

  private void updateUI() {
    String email = SPUtils.getInstance().getString(USER_EMAIL);
    Log.d("xzw","email"+email);
    List<SportMotionRecord> records = dataManager.queryRecordList(email);

    if (records != null) {
      double sportMile = 0;
      long sportTime = 0;
      for (SportMotionRecord record : records) {
        sportMile += record.getDistance();
        sportTime += record.getDuration();
      }
      mMileTv.setText(decimalFormat.format(sportMile/1000d));
      mCountTv.setText(String.valueOf(records.size()));
      mTimeTv.setText(decimalFormat.format((double)sportTime/60d));
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode != RESULT_OK) return;

    if (requestCode == SPORT) {
      updateUI();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    updateUI();
  }

  @Override
  public void onDestroy() {
    if (dataManager != null) {
      dataManager.closeRealm();
    }
    super.onDestroy();
  }
}