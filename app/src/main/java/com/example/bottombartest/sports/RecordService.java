package com.example.bottombartest.sports;


import com.amap.api.maps.model.LatLng;

/**
 * 创建时间: 2020/07/18 12:17 <br>
 * 作者: xuziwei <br>
 * 描述: 记录运动信息的Service
 * God bless my code!!
 */
public interface RecordService {
  //记录运动坐标和大概描述信息
  void recordSport(LatLng latLng, String location);
}
