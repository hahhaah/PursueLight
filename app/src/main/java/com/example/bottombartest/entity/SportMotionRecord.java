package com.example.bottombartest.entity;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

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
}
