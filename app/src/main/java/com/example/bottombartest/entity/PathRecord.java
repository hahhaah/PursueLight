package com.example.bottombartest.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间: 2020/07/18 12:08 <br>
 * 作者: xuziwei <br>
 * 描述:
 * God bless my code!!
 */
class PathRecord implements Parcelable {

  //主键
  private Long id;
  //运动开始点
  private LatLng mStartPoint;
  //运动结束点
  private LatLng mEndPoint;
  //运动轨迹
  private List<LatLng> mPathLinePoints = new ArrayList<>();
  //运动距离
  private Double mDistance;
  //运动时长
  private Long mDuration;
  //运动开始时间
  private Long mStartTime;
  //运动结束时间
  private Long mEndTime;
  //消耗卡路里
  private Double mCalorie;
  //平均时速(公里/小时)
  private Double mSpeed;
  //平均配速(分钟/公里)
  private Double mDistribution;
  //日期标记
  private String mDateTag;
  protected PathRecord(Parcel in) {
  }

  public static final Creator<PathRecord> CREATOR = new Creator<PathRecord>() {
    @Override
    public PathRecord createFromParcel(Parcel in) {
      return new PathRecord(in);
    }

    @Override
    public PathRecord[] newArray(int size) {
      return new PathRecord[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
  }
}
