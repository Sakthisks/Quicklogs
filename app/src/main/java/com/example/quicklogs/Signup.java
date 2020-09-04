// class for the signp object
package com.example.quicklogs;

public class Signup {
    private String name;
    private String email;
    private String password;
    private String mobile;
    Signup (String name, String email, String password, String mobile) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
    }

    String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }



    String getPassword() {
        return password;
    }

    String getMobile() {
        return mobile;
    }

    void setName(String name) {
        this.name = name;
    }

    void setEmail(String email) {
        this.email = email;
    }

    void setPassword(String password) {
        this.password = password;
    }

    void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
