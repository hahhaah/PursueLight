package com.example.bottombartest.activities;

import androidx.annotation.Nullable;
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

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.bottombartest.R;
import com.example.bottombartest.interfaces.UserService;
import com.example.bottombartest.utils.LogUtils;
import com.example.bottombartest.utils.MyConstants;

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
import static com.example.bottombartest.utils.MyConstants.REQUEST_REGISTER;
import static com.example.bottombartest.utils.MyConstants.USER_NAME;
import static com.example.bottombartest.utils.MyConstants.ZG_API;

/*
 * author：xuziwei
 * description：登陆界面
 * God bless my code!!
 */
public class LoginActivity extends AppCompatActivity {

  private EditText mNameEdt;
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
    mNameEdt = findViewById(R.id.name_edit);
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

    //找回密码
    mFindPwdTv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(LoginActivity.this,FindPwdActivity.class);
        startActivity(intent);
      }
    });

    //立即注册
    mRegisterTv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivityForResult(intent,REQUEST_REGISTER);
      }
    });


    mLoginBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String name = mNameEdt.getText().toString();
        String pwd = mPwdEdt.getText().toString();
        login(name,pwd);
        LogUtils.d(TAG,"name-->"+name+", pwd-->"+pwd);
      }
    });

  }

  private void loginSuccess(String name, String pwd) {
    SPUtils.getInstance().put(MyConstants.IS_LOGIN, true);
    SPUtils.getInstance().put(USER_NAME, name);
    SPUtils.getInstance().put(PASSWORD, pwd);

    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
    startActivity(intent);
    finish();
  }

  private void updateBtnStatus(){
    boolean hasInput = false;
    if (!TextUtils.isEmpty(mNameEdt.getText()) && !TextUtils.isEmpty(mPwdEdt.getText())) {
      hasInput = true;
    }

    if (hasInput) {
      mLoginBtn.setEnabled(true);
    } else {
      mLoginBtn.setEnabled(false);
    }
  }

  //向服务器发起请求查询用户
  private void login(final String name, final String pwd){
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ZG_API)
            .build();
    UserService service = retrofit.create(UserService.class);

    JSONObject obj = new JSONObject();
    try {
      obj.putOpt(PASSWORD,pwd);
      obj.putOpt(USER_NAME,name);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    LogUtils.d(TAG, "json: "+obj.toString());
    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),obj.toString());
    Call<ResponseBody> call = service.login(requestBody);
    call.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.code()==200) {
          //登录成功 获取id
          try {
            LogUtils.d(TAG, "msg: "+ response.body().string());
            loginSuccess(name,pwd);
          } catch (IOException e) {
            ToastUtils.showShort("请稍后再试");
            e.printStackTrace();
          }
        } else {
          ToastUtils.showShort("出了点小问题，请稍后再试");
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        ToastUtils.showShort("用户名或密码输入错误");
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if( requestCode == REQUEST_REGISTER && resultCode == RESULT_OK) {
      mNameEdt.setText(data.getStringExtra(USER_NAME));
    }
  }
}
