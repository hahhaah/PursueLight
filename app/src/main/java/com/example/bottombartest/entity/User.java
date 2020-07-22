package com.example.bottombartest.entity;

/**
 * 创建时间: 2020/07/22 16:57 <br>
 * 作者: xuziwei <br>
 * 描述:
 * God bless my code!!
 */
public class User {
  /**
   * code : 201
   * msg : ok
   * data : {"id":15,"username":"追光","last_name":"","gender":null,"email":"qq@qq.com","head_image":"http://39.103.132.187:8000/users/users/img/demo.png","authority":"普通用户","birthday":null,"intro":"这个用户很懒，什么都没有写","city":null,"weight":0,"height":0,"is_active":true,"date_joined":"2020-07-22 15:51:36","last_login":null}
   */

  private int code;
  private String msg;
  private DataBean data;

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public DataBean getData() {
    return data;
  }

  public void setData(DataBean data) {
    this.data = data;
  }

  public static class DataBean {
    @Override
    public String toString() {
      return "DataBean{" +
              "id=" + id +
              ", username='" + username + '\'' +
              ", last_name='" + last_name + '\'' +
              ", gender=" + gender +
              ", email='" + email + '\'' +
              ", head_image='" + head_image + '\'' +
              ", authority='" + authority + '\'' +
              ", birthday=" + birthday +
              ", intro='" + intro + '\'' +
              '}';
    }

    /**
     * id : 15
     * username : 追光
     * last_name :
     * gender : null
     * email : qq@qq.com
     * head_image : http://39.103.132.187:8000/users/users/img/demo.png
     * authority : 普通用户
     * birthday : null
     * intro : 这个用户很懒，什么都没有写
     * city : null
     * weight : 0.0
     * height : 0.0
     * is_active : true
     * date_joined : 2020-07-22 15:51:36
     * last_login : null
     */

    private int id;
    private String username;
    private String last_name;
    private Object gender;
    private String email;
    private String head_image;
    private String authority;
    private Object birthday;
    private String intro;
    private Object city;
    private double weight;
    private double height;
    private boolean is_active;
    private String date_joined;
    private Object last_login;

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getLast_name() {
      return last_name;
    }

    public void setLast_name(String last_name) {
      this.last_name = last_name;
    }

    public Object getGender() {
      return gender;
    }

    public void setGender(Object gender) {
      this.gender = gender;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public String getHead_image() {
      return head_image;
    }

    public void setHead_image(String head_image) {
      this.head_image = head_image;
    }

    public String getAuthority() {
      return authority;
    }

    public void setAuthority(String authority) {
      this.authority = authority;
    }

    public Object getBirthday() {
      return birthday;
    }

    public void setBirthday(Object birthday) {
      this.birthday = birthday;
    }

    public String getIntro() {
      return intro;
    }

    public void setIntro(String intro) {
      this.intro = intro;
    }

    public Object getCity() {
      return city;
    }

    public void setCity(Object city) {
      this.city = city;
    }

    public double getWeight() {
      return weight;
    }

    public void setWeight(double weight) {
      this.weight = weight;
    }

    public double getHeight() {
      return height;
    }

    public void setHeight(double height) {
      this.height = height;
    }

    public boolean isIs_active() {
      return is_active;
    }

    public void setIs_active(boolean is_active) {
      this.is_active = is_active;
    }

    public String getDate_joined() {
      return date_joined;
    }

    public void setDate_joined(String date_joined) {
      this.date_joined = date_joined;
    }

    public Object getLast_login() {
      return last_login;
    }

    public void setLast_login(Object last_login) {
      this.last_login = last_login;
    }
  }

  @Override
  public String toString() {
    return "User{" +
            "code=" + code +
            ", msg='" + msg + '\'' +
            ", data=" + data +
            '}';
  }
}
