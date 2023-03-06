package com.example.nirmol_nogori.Model;

public class Cleaner {
    private String publisher;
    private String name;
    private String phoneno;
    private String imageurl;
    private String location;
    private int total_fair;
    private float rating;
    private int total_hr;

    public Cleaner() {
    }


    public Cleaner(String name, String phoneno, String imageurl, String location,String publisher,float rating,int total_fair,int total_hr) {
        this.name = name;
        this.phoneno = phoneno;
        this.imageurl = imageurl;
        this.location = location;
        this.publisher=publisher;
        this.rating=rating;
        this.total_fair=total_fair;
        this.total_hr=total_hr;
    }


    public int getTotal_hr() {
        return total_hr;
    }

    public void setTotal_hr(int total_hr) {
        this.total_hr = total_hr;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getLocation() {
        return location;
    }

    public int getTotal_fair() {
        return total_fair;
    }

    public float getRating() {
        return rating;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTotal_fair(int total_fair) {
        this.total_fair = total_fair;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }


}
