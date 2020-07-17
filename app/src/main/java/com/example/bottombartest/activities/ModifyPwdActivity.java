package com.example.bottombartest.activities;

import android.os.Bundle;
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
import com.example.bottombartest.utils.LogUtils;

/*
 * author：xuziwei
 * date：2020-07-16 10:11
 * description：
 * God bless my code!!
 */
public class ModifyPwdActivity extends AppCompatActivity {

  private static final String TAG = "ModifyPwdActivity";
  private EditText mNewPwdEdt;
  private EditText mConfirmEdt;
  private Button mSureBtn;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_modify_pwd);

    initView();

    initEvent();
  }

  private void initView() {
    mNewPwdEdt = findViewById(R.id.input_new_pwd);
    mConfirmEdt = findViewById(R.id.mod_confirm_edit);
    mSureBtn = findViewById(R.id.mod_sure_btn);
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
    mNewPwdEdt.addTextChangedListener(watcher);
    mConfirmEdt.addTextChangedListener(watcher);

    //todo:修改密码
    mSureBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String newPwd = mNewPwdEdt.getText().toString();
        String confirm = mConfirmEdt.getText().toString();
        if(!newPwd.equals(confirm)){
          Toast.makeText(ModifyPwdActivity.this,"两次密码输入不一致！",Toast.LENGTH_SHORT).show();
        }
        LogUtils.d(TAG,"newPwd-->"+newPwd+",confirm--->"+confirm);
      }
    });
  }

  private void updateBtnStatus() {
    boolean hasInput = false;
    if (!TextUtils.isEmpty(mNewPwdEdt.getText()) && !TextUtils.isEmpty(mConfirmEdt.getText())) {
      hasInput = true;
    }

    String newPwd = mNewPwdEdt.getText().toString();
    String confirm = mConfirmEdt.getText().toString();

    if (hasInput) {
      mSureBtn.setEnabled(true);
    } else {
      mSureBtn.setEnabled(false);
    }
  }

}
