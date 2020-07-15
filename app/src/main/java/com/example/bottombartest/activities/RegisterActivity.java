package com.example.bottombartest.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
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
  private RadioButton mRadioBtn;
  private EditText mEmailEdt;
  private EditText mPwdEdt;
  private EditText mConfirmEdt;
  private Button mSendBtn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    initView();
    initEvent();
  }


  private void initView() {
    mRadioBtn = findViewById(R.id.radio_btn);
    mEmailEdt = findViewById(R.id.reg_email_edit);
    mPwdEdt = findViewById(R.id.reg_pwd_edit);
    mConfirmEdt = findViewById(R.id.confirm_edit);
    mSendBtn = findViewById(R.id.reg_send_code);
    mRegBtn = findViewById(R.id.register_btn);
    mRegBtn.setEnabled(false);
    mSendBtn.setEnabled(false);

  }

  private void updateBtnStatus(){
    boolean hasEmail = false;
    boolean hasPassword = false;
    if (!TextUtils.isEmpty(mEmailEdt.getText())) {
      hasEmail = true;
      mSendBtn.setEnabled(true);
    }

    if (!TextUtils.isEmpty(mPwdEdt.getText()) && !TextUtils.isEmpty(mConfirmEdt.getText())) {
      hasPassword = true;
    }

    if (hasEmail && hasPassword && mRadioBtn.isChecked()) {
      mRegBtn.setEnabled(true);
    } else {
      mRegBtn.setEnabled(false);
    }
  }

  private void initEvent() {
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

    mConfirmEdt.addTextChangedListener(new TextWatcher() {
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

    mSendBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Toast.makeText(RegisterActivity.this,"验证码已发送，请注意查收!",Toast.LENGTH_SHORT).show();
        //todo:样式改为 xx秒后重新获取
          mSendBtn.setEnabled(false);
      }
    });

    mRegBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String email = mEmailEdt.getText().toString();
        String pwd = mPwdEdt.getText().toString();
        String confirm = mConfirmEdt.getText().toString();
        LogUtils.d(TAG,"email-->"+email+", pwd-->"+pwd+", confirm-->"+pwd);
      }
    });

  }


}
