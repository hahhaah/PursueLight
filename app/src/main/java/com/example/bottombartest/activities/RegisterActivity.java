package com.example.bottombartest.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bottombartest.R;
import com.example.bottombartest.utils.LogUtils;

/*
 * author：xuziwei
 * date：2020-07-13 13:40
 * description：
 * God bless my code!!
 */
public class RegisterActivity extends AppCompatActivity {

  private static final String TAG = "RegisterActivity";
  private Button mRegBtn;
  private CheckBox mCheckBox;
  private EditText mEmailEdt;
  private EditText mPwdEdt;
  private EditText mConfirmEdt;
  private EditText mVerifyEdt;
  private Button mSendBtn;

  private boolean isTimerStart = false;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    initView();
    initEvent();
  }


  private void initView() {
    mCheckBox = findViewById(R.id.res_checkbox);
    mEmailEdt = findViewById(R.id.reg_email_edit);
    mPwdEdt = findViewById(R.id.reg_pwd_edit);
    mConfirmEdt = findViewById(R.id.confirm_edit);
    mVerifyEdt = findViewById(R.id.verify_edit);
    mSendBtn = findViewById(R.id.reg_send_code);
    mRegBtn = findViewById(R.id.register_btn);
    mRegBtn.setEnabled(false);
    mSendBtn.setEnabled(false);
  }

  private void updateBtnStatus(){
    boolean hasEmail = false;
    boolean hasPassword = false;
    boolean hasVerifyCode = false;
    if (!TextUtils.isEmpty(mEmailEdt.getText())) {
      hasEmail = true;
      if(!isTimerStart)mSendBtn.setEnabled(true);
    }

    if (!TextUtils.isEmpty(mPwdEdt.getText()) && !TextUtils.isEmpty(mConfirmEdt.getText())) {
      hasPassword = true;
    }

    if (!TextUtils.isEmpty(mVerifyEdt.getText())){
      hasVerifyCode = true;
    }

    if (hasEmail && hasPassword && hasVerifyCode && mCheckBox.isChecked()) {
      mRegBtn.setEnabled(true);
    } else {
      mRegBtn.setEnabled(false);
    }
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

    mPwdEdt.addTextChangedListener(watcher);

    mConfirmEdt.addTextChangedListener(watcher);

    mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        updateBtnStatus();
      }
    });

    mSendBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Toast.makeText(RegisterActivity.this,"验证码已发送，请注意查收!",Toast.LENGTH_SHORT).show();
        mSendBtn.setEnabled(false);
        MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000,1000);
        myCountDownTimer.start();
        isTimerStart = true;
      }
    });

    mRegBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String email = mEmailEdt.getText().toString();
        String pwd = mPwdEdt.getText().toString();
        String confirm = mConfirmEdt.getText().toString();
        if(!confirm.equals(pwd)){
          Toast.makeText(RegisterActivity.this,"两次密码输入不一致！",Toast.LENGTH_SHORT).show();
        }
        LogUtils.d(TAG,"email-->"+email+", pwd-->"+pwd+", confirm-->"+confirm);
      }
    });

  }


  private class MyCountDownTimer extends CountDownTimer {

    public MyCountDownTimer(long millisInFuture, long countDownInterval) {
      super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long l) {
      mSendBtn.setClickable(false);
      mSendBtn.setText(l/1000+"秒");
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
