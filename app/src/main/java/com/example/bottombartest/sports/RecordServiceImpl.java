package com.example.bottombartest.sports;

import android.content.Context;

import com.amap.api.maps.model.LatLng;
import com.example.bottombartest.utils.LogUtils;

/**
 * 创建时间: 2020/07/18 12:27 <br>
 * 作者: xuziwei <br>
 * 描述:
 * God bless my code!!
 */
public class RecordServiceImpl implements RecordService {

  private Context mContext;

  private static final String TAG = "RecordServiceImpl";

  public RecordServiceImpl(Context context) {
    this.mContext = context;
  }

  @Override
  public void recordSport(LatLng latLng, String location) {
    LogUtils.d(TAG,"保存定位数据 = " + latLng.latitude + ":" + latLng.longitude + "   " + location);
  }
}
