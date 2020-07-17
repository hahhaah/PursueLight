package com.example.bottombartest.interfaces;

import com.example.bottombartest.entity.Weather;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.GET;

/**
 * 创建时间: 2020/07/16 21:07 <br>
 * 作者: xuziwei <br>
 * 描述:天气的网络请求接口
 * God bless my code!!
 */
public interface WeatherRequest {

  @GET("weather/7d?location=101010100&key=d63dc874c9894eda8043b0b3a29caa0f")
  Call<Weather> getWeatherInfo();

  @GET("city/lookup?location=beijing&key=d63dc874c9894eda8043b0b3a29caa0f")
  Call<ResponseBody> getCityId();
}
