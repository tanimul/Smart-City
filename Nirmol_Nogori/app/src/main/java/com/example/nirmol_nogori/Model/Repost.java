package com.example.nirmol_nogori.Model;

public class Repost {
    private String repostdetails;
    private String repostimage;
    private String userid;


    public Repost() {
    }

    public Repost(String repostdetails, String repostimage, String userid) {
        this.repostdetails = repostdetails;
        this.repostimage = repostimage;
        this.userid = userid;
    }

    public String getRepostdetails() {
        return repostdetails;
    }

    public void setRepostdetails(String repostdetails) {
        this.repostdetails = repostdetails;
    }

    public String getRepostimage() {
        return repostimage;
    }

    public void setRepostimage(String repostimage) {
        this.repostimage = repostimage;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
