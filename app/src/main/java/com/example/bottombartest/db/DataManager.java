package com.example.bottombartest.db;

import com.example.bottombartest.entity.SportMotionRecord;
import com.example.bottombartest.entity.UserAccount;

import java.util.List;

/**
 * 创建时间: 2020/07/17 12:12 <br>
 * 作者: xuziwei <br>
 * 描述:
 * God bless my code!!
 */
class DataManager implements DBHelper {
  @Override
  public void insertSportRecord(SportMotionRecord record) {

  }

  @Override
  public void deleteSportRecord(SportMotionRecord record) {

  }

  @Override
  public void deleteSportRecord() {

  }

  @Override
  public List<SportMotionRecord> queryRecordList(int master) {
    return null;
  }

  @Override
  public List<SportMotionRecord> queryRecordList(int master, String dateTag) {
    return null;
  }

  @Override
  public List<SportMotionRecord> queryRecordList() {
    return null;
  }

  @Override
  public SportMotionRecord queryRecord(int master, long startTime, long endTime) {
    return null;
  }

  @Override
  public SportMotionRecord queryRecord(int master, String dateTag) {
    return null;
  }

  @Override
  public void closeRealm() {

  }

  @Override
  public void insertAccount(UserAccount account) {

  }

  @Override
  public UserAccount queryAccount(String account) {
    return null;
  }

  @Override
  public boolean checkAccount(String account, String psd) {
    return false;
  }

  @Override
  public boolean checkAccount(String account) {
    return false;
  }

  @Override
  public List<UserAccount> queryAllAccounts() {
    return null;
  }
}
