package com.ass2.i190426_i190435;

public class User {
    String id, name, dp, email, gender, num;

    public User(String id, String name, String dp, String email, String gender, String num) {
        this.id=id;
        this.name = name;
        this.dp = dp;
        this.email = email;
        this.gender = gender;
        this.num=num;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
