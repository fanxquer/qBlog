package com.fanxquer.qblog.entity;

public class SearchResult {
    private int articleId;
    private String articleTitle;
    private String categoryName;
    private String textPart;
    private String key;

    public int getArticleId() {
        return articleId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTextPart() {
        return textPart;
    }

    public void setTextPart(String textPart) {
        this.textPart = textPart;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
