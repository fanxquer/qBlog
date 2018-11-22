package com.fanxquer.qblog.entity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BlogComment {
    private int id;
    private String content;
    private String user_name;
    private String url;
    private Date date_publish;
    private int article_id;
    private int pid = 0;

    public String getArticle_name() {
        return article_name;
    }

    public void setArticle_name(String article_name) {
        this.article_name = article_name;
    }

    private String article_name;

    public BlogComment() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
//        this.date_publish = sdf.format(new Date());
        this.date_publish = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        String c = content.replaceAll("\n", "<br>");
        return c;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDate_publish() throws ParseException {
//        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = format.parse(date_publish);
        return date_publish;
    }

    public void setDate_publish(Date date_publish) {
        this.date_publish = date_publish;
    }

    public int getArticle_id() {
        return article_id;
    }

    public void setArticle_id(int article_id) {
        this.article_id = article_id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
}
