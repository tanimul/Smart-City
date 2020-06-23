package com.example.nirmol_nogori.Model;

public class Review {
    private String date;
    private int total_hour;
    private int total_fair;
    private String review;
    private float rating;
    private String userid;
    private String cleanername;
    private String location;
    private String imageurl;
    private String username;

    public Review(String date, int total_hour, int total_fair, String review,
                  float rating, String userid, String cleanername, String location, String imageurl, String username) {
        this.date = date;
        this.total_hour = total_hour;
        this.total_fair = total_fair;
        this.review = review;
        this.rating = rating;
        this.userid = userid;
        this.cleanername = cleanername;
        this.location = location;
        this.imageurl = imageurl;
        this.username = username;
    }

    public Review() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTotal_hour() {
        return total_hour;
    }

    public void setTotal_hour(int total_hour) {
        this.total_hour = total_hour;
    }

    public int getTotal_fair() {
        return total_fair;
    }

    public void setTotal_fair(int total_fair) {
        this.total_fair = total_fair;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCleanername() {
        return cleanername;
    }

    public void setCleanername(String cleanername) {
        this.cleanername = cleanername;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
