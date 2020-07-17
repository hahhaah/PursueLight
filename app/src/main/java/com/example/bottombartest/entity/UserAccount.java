package com.example.bottombartest.entity;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * 创建时间: 2020/07/17 12:07 <br>
 * 作者: xuziwei <br>
 * 描述: 用户账号实体类
 * God bless my code!!
 */
public class UserAccount extends RealmObject implements Serializable {

  @PrimaryKey
  private Long id;

  @Required
  private String email;

  @Required
  private String pwd;

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

  public String getPwd() {
    return pwd;
  }

  public void setPwd(String pwd) {
    this.pwd = pwd;
  }
}
