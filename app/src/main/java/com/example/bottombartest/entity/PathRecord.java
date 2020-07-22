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
public class PathRecord implements Parcelable {

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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LatLng getStartPoint() {
    return mStartPoint;
  }

  public void setStartPoint(LatLng startPoint) {
    mStartPoint = startPoint;
  }

  public LatLng getEndPoint() {
    return mEndPoint;
  }

  public void addPoint(LatLng point) {
    mPathLinePoints.add(point);
  }

  public void setEndPoint(LatLng endPoint) {
    mEndPoint = endPoint;
  }

  public List<LatLng> getPathLinePoints() {
    return mPathLinePoints;
  }

  public void setPathLinePoints(List<LatLng> pathLinePoints) {
    mPathLinePoints = pathLinePoints;
  }

  public Double getDistance() {
    return mDistance;
  }

  public void setDistance(Double distance) {
    mDistance = distance;
  }

  public Long getDuration() {
    return mDuration;
  }

  public void setDuration(Long duration) {
    mDuration = duration;
  }

  public Long getStartTime() {
    return mStartTime;
  }

  public void setStartTime(Long startTime) {
    mStartTime = startTime;
  }

  public Long getEndTime() {
    return mEndTime;
  }

  public void setEndTime(Long endTime) {
    mEndTime = endTime;
  }

  public Double getCalorie() {
    return mCalorie;
  }

  public void setCalorie(Double calorie) {
    mCalorie = calorie;
  }

  public Double getSpeed() {
    return mSpeed;
  }

  public void setSpeed(Double speed) {
    mSpeed = speed;
  }

  public Double getDistribution() {
    return mDistribution;
  }

  public void setDistribution(Double distribution) {
    mDistribution = distribution;
  }

  public String getDateTag() {
    return mDateTag;
  }

  public void setDateTag(String dateTag) {
    mDateTag = dateTag;
  }

  //消耗卡路里
  private Double mCalorie;
  //平均时速(公里/小时)
  private Double mSpeed;
  //平均配速(分钟/公里)
  private Double mDistribution;
  //日期标记
  private String mDateTag;

  public PathRecord() {

  }
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

  @Override
  public String toString() {
    return "PathRecord{" +
            "mDistance=" + mDistance +
            ", mDuration=" + mDuration +
            ", mCalorie=" + mCalorie +
            '}';
  }
}
