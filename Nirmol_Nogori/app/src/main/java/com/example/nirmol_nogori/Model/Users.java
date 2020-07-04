package com.example.nirmol_nogori.Model;

public class Users {

    private String first_name, last_name, user_email, user_image_url,phoneno,userid;

    public Users() {

    }

    public Users(String first_name, String last_name, String user_email, String user_image_url,String phoneno,String userid) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.user_email = user_email;
        this.user_image_url = user_image_url;
        this.phoneno=phoneno;
        this.userid=userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getUser_image_url() {
        return user_image_url;
    }

    public void setUser_image_url(String user_image_url) {
        this.user_image_url = user_image_url;
    }


    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

}
