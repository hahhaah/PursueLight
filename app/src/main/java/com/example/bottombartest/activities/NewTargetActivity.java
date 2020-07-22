package com.example.bottombartest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.bottombartest.R;
import com.example.bottombartest.utils.LogUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.bottombartest.utils.MyConstants.TARGET;
import static com.example.bottombartest.utils.MyConstants.TIME;

public class NewTargetActivity extends AppCompatActivity {

  private EditText mEtTarget;
  private Button mSureBtn;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_target);

    initView();
    initEvent();
  }


  private void initView() {
    mEtTarget = findViewById(R.id.target_edit);
    mSureBtn = findViewById(R.id.set_target);
  }

  private void initEvent() {
    findViewById(R.id.ic_back).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setResult(RESULT_OK);
        finish();
      }
    });


    mSureBtn.setOnClickListener(new View.OnClickListener() {


      @Override
      public void onClick(View view) {
        String target = mEtTarget.getText().toString();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String time  = formatter.format(curDate);
        Intent data = new Intent();
        data.putExtra(TARGET,target);
        data.putExtra(TIME,time);
        LogUtils.d("xzw","target-->"+target + "--"+time);
        ToastUtils.showShort("目标创建成功，努力实现吧～");
        setResult(RESULT_OK,data);
        finish();
      }
    });
  }
}