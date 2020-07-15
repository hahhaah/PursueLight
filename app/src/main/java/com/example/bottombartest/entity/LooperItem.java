package com.example.bottombartest.entity;

/*
 * author：xuziwei
 * date：2020-07-15 22:27
 * description：
 * God bless my code!!
 */
public class LooperItem {

  private int resId;

  private String title;

  public LooperItem(int resId, String title) {
    this.resId = resId;
    this.title = title;
  }

  public int getImgRsId() {
    return resId;
  }

  public String getTitle() {
    return title==null?"":title;
  }
}
