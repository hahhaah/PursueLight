package com.example.bottombartest.entity;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * 创建时间: 2020/07/22 20:39 <br>
 * 作者: xuziwei <br>
 * 描述:
 * God bless my code!!
 */
public class Target extends RealmObject  implements Serializable {

  @PrimaryKey
  private Long id;

  @Required
  private String mTime;

  @Required
  private String mContent;

  private boolean mFinished;

  public Target() {
  }

  public Target(String time, String content) {
    mTime = time;
    mContent = content;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTime() {
    return mTime;
  }

  public void setTime(String time) {
    mTime = time;
  }

  public String getContent() {
    return mContent;
  }

  public void setContent(String content) {
    mContent = content;
  }

  public boolean isFinished() {
    return mFinished;
  }

  public void setFinished(boolean finished) {
    this.mFinished = finished;
  }

}
