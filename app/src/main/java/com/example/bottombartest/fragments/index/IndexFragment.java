package com.example.bottombartest.fragments.index;

import android.graphics.Rect;
 import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottombartest.R;
import com.example.bottombartest.activities.RecordActivity;
import com.example.bottombartest.adapter.WeatherAdapter;
import com.example.bottombartest.entity.LooperItem;
import com.example.bottombartest.entity.Weather;
import com.example.bottombartest.interfaces.UserService;
import com.example.bottombartest.interfaces.WeatherRequest;
import com.example.bottombartest.utils.MyConstants;
import com.example.bottombartest.utils.UIHelper;
import com.example.bottombartest.view.LooperView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
  private RecyclerView mRecyclerView;
  private WeatherAdapter mWeatherAdapter;

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
    if(savedInstanceState == null){
      getWeatherData();
    }
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

    mRecyclerView = mRootView.findViewById(R.id.weather_recyclerview);
    LinearLayoutManager manager = new LinearLayoutManager(getActivity());
    manager.setOrientation(RecyclerView.VERTICAL);
    mRecyclerView.setLayoutManager(manager);

    mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
          outRect.top = UIHelper.dip2px(mRootView.getContext(),6);
          outRect.bottom = UIHelper.dip2px(mRootView.getContext(),5);
          outRect.left = UIHelper.dip2px(mRootView.getContext(),5);
        }
    });

    //设置适配器
    mWeatherAdapter = new WeatherAdapter();
    mRecyclerView.setAdapter(mWeatherAdapter);
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

        mWeatherAdapter.setData(weather.getDaily());

        Log.d(TAG, "onResponse: "+weather.getDaily().size());
      }

      @Override
      public void onFailure(Call<Weather> call, Throwable t) {

      }
    });

  }

}