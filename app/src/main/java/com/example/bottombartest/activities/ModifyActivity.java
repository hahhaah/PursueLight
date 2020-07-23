package com.example.bottombartest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.SPUtils;
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

import static com.example.bottombartest.utils.MyConstants.HEIGHT;
import static com.example.bottombartest.utils.MyConstants.INTRO;
import static com.example.bottombartest.utils.MyConstants.NICKNAME;
import static com.example.bottombartest.utils.MyConstants.PASSWORD;
import static com.example.bottombartest.utils.MyConstants.USER_EMAIL;
import static com.example.bottombartest.utils.MyConstants.USER_ID;
import static com.example.bottombartest.utils.MyConstants.USER_NAME;
import static com.example.bottombartest.utils.MyConstants.WEIGHT;
import static com.example.bottombartest.utils.MyConstants.ZG_API;

/**
 * 创建时间: 2020/07/17 15:05 <br>
 * 作者: xuziwei <br>
 * 描述: 修改用户信息
 * God bless my code!!
 */
public class ModifyActivity extends AppCompatActivity {

  private static final String TAG = "ModifyActivity";
  private EditText mNameEdt;
  private EditText mIntroEdt;
  private EditText mWeightEdt;
  private EditText mHeightEdt;
  private Button mModBtn;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_modify_info);
    
    initView();
    initEvent();
  }



  private void initView() {
    mNameEdt = findViewById(R.id.name_edit);
    mIntroEdt = findViewById(R.id.input_intro);
    mModBtn = findViewById(R.id.sure_mod);
    mHeightEdt = findViewById(R.id.height_edit);
    mWeightEdt = findViewById(R.id.weight_edit);

    String name  = SPUtils.getInstance().getString(NICKNAME);
    String intro  = SPUtils.getInstance().getString(INTRO);
    String height  = SPUtils.getInstance().getString(HEIGHT);
    String weight  = SPUtils.getInstance().getString(WEIGHT);

    if(! TextUtils.isEmpty(name)) {
      mNameEdt.setText(name);
    }
    if(! TextUtils.isEmpty(intro)) {
      mIntroEdt.setText(intro);
    }

    if(! TextUtils.isEmpty(height)) {
      mHeightEdt.setText(height);
    }
    if(! TextUtils.isEmpty(weight)) {
      mWeightEdt.setText(weight);
    }
  }

  private void initEvent() {
    findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setResult(RESULT_OK);
        finish();
      }
    });

    mModBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        final String name = mNameEdt.getText().toString();
        final String intro = mIntroEdt.getText().toString();
        final String height = mHeightEdt.getText().toString();
        final String weight = mWeightEdt.getText().toString();
        LogUtils.d(TAG, "name--: "+name + "intro-->"+intro);
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(intro)) {
          modifyInfo(name,intro,height,weight);
        }
      }
    });
  }

  private void modifyInfo(final String name, final String intro,final String height,final String weight) {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ZG_API)
            .build();
    UserService service = retrofit.create(UserService.class);

    JSONObject obj = new JSONObject();
    try {
      obj.putOpt(INTRO,intro);
      obj.putOpt(NICKNAME,name);
      obj.putOpt(WEIGHT,weight);
      //obj.putOpt(PASSWORD,SPUtils.getInstance().getString(PASSWORD));
      obj.putOpt(HEIGHT,height);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    LogUtils.d(TAG, "json: "+obj.toString());
    int id = SPUtils.getInstance().getInt(USER_ID);
    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),obj.toString());
    Call<ResponseBody> call = service.modify(id,requestBody);
    call.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        LogUtils.d(TAG, "msg: "+ response.message());
        LogUtils.d(TAG, "onResponse: "+response.code());

        if (response.code()==200) {
          //注册成功
          try {
            LogUtils.d(TAG, "body: "+ response.body().string());
            modifySuccess(name,intro,height,weight);
          } catch (IOException e) {
            ToastUtils.showShort("请稍后再试");
            e.printStackTrace();
          }
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        ToastUtils.showShort("出了点小问题，请稍后再试");
      }
    });
  }

  private void modifySuccess(String name, String intro,String height,final String weight) {
    SPUtils.getInstance().put(INTRO,intro);
    ToastUtils.showShort("修改成功!");
    SPUtils.getInstance().put(INTRO,intro);
    SPUtils.getInstance().put(NICKNAME,name);
    SPUtils.getInstance().put(WEIGHT,weight);
    SPUtils.getInstance().put(HEIGHT,height);
    finish();
  }
}
