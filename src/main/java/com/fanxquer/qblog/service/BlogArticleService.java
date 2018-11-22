package com.fanxquer.qblog.service;

import com.fanxquer.qblog.entity.BlogArticle;
import com.fanxquer.qblog.entity.SearchResult;
import com.fanxquer.qblog.util.ImageNode;
import com.fanxquer.qblog.util.Page;

import java.util.List;

public interface BlogArticleService {

    public List<BlogArticle> getAll();

    public List<BlogArticle> getAll(Page page);

    public List<BlogArticle> getPublicArticles();

    public List<BlogArticle> getPublicArticles(Page page);

    public List<BlogArticle> getArticlesNotDraft(Page page);

    public List<BlogArticle> getDrafts(Page page);

    public int total();

    public int totalPublic();

    public BlogArticle getById(int id);

    public List<BlogArticle> getArticleByUserName(String user_name, int start, int count);

    public List<BlogArticle> getDraftByUserName(String user_name, int start, int count);

    public int countByUserName(String user_name);

    public int countArticleByUserName(String user_name);

    public int countDraftByUserName(String user_name);

    public List<BlogArticle> getByCategory(int category);

    public List<BlogArticle> getByCategory(int category, Page page);

    public void add(BlogArticle blogArticle);

    public int countByCategory(int category);

    public void update(BlogArticle blogArticle);

    public void delete(int id);

    public void addClick(int id);

    public List<SearchResult> searchByKey(String key);

    public List<SearchResult> searchByKey(String key, Page page);

    public int totalSearch(String key);

    public int totalByCategory(int category);

    public int totalNotDraft();

    public int totalDraft();

    public List<ImageNode> getColorList(String key);

}
