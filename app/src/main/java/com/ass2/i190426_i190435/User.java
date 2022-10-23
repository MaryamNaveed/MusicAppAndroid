package com.ass2.i190426_i190435;

public class User {
    String name, dp, email;

    public User(String name, String dp, String email) {
        this.name = name;
        this.dp = dp;
        this.email = email;
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
