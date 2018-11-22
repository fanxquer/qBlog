package com.fanxquer.qblog.entity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BlogArticle {
    private int id;
    private String user_name;
    private String title;
    private String desc_text;
    private String content_md;
    private String content_html;
    private int category;
    private Date date_publish;
    private int click_count;
    private boolean is_draft;
    private boolean is_public;

    public BlogArticle() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
//        this.date_publish = sdf.format(new Date());
        this.date_publish = new Date();
        this.is_draft = false;
        this.is_public = false;
    }

    public boolean isIs_public() {
        return is_public;
    }

    public void setIs_public(boolean is_public) {
        this.is_public = is_public;
    }

    public boolean isIs_draft() {
        return is_draft;
    }

    public void setIs_draft(boolean is_draft) {
        this.is_draft = is_draft;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc_text() {
        return desc_text;
    }

    public void setDesc_text(String desc_text) {
        this.desc_text = desc_text;
    }

    public String getContent_md() {
        return content_md;
    }

    public void setContent_md(String content_md) {
        this.content_md = content_md;
    }

    public String getContent_html() {
        return content_html;
    }

    public void setContent_html(String content_html) {
        this.content_html = content_html;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public Date getDate_publish() throws ParseException {
//        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = format.parse(date_publish);
        return date_publish;
    }

    public void setDate_publish(Date date_publish) {
        this.date_publish = date_publish;
    }

    public int getClick_count() {
        return click_count;
    }

    public void setClick_count(int click_count) {
        this.click_count = click_count;
    }
}

