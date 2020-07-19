package com.example.bottombartest.db;


import com.example.bottombartest.entity.SportMotionRecord;
import com.example.bottombartest.entity.UserAccount;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * 创建时间: 2020/07/18 16:23 <br>
 * 作者: xuziwei <br>
 * 描述:
 * God bless my code!!
 */
public class RealmHelper implements DBHelper {

  private static final String DB_SPORT = "sport_motion.realm";//数据库名
  private static final String DB_ACCOUNT = "account.realm";//数据库名
  private static final String DB_KEY = "126xuziwei200047";//秘钥

  private Realm sportRealm;
  private Realm accountRealm;

  public RealmHelper() {
    if (sportRealm == null) {
      sportRealm  = Realm.getInstance(new RealmConfiguration.Builder()
              .deleteRealmIfMigrationNeeded()
              .schemaVersion(2)
              .name(DB_SPORT)
              .encryptionKey(getKey(DB_KEY))
              .build()
      );
    }

    if (accountRealm == null) {
      accountRealm = Realm.getInstance(new RealmConfiguration.Builder()
              .deleteRealmIfMigrationNeeded()//声明版本冲突时自动删除原数据库，开发时候打开
              .schemaVersion(1)//指定数据库的版本号
              .name(DB_ACCOUNT)//指定数据库的名称
              .encryptionKey(getKey(DB_KEY))
              .build()
      );
    }
  }

  private byte[] getKey(String dbKey) {
    StringBuilder newKey = new StringBuilder();
    for (int i = 0; i < 4; i++) {
      newKey.append(dbKey);
    }
    return newKey.toString().getBytes();
  }

  @Override
  public void insertSportRecord(final SportMotionRecord record) {
    /**
     * 当Model中存在主键的时候，推荐使用copyToRealmOrUpdate方法插入数据。
     * 如果对象存在，就更新该对象；否则，它会创建一个新的对象。
     */
    sportRealm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        record.setId(generateNewPrimaryKey());
        realm.copyToRealmOrUpdate(record);
      }
    });

  }

  //获取最大的PrimaryKey并加一,否则id不变，会覆盖已有记录
  private long generateNewPrimaryKey() {
    long primaryKey = 0;
    RealmResults<SportMotionRecord> results = sportRealm.where(SportMotionRecord.class).findAll();
    if (results != null && results.size() > 0) {
      SportMotionRecord last = results.last();
      primaryKey = last.getId() + 1;
    }
    return primaryKey;
  }

  @Override
  public void deleteSportRecord(final SportMotionRecord record) {
    sportRealm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        record.deleteFromRealm();
      }
    });
  }

  @Override
  public void deleteAllSportRecords() {
    sportRealm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        realm.deleteAll();
      }
    });
  }

  @Override
  public List<SportMotionRecord> queryRecordList(String email) {
    RealmResults<SportMotionRecord> results = sportRealm.where(SportMotionRecord.class).equalTo("email", email).findAll();
    return sportRealm.copyFromRealm(results);
  }

  @Override
  public List<SportMotionRecord> queryRecordList(String email, String dateTag) {
    RealmResults<SportMotionRecord> records = sportRealm.where(SportMotionRecord.class)
            .equalTo("email",email)
            .equalTo("dateTag",dateTag)
            .findAll();
    return sportRealm.copyFromRealm(records);
  }

  @Override
  public List<SportMotionRecord> queryRecordList() {
    RealmResults<SportMotionRecord> results = sportRealm.where(SportMotionRecord.class).findAll();
    return sportRealm.copyFromRealm(results);
  }

  @Override
  public SportMotionRecord queryRecord(String email, long startTime, long endTime) {
    return sportRealm.where(SportMotionRecord.class)
            .equalTo("email", email)
            .equalTo("mStartTime", startTime)
            .equalTo("mEndTime", endTime)
            .findFirst();
  }

  @Override
  public SportMotionRecord queryRecord(String email, String dateTag) {
    return sportRealm.where(SportMotionRecord.class)
            .equalTo("email", email)
            .equalTo("dateTag", dateTag)
            .findFirst();
  }

  @Override
  public void closeRealm() {
    if (null != sportRealm && !sportRealm.isClosed())
      sportRealm.close();
    if (null != accountRealm && !accountRealm.isClosed())
      accountRealm.close();
  }

  @Override
  public void insertAccount(final UserAccount account) {
    accountRealm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        realm.copyToRealm(account);
      }
    });
  }

  @Override
  public UserAccount queryAccount(String email) {
    return accountRealm.where(UserAccount.class)
            .equalTo("email", email)
            .findFirst();
  }

  @Override
  public boolean checkAccount(String email, String pwd) {
    return accountRealm.where(UserAccount.class)
            .equalTo("email", email)
            .equalTo("pwd", pwd)
            .findFirst() != null;
  }

  @Override
  public boolean checkAccount(String email) {
    return accountRealm.where(UserAccount.class)
            .equalTo("email", email)
            .findFirst() != null;
  }

  @Override
  public List<UserAccount> queryAllAccounts() {
    RealmResults<UserAccount> results = accountRealm.where(UserAccount.class).findAll();
    return accountRealm.copyFromRealm(results);
  }
}
