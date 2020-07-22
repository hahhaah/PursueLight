package com.example.bottombartest.entity;

import java.util.List;

/**
 * 创建时间: 2020/07/16 22:25 <br>
 * 作者: xuziwei <br>
 * 描述:
 * God bless my code!!
 */
public class Weather {

  /**
   * code : 200
   * updateTime : 2020-07-16T21:57+08:00
   * fxLink : http://hfx.link/2ax1
   * daily : [{"fxDate":"2020-07-16","sunrise":"05:00","sunset":"19:40","moonrise":"01:13","moonset":"15:36","moonPhase":"残月","tempMax":"33","tempMin":"25","iconDay":"101","textDay":"多云","iconNight":"302","textNight":"雷阵雨","wind360Day":"221","windDirDay":"西南风","windScaleDay":"3-4","windSpeedDay":"23","wind360Night":"178","windDirNight":"南风","windScaleNight":"1-2","windSpeedNight":"6","humidity":"53","precip":"0.0","pressure":"996","vis":"25","cloud":"25","uvIndex":"8"},{"fxDate":"2020-07-17","sunrise":"05:01","sunset":"19:39","moonrise":"01:46","moonset":"16:38","moonPhase":"残月","tempMax":"31","tempMin":"24","iconDay":"302","textDay":"雷阵雨","iconNight":"302","textNight":"雷阵雨","wind360Day":"205","windDirDay":"西南风","windScaleDay":"3-4","windSpeedDay":"19","wind360Night":"98","windDirNight":"东风","windScaleNight":"1-2","windSpeedNight":"7","humidity":"69","precip":"1.0","pressure":"998","vis":"24","cloud":"55","uvIndex":"1"},{"fxDate":"2020-07-18","sunrise":"05:02","sunset":"19:39","moonrise":"02:24","moonset":"17:40","moonPhase":"残月","tempMax":"30","tempMin":"24","iconDay":"302","textDay":"雷阵雨","iconNight":"305","textNight":"小雨","wind360Day":"161","windDirDay":"东南风","windScaleDay":"1-2","windSpeedDay":"2","wind360Night":"152","windDirNight":"东南风","windScaleNight":"1-2","windSpeedNight":"1","humidity":"83","precip":"0.0","pressure":"997","vis":"10","cloud":"24","uvIndex":"1"},{"fxDate":"2020-07-19","sunrise":"05:02","sunset":"19:38","moonrise":"03:12","moonset":"18:38","moonPhase":"残月","tempMax":"31","tempMin":"22","iconDay":"305","textDay":"小雨","iconNight":"150","textNight":"晴","wind360Day":"55","windDirDay":"东北风","windScaleDay":"1-2","windSpeedDay":"3","wind360Night":"301","windDirNight":"西北风","windScaleNight":"1-2","windSpeedNight":"2","humidity":"44","precip":"0.0","pressure":"994","vis":"24","cloud":"25","uvIndex":"2"},{"fxDate":"2020-07-20","sunrise":"05:03","sunset":"19:37","moonrise":"04:06","moonset":"19:33","moonPhase":"新月","tempMax":"33","tempMin":"22","iconDay":"100","textDay":"晴","iconNight":"150","textNight":"晴","wind360Day":"358","windDirDay":"北风","windScaleDay":"1-2","windSpeedDay":"11","wind360Night":"202","windDirNight":"西南风","windScaleNight":"1-2","windSpeedNight":"5","humidity":"46","precip":"0.0","pressure":"1001","vis":"25","cloud":"0","uvIndex":"10"},{"fxDate":"2020-07-21","sunrise":"05:04","sunset":"19:36","moonrise":"05:10","moonset":"20:20","moonPhase":"峨眉月","tempMax":"34","tempMin":"23","iconDay":"100","textDay":"晴","iconNight":"101","textNight":"多云","wind360Day":"155","windDirDay":"东南风","windScaleDay":"3-4","windSpeedDay":"23","wind360Night":"130","windDirNight":"东南风","windScaleNight":"3-4","windSpeedNight":"17","humidity":"46","precip":"0.0","pressure":"1002","vis":"25","cloud":"1","uvIndex":"10"},{"fxDate":"2020-07-22","sunrise":"05:05","sunset":"19:35","moonrise":"06:18","moonset":"21:02","moonPhase":"峨眉月","tempMax":"33","tempMin":"23","iconDay":"101","textDay":"多云","iconNight":"101","textNight":"多云","wind360Day":"107","windDirDay":"东南风","windScaleDay":"1-2","windSpeedDay":"11","wind360Night":"126","windDirNight":"东南风","windScaleNight":"1-2","windSpeedNight":"9","humidity":"59","precip":"0.0","pressure":"1004","vis":"25","cloud":"25","uvIndex":"10"}]
   * refer : {"sources":["Weather China"],"license":["no commercial use"]}
   */

  private String code;
  private String updateTime;
  private String fxLink;
  private ReferBean refer;
  private List<DailyBean> daily;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(String updateTime) {
    this.updateTime = updateTime;
  }

  public String getFxLink() {
    return fxLink;
  }

  public void setFxLink(String fxLink) {
    this.fxLink = fxLink;
  }

  public ReferBean getRefer() {
    return refer;
  }

  public void setRefer(ReferBean refer) {
    this.refer = refer;
  }

  public List<DailyBean> getDaily() {
    return daily;
  }

  public void setDaily(List<DailyBean> daily) {
    this.daily = daily;
  }

  public static class ReferBean {
    private List<String> sources;
    private List<String> license;

    public List<String> getSources() {
      return sources;
    }

    public void setSources(List<String> sources) {
      this.sources = sources;
    }

    public List<String> getLicense() {
      return license;
    }

    public void setLicense(List<String> license) {
      this.license = license;
    }
  }
}
