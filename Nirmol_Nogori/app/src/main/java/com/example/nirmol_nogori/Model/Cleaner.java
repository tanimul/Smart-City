package com.example.nirmol_nogori.Model;

public class Cleaner {
    public String name;


    public String phoneno;
    public String imageurl;
    public String location;
    public String total_fair;
    public float rating;


    public Cleaner(String name, String imageurl, String location, String total_fair, float rating) {
        this.name = name;
        //this.phoneno = phoneno;
        this.imageurl = imageurl;
        this.location = location;
        this.total_fair = total_fair;
        this.rating = rating;
    }

    public Cleaner(String phoneno, String imageurl) {
        this.phoneno = phoneno;
        this.imageurl = imageurl;
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

    public String getTotal_fair() {
        return total_fair;
    }

    public float getRating() {
        return rating;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTotal_fair(String total_fair) {
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
}
