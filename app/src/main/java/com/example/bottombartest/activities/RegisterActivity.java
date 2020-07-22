package com.example.bottombartest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ToastUtils;
import com.example.bottombartest.R;
import com.example.bottombartest.interfaces.UserService;
import com.example.bottombartest.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.bottombartest.utils.MyConstants.PASSWORD;
import static com.example.bottombartest.utils.MyConstants.USER_EMAIL;
import static com.example.bottombartest.utils.MyConstants.USER_NAME;
import static com.example.bottombartest.utils.MyConstants.ZG_API;

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
  private EditText mNameEdt;
  private EditText mPwdEdt;
  private EditText mConfirmEdt;
  private EditText mEmailEdt;
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
    mNameEdt = findViewById(R.id.reg_name_edit);
    mPwdEdt = findViewById(R.id.reg_pwd_edit);
    mConfirmEdt = findViewById(R.id.confirm_edit);
    mEmailEdt = findViewById(R.id.reg_email_edit);
    mSendBtn = findViewById(R.id.reg_send_code);
    mRegBtn = findViewById(R.id.register_btn);
    mRegBtn.setEnabled(false);
    mSendBtn.setEnabled(false);
  }

  private void updateBtnStatus(){
    boolean hasEmail = false;
    boolean hasPassword = false;
    boolean hasVerifyCode = false;
    if (!TextUtils.isEmpty(mNameEdt.getText())) {
      hasEmail = true;
      if(!isTimerStart)mSendBtn.setEnabled(true);
    }

    if (!TextUtils.isEmpty(mPwdEdt.getText()) && !TextUtils.isEmpty(mConfirmEdt.getText())) {
      hasPassword = true;
    }

    if (!TextUtils.isEmpty(mEmailEdt.getText())){
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
    mNameEdt.addTextChangedListener(watcher);

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

    //todo：向后端发起请求查询账号是否存在，
    mRegBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String name = mNameEdt.getText().toString();
        String email = mEmailEdt.getText().toString();
        String pwd = mPwdEdt.getText().toString();
        String confirm = mConfirmEdt.getText().toString();
        if(!confirm.equals(pwd)){
          Toast.makeText(RegisterActivity.this,"两次密码输入不一致！",Toast.LENGTH_SHORT).show();
          return;
        }
        if(pwd.length() < 6){
          Toast.makeText(RegisterActivity.this,"密码不能小于6位！",Toast.LENGTH_SHORT).show();
          return;
        }
        LogUtils.d(TAG,"name-->"+name+", pwd-->"+pwd+", email-->"+email);
        register(name,pwd,email);
      }
    });

  }

  private void register(final String name,final String pwd,final String email){
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ZG_API)
            .build();
    UserService service = retrofit.create(UserService.class);

    JSONObject obj = new JSONObject();
    try {
      obj.putOpt(PASSWORD,pwd);
      obj.putOpt(USER_EMAIL,email);
      obj.putOpt(USER_NAME,name);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    LogUtils.d(TAG, "json: "+obj.toString());
    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),obj.toString());
    Call<ResponseBody> call = service.register(requestBody);
    call.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        LogUtils.d(TAG, "msg: "+ response.message());
        if (response.code()==201) {
          //注册成功
          try {
            LogUtils.d(TAG, "body: "+ response.body().string());
            registerSuccess(name,pwd);
          } catch (IOException e) {
            ToastUtils.showShort("请稍后再试");
            e.printStackTrace();
          }
        } else if(response.code() == 400){
          ToastUtils.showShort("用户名已存在！");
        }
        Log.d(TAG, "onResponse: "+response.code());
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        ToastUtils.showShort("出了点小问题，请稍后再试");
      }
    });
  }

  private void registerSuccess(String name, String pwd) {
    ToastUtils.showShort("注册成功!");
    Intent data  = new Intent();
    data.putExtra(USER_NAME,name);
    setResult(RESULT_OK,data);
    finish();
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
