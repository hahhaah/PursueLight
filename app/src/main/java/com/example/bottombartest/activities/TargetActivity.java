package com.example.bottombartest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottombartest.R;

/**
 * 创建时间: 2020/07/17 11:26 <br>
 * 作者: xuziwei <br>
 * 描述: 目标界面
 * God bless my code!!
 */
public class TargetActivity extends AppCompatActivity {

  private static final String TAG = "TargetActivity";

  private TextView mTvGoCreate;
  private RecyclerView mRecyclerView;
  private FrameLayout mFlNoTarget;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_target);

    initView();
    initEvent();
  }

  private void initView() {
    mTvGoCreate = findViewById(R.id.go_create_target);
    mRecyclerView = findViewById(R.id.target_list);
    mFlNoTarget = findViewById(R.id.fl_no_target);
  }

  private void initEvent() {
    findViewById(R.id.ic_back).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setResult(RESULT_OK);
        finish();
      }
    });


  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

  }
}
