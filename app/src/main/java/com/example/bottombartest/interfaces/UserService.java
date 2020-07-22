package com.example.bottombartest.interfaces;

import com.example.bottombartest.entity.User;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * 创建时间: 2020/07/21 17:34 <br>
 * 作者: xuziwei <br>
 * 描述:
 * God bless my code!!
 */
public interface UserService {

  @POST("users/users/")
  Call<User> register(@Body RequestBody body);

  @POST("login")
  Call<ResponseBody> login(@Body RequestBody body);

  @PATCH("users/users/{id}/")
  Call<ResponseBody> modify(@Path("id")int id,@Body RequestBody body);

  @GET("users/users/{id}")
  Call<ResponseBody> getUserInfo(@Path("id")int id);

}
