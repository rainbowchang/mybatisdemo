package com.example.mybatisdemo.entity;

import com.example.mybatisdemo.annotation.Column;
import com.example.mybatisdemo.annotation.Table;
import com.example.mybatisdemo.service.Entity;

@Table(name = "user", alias = "a", columns = {
        @Column(name = "id", attrName = "id", label = "id", isPK = true),
        @Column(name = "user_name", attrName = "userName", label = "用户名"),
        @Column(name = "pass_word", attrName = "passWord", label = "密码"),
        @Column(name = "real_name", attrName = "realName", label = "姓名"),
}, orderBy = "a.create_date ASC")
public class User extends Entity<User> {
    private static final long serialVersionUID = 1L;

    private String userName;
    private String passWord;
    private String realName;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", realName='" + realName + '\'' +
                super.toString() +
                '}';
    }

}
