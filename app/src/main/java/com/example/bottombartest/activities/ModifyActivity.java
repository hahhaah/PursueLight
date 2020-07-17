package com.example.bottombartest.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bottombartest.R;

/**
 * 创建时间: 2020/07/17 15:05 <br>
 * 作者: xuziwei <br>
 * 描述:
 * God bless my code!!
 */
public class ModifyActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_modify_info);
    
    initView();
  }

  private void initView() {
  }
}
