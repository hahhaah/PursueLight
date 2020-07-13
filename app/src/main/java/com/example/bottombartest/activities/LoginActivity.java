package com.example.bottombartest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bottombartest.R;
import com.example.bottombartest.utils.LogUtils;

public class LoginActivity extends AppCompatActivity {

  private EditText mEmailEdt;
  private EditText mPwdEdt;
  private Button mLoginBtn;
  private TextView mRegisterTv;
  private TextView mFindPwdTv;

  private static final String TAG = "LoginActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    initView();
    initEvent();
  }


  private void initView() {
    mEmailEdt = findViewById(R.id.email_edit);
    mPwdEdt = findViewById(R.id.pwd_edit);
    mLoginBtn = findViewById(R.id.login_btn);
    mLoginBtn.setEnabled(false);

    mRegisterTv = findViewById(R.id.go_register_tv);
    mFindPwdTv = findViewById(R.id.find_pwd);

    mRegisterTv.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );   //下划线
    mFindPwdTv.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
  }

  private void initEvent() {
    mRegisterTv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
      }
    });

    mEmailEdt.addTextChangedListener(new TextWatcher() {
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
    });

    mPwdEdt.addTextChangedListener(new TextWatcher() {
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
    });

    //找回密码
    mFindPwdTv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
      }
    });

    //todo 向后端服务器发起查询
    mLoginBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String email = mEmailEdt.getText().toString();
        String pwd = mPwdEdt.getText().toString();
        if(email.equals("123@qq.com") && pwd.equals("123")) {
          Intent intent = new Intent(LoginActivity.this, MainActivity.class);
          startActivity(intent);
          LoginActivity.this.finish();
        }
        LogUtils.d(TAG,"email-->"+email+", pwd-->"+pwd);
      }
    });

  }

  private void updateBtnStatus(){
    boolean hasUsername = false;
    boolean hasPassword = false;
    if (!TextUtils.isEmpty(mEmailEdt.getText())) {
      hasUsername = true;
    }

    if (!TextUtils.isEmpty(mPwdEdt.getText())) {
      hasPassword = true;
    }
    if (hasUsername && hasPassword) {
      mLoginBtn.setEnabled(true);
    } else {
      mLoginBtn.setEnabled(false);
    }
  }


}
