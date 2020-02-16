package com.example.nirmol_nogori;

public class User {

    private String first_name, last_name, user_email;


    public User(String first_name, String last_name, String user_email) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.user_email = user_email;
    }

    public User() {

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
