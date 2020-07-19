package com.example.bottombartest.entity;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * 创建时间: 2020/07/17 12:14 <br>
 * 作者: xuziwei <br>
 * 描述:
 * God bless my code!!
 */
public class SportMotionRecord extends RealmObject implements Serializable {

  /**
   * 表示该字段是主键
   * <p>
   * 字段类型必须是字符串（String）或整数（byte，short，int或long）
   * 以及它们的包装类型（Byte,Short, Integer, 或 Long）。不可以存在多个主键，
   * 使用字符串字段作为主键意味着字段被索引（注释@PrimaryKey隐式地设置注释@Index）。
   */
  @PrimaryKey
  private Long id;
  @Required
  private String email;
  //运动距离
  @Required
  private Double distance;
  //运动时长
  @Required
  private Long duration;
  //运动轨迹
  @Required
  private String pathLine;
  //运动开始点
  @Required
  private String startPoint;
  //运动结束点
  @Required
  private String endPoint;
  //运动开始时间
  @Required
  private Long mStartTime;
  //运动结束时间
  @Required
  private Long mEndTime;
  //消耗卡路里
  @Required
  private Double calorie;
  //平均时速(公里/小时)
  @Required
  private Double speed;


  //平均配速(分钟/公里)
  @Required
  private Double distribution;
  //日期标记
  @Required
  private String dateTag;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Double getDistance() {
    return distance;
  }

  public void setDistance(Double distance) {
    this.distance = distance;
  }

  public Long getDuration() {
    return duration;
  }

  public void setDuration(Long duration) {
    this.duration = duration;
  }

  public String getPathLine() {
    return pathLine;
  }

  public void setPathLine(String pathLine) {
    this.pathLine = pathLine;
  }

  public String getStartPoint() {
    return startPoint;
  }

  public void setStartPoint(String startPoint) {
    this.startPoint = startPoint;
  }

  public String getEndPoint() {
    return endPoint;
  }

  public void setEndPoint(String endPoint) {
    this.endPoint = endPoint;
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
    return calorie;
  }

  public void setCalorie(Double calorie) {
    this.calorie = calorie;
  }

  public Double getSpeed() {
    return speed;
  }

  public void setSpeed(Double speed) {
    this.speed = speed;
  }

  public Double getDistribution() {
    return distribution;
  }

  public void setDistribution(Double distribution) {
    this.distribution = distribution;
  }

  public String getDateTag() {
    return dateTag;
  }

  public void setDateTag(String dateTag) {
    this.dateTag = dateTag;
  }
}
