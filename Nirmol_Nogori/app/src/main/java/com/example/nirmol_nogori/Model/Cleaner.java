package com.example.nirmol_nogori.Model;

public class Cleaner {
    public String phoneno;
    public String imageurl;

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
}
