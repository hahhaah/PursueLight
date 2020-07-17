package com.example.bottombartest.fragments.index;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.bottombartest.R;
import com.example.bottombartest.entity.LooperItem;
import com.example.bottombartest.entity.Weather;
import com.example.bottombartest.interfaces.WeatherRequest;
import com.example.bottombartest.view.LooperView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.bottombartest.utils.MyConstants.WEATHER_URL;

public class IndexFragment extends Fragment {

  private static final String TAG = "IndexFragment";
  private List<LooperItem> mData;
  private LooperView mLooperView;
  private View mRootView;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mRootView = inflater.inflate(R.layout.fragment_index,container,false);
    return mRootView;
  }

  //在onCreateView之后执行
  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    initTestData();
    initView();
  }

  private void initTestData() {
    mData = new ArrayList<>();
    mData.add(new LooperItem(R.mipmap.logo,"图片1的标题"));
    mData.add(new LooperItem(R.mipmap.logo,"图片2的标题"));
    mData.add(new LooperItem(R.mipmap.logo,"图片3的标题"));
    mData.add(new LooperItem(R.mipmap.logo,"图片4的标题"));
  }

  private void initView(){
    mLooperView = mRootView.findViewById(R.id.looper_view);
    mLooperView.setData(new LooperView.InnerPageAdapter() {
      @Override
      public int getDataSize() {
        return mData.size();
      }

      @Override
      protected View getItemView(ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //设置图片
        imageView.setImageResource(mData.get(position).getImgRsId());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layoutParams);
        return imageView;
      }
    },new LooperView.TitleBindListener() {
      @Override
      public String getTitle(int position) {
        return mData.get(position % mData.size()).getTitle();
      }
    });
  }

  //获取天气数据
  private void getWeatherData(){
    List<Weather> list = new ArrayList<>();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(WEATHER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    final WeatherRequest request = retrofit.create(WeatherRequest.class);
    Call<Weather> call = request.getWeatherInfo();
    call.enqueue(new Callback<Weather>() {
      @Override
      public void onResponse(Call<Weather> call, Response<Weather> response) {
        Weather weather = response.body();
        //todo:更新天气预报信息显示
        Log.d(TAG, "onResponse: "+weather.getDaily().size());
      }

      @Override
      public void onFailure(Call<Weather> call, Throwable t) {

      }
    });

  }

}