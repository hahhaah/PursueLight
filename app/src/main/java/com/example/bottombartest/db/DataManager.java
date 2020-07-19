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
public class DataManager implements DBHelper {

  private RealmHelper realmHelper;

  public DataManager(RealmHelper helper) {
    realmHelper = helper;
  }


  @Override
  public void insertSportRecord(SportMotionRecord record) {
    realmHelper.insertSportRecord(record);
  }

  @Override
  public void deleteSportRecord(SportMotionRecord record) {
    realmHelper.deleteSportRecord(record);
  }

  @Override
  public void deleteAllSportRecords() {
    realmHelper.deleteAllSportRecords();
  }

  @Override
  public List<SportMotionRecord> queryRecordList(String email) {
    return realmHelper.queryRecordList(email);
  }

  @Override
  public List<SportMotionRecord> queryRecordList(String email, String dateTag) {
    return realmHelper.queryRecordList(email,dateTag);
  }

  @Override
  public List<SportMotionRecord> queryRecordList() {
    return realmHelper.queryRecordList();
  }

  @Override
  public SportMotionRecord queryRecord(String email, long startTime, long endTime) {
    return realmHelper.queryRecord(email,startTime,endTime);
  }

  @Override
  public SportMotionRecord queryRecord(String email, String dateTag) {
    return realmHelper.queryRecord(email,dateTag);
  }

  @Override
  public void closeRealm() {
    realmHelper.closeRealm();
  }

  @Override
  public void insertAccount(UserAccount account) {
    realmHelper.insertAccount(account);
  }

  @Override
  public UserAccount queryAccount(String account) {
    return realmHelper.queryAccount(account);
  }

  @Override
  public boolean checkAccount(String account, String psd) {
    return realmHelper.checkAccount(account,psd);
  }

  @Override
  public boolean checkAccount(String account) {
    return realmHelper.checkAccount(account);

  }

  @Override
  public List<UserAccount> queryAllAccounts() {
    return realmHelper.queryAllAccounts();
  }
}
