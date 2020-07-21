package com.example.bottombartest.interfaces;

import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 创建时间: 2020/07/21 17:34 <br>
 * 作者: xuziwei <br>
 * 描述:
 * God bless my code!!
 */
public interface UserService {

  @Headers("Content-Type: application/json")
  @POST("users")
  Call<ResponseBody> addUser(@Body RequestBody body);

  @GET("users/{id}")
  Call<ResponseBody> getUserInfo(@Path("id")int id);
}
