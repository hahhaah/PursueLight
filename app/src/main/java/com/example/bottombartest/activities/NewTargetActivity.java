package com.example.bottombartest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.blankj.utilcode.util.SPUtils;
import com.example.bottombartest.R;

public class NewTargetActivity extends AppCompatActivity {

  private EditText mEtTarget;
  private ImageView mIvSure;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_target);

    initView();
    initEvent();
  }


  private void initView() {
    mEtTarget = findViewById(R.id.target_edit);
    mIvSure = findViewById(R.id.iv_set_target);

  }

  private void initEvent() {
    findViewById(R.id.ic_back).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setResult(RESULT_OK);
        finish();
      }
    });


    mIvSure.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String target = mEtTarget.getText().toString();
        //todo 保存目标至数据库
      }
    });
  }
}