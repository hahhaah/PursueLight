package com.example.bottombartest.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bottombartest.R;

/**
 * 创建时间: 2020/07/17 12:02 <br>
 * 作者: xuziwei <br>
 * 描述: 我的记录界面  有运动日历等
 * God bless my code!!
 */
public class RecordActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_record);

    initView();
  }

  private void initView() {

  }
}
