package com.example.nirmol_nogori.Model;

public class Admin {
    private String password;
    private String imageurl;
    private String adminid;
    private String mobile;
    private String name;
    private String email;

    public Admin(String password, String imageurl, String adminid, String mobile, String name, String email) {
        this.password = password;
        this.imageurl = imageurl;
        this.adminid = adminid;
        this.mobile = mobile;
        this.name = name;
        this.email = email;
    }

    public Admin() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getAdminid() {
        return adminid;
    }

    public void setAdminid(String adminid) {
        this.adminid = adminid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
