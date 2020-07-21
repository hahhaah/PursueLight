package com.example.bottombartest.adapter;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottombartest.MyApplication;
import com.example.bottombartest.R;
import com.example.bottombartest.entity.DailyBean;

import java.util.ArrayList;
import java.util.List;

import static com.example.bottombartest.utils.UIHelper.getPackageName;

/**
 * 创建时间: 2020/07/20 22:08 <br>
 * 作者: xuziwei <br>
 * 描述:
 * God bless my code!!
 */
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.InnerHolder> {

  private List<DailyBean> mWeatherList = new ArrayList<>();

  private ItemClickListener mItemClickListener = null;
  @NonNull
  @Override
  public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather,parent,false);
    return new InnerHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull InnerHolder holder, final int position) {
    View view = holder.itemView;
    DailyBean bean = mWeatherList.get(position);
    ImageView iconImg = view.findViewById(R.id.iv_weather);
    TextView textTv = view.findViewById(R.id.tv_date);
    TextView condTv = view.findViewById(R.id.tv_condition);
    TextView maxTv = view.findViewById(R.id.tv_temp_max);
    TextView minTv = view.findViewById(R.id.tv_temp_min);

    Log.d("xzw", "onBindViewHolder: "+bean.toString());
    textTv.setText(bean.getFxDate());
    iconImg.setImageDrawable(getDrawableByName("p"+bean.getIconDay()));
    condTv.setText(bean.getTextDay());
    maxTv.setText(bean.getTempMax()+"摄氏度");
    minTv.setText(bean.getTempMin()+"摄氏度");


    view.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (mItemClickListener != null) {
          mItemClickListener.onItemClick(mWeatherList,position);
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return mWeatherList.size();
  }

  public void setData(List<DailyBean> list) {
    mWeatherList.clear();
    mWeatherList.addAll(list);
    notifyDataSetChanged();
  }


  private Drawable getDrawableByName(String name) {
    Resources res = MyApplication.getAppContext().getResources();
    final String packageName = getPackageName();
    int imageResId = res.getIdentifier(name, "drawable", packageName);
    return MyApplication.getAppContext().getResources().getDrawable(imageResId);
  }


  public class InnerHolder extends RecyclerView.ViewHolder{

    public InnerHolder(@NonNull View itemView) {
      super(itemView);
    }
  }

  public interface ItemClickListener{
    void onItemClick(List<DailyBean> weatherList,int pos);
  }

  public void setOnItemClick(ItemClickListener listener){
    mItemClickListener = listener;
  }
}
