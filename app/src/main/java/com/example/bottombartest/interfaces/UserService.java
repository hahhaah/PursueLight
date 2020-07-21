package com.example.bottombartest.interfaces;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * 创建时间: 2020/07/21 17:34 <br>
 * 作者: xuziwei <br>
 * 描述:
 * God bless my code!!
 */
public interface UserService {

  @Headers("Content-Type: application/json")
  @POST("xinyan/resultCallBack")
  Call<Boolean> addUser(@Body RequestBody body);
}
