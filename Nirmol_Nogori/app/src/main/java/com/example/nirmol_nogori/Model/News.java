package com.example.nirmol_nogori.Model;

public class News {
    private String adminid;
    private String date;
    private String news_img_url;
    private String newstitle;
    private String src;

    public News(String adminid, String date, String news_img_url, String newstitle, String src) {
        this.adminid = adminid;
        this.date = date;
        this.news_img_url = news_img_url;
        this.newstitle = newstitle;
        this.src = src;
    }

    public News() {
    }

    public String getAdminid() {
        return adminid;
    }

    public void setAdminid(String adminid) {
        this.adminid = adminid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNews_img_url() {
        return news_img_url;
    }

    public void setNews_img_url(String news_img_url) {
        this.news_img_url = news_img_url;
    }

    public String getNewstitle() {
        return newstitle;
    }

    public void setNewstitle(String newstitle) {
        this.newstitle = newstitle;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
