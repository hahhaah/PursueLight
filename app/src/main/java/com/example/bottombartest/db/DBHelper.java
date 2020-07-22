package com.example.bottombartest.db;

import com.example.bottombartest.entity.SportMotionRecord;
import com.example.bottombartest.entity.Target;
import com.example.bottombartest.entity.UserAccount;

import java.util.List;

/**
 * 创建时间: 2020/07/17 12:13 <br>
 * 作者: xuziwei <br>
 * 描述:
 * God bless my code!!
 */
interface DBHelper {

  /**
   * 增加 运动数据
   *
   * @param record 运动数据
   */
  void insertSportRecord(SportMotionRecord record);

  /**
   * 删除 运动数据
   *
   * @param record 运动数据
   */
  void deleteSportRecord(SportMotionRecord record);

  /**
   * 删除 全部运动数据
   */
  void deleteAllSportRecords();

  /**
   * 获取 某人的运动数据
   *
   * @param email    用户邮箱
   */
  List<SportMotionRecord> queryRecordList(String email);

  /**
   * 获取 某人的运动数据
   *
   * @param email    用户邮箱
   * @param dateTag 时间标记
   */
  List<SportMotionRecord> queryRecordList(String email, String dateTag);

  /**
   * 获取 全部的运动数据
   */
  List<SportMotionRecord> queryRecordList();

  /**
   * 获取 运动数据
   *
   * @param email    用户邮箱
   * @param startTime 开始时间
   * @param endTime   结束时间
   */
  SportMotionRecord queryRecord(String email, long startTime, long endTime);

  /**
   * 获取 运动数据
   *
   * @param email  用户邮箱
   * @param dateTag 时间标记
   */
  SportMotionRecord queryRecord(String email, String dateTag);

  /**
   * 关闭数据库
   */
  void closeRealm();

  /**
   * 增加 账号
   *
   * @param account 账号
   */
  void insertAccount(UserAccount account);

  /**
   * 获取 账号
   *
   * @param account 账号
   */
  UserAccount queryAccount(String account);

  /**
   * 查詢 账号
   *
   * @param account 账号
   * @param psd     密码
   */
  boolean checkAccount(String account, String psd);

  /**
   * 查詢 账号
   * @param account 账号
   */
  boolean checkAccount(String account);

  /**
   * 获取 全部的账号
   */
  List<UserAccount> queryAllAccounts();


  /**
   * 增加 目标
   *
   * @param target 账号
   */
  void insertTarget(Target target);

  /**
   * 删除 某一目标
   *
   * @param target 运动数据
   */
  void deleteTarget(Target target);

  /**
   * 获取 全部的目标
   */
  List<Target> queryAllTargets();

}
