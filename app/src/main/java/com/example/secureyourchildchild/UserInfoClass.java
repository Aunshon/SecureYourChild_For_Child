package com.example.secureyourchildchild;

public class UserInfoClass {

    String phone,username,age,uid,device_token,user_type;

    public UserInfoClass(String phone, String username, String age, String uid, String device_token, String user_type) {
        this.phone = phone;
        this.username = username;
        this.age = age;
        this.uid = uid;
        this.device_token = device_token;
        this.user_type = user_type;
    }

    public UserInfoClass() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
