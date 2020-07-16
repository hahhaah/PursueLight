package com.example.bottombartest.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bottombartest.R;
import com.example.bottombartest.utils.UIHelper;

/*
 * author：xuziwei
 * date：2020-07-16 09:45
 * description：
 * God bless my code!!
 */
public class FindPwdActivity extends AppCompatActivity {

  private EditText mEmailEdt;
  private EditText mVerifyEdt;
  private Button mSureBtn;
  private Button mSendBtn;

  private boolean isTimerStart = false;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_find);

    initView();

    initEvent();

  }

  private void initView() {
    mEmailEdt = findViewById(R.id.find_email_edit);
    mVerifyEdt = findViewById(R.id.find_verify_edit);
    mSureBtn = findViewById(R.id.sure_btn);
    mSendBtn = findViewById(R.id.find_send_code);
    mSendBtn.setEnabled(false);
    mSureBtn.setEnabled(false);
  }

  private void initEvent() {

    TextWatcher watcher = new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void afterTextChanged(Editable editable) {
        updateBtnStatus();
      }
    };

    mEmailEdt.addTextChangedListener(watcher);
    mVerifyEdt.addTextChangedListener(watcher);

    mSendBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Toast.makeText(FindPwdActivity.this,"验证码已发送，请注意查收!",Toast.LENGTH_SHORT).show();
        mSendBtn.setEnabled(false);
        MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000,1000);
        myCountDownTimer.start();
        isTimerStart = true;
      }
    });

    //todo:验证码与后台下发是否一致，一致才跳转至修改界面
    mSureBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(FindPwdActivity.this,ModifyPwdActivity.class);
        startActivity(intent);
        finish();
      }
    });

  }

  private void updateBtnStatus() {
    boolean hasEmail = false;
    boolean hasCode = false;
    if (!TextUtils.isEmpty(mEmailEdt.getText())) {
      hasEmail = true;
      if(!isTimerStart)mSendBtn.setEnabled(true);
    }

    if (!TextUtils.isEmpty(mVerifyEdt.getText())) {
      hasCode = true;
    }
    if (hasEmail && hasCode) {
      mSureBtn.setEnabled(true);
    } else {
      mSureBtn.setEnabled(false);
    }
  }

  private class MyCountDownTimer extends CountDownTimer {

    public MyCountDownTimer(long millisInFuture, long countDownInterval) {
      super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long l) {
      mSendBtn.setClickable(false);
      mSendBtn.setText(l/1000+"秒");
      mSendBtn.setTextColor(Color.WHITE);
    }


    //计时完毕的方法
    @Override
    public void onFinish() {
      //重新给Button设置文字
      mSendBtn.setText("重新获取");
      //设置可点击
      mSendBtn.setClickable(true);
      isTimerStart = false;
    }
  }

}
