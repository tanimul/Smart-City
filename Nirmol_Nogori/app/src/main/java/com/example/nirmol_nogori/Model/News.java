package com.example.nirmol_nogori.Model;

public class News {
    public String news_name;
    public String src;
    public String date;
    public String news_img_url;

    public News(String news_name, String src, String date, String news_img_url) {
        this.news_name = news_name;
        this.src = src;
        this.date = date;
        this.news_img_url = news_img_url;
    }

    public String getNews_name() {
        return news_name;
    }

    public void setNews_name(String news_name) {
        this.news_name = news_name;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
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
}
