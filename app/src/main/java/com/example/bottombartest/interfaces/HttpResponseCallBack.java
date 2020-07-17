package com.example.bottombartest.interfaces;

/**
 * 创建时间: 2020/07/17 14:13 <br>
 * 作者: xuziwei <br>
 * 描述:
 * God bless my code!!
 */
public interface HttpResponseCallBack {

  void onResponseStart(String apiName);

  /**
   * 此回调只有调用download方法下载数据时才生效
   *
   * @param apiName
   * @param count
   * @param current
   */
   void onLoading(String apiName, long count, long current);

   void onSuccess(String apiName, Object object);

   void onFailure(String apiName, Throwable t, int errorNo, String strMsg);

}

