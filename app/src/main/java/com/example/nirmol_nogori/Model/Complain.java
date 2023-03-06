package com.example.nirmol_nogori.Model;

public class Complain {
    private String area;
    private String complainid;
    private String complainimage;
    private String date;
    private String details;
    private String userid;
    private String username;

    public Complain() {
    }

    public Complain(String area,String complainid, String complainimage, String date, String details, String userid,String username) {
        this.area = area;
        this.complainimage = complainimage;
        this.date = date;
        this.details = details;
        this.userid = userid;
        this.complainid=complainid;
        this.username=username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComplainid() {
        return complainid;
    }

    public void setComplainid(String complainid) {
        this.complainid = complainid;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getComplainimage() {
        return complainimage;
    }

    public void setComplainimage(String complainimage) {
        this.complainimage = complainimage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

}
