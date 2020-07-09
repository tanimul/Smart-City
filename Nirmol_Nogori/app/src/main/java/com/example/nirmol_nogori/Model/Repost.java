package com.example.nirmol_nogori.Model;

public class Repost {
    private String repostdetails;
    private String repostimage;
    private String userid;
    private String repostid;
    private String complainid;


    public Repost() {
    }

    public Repost(String repostdetails, String repostimage, String userid,String repostid,String complainid) {
        this.repostdetails = repostdetails;
        this.repostimage = repostimage;
        this.userid = userid;
        this.repostid=repostid;
        this.complainid=complainid;
    }

    public String getRepostid() {
        return repostid;
    }

    public void setRepostid(String repostid) {
        this.repostid = repostid;
    }

    public String getComplainid() {
        return complainid;
    }

    public void setComplainid(String complainid) {
        this.complainid = complainid;
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
