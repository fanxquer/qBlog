package com.fanxquer.qblog.dao;

import com.fanxquer.qblog.entity.BlogArticle;
import com.fanxquer.qblog.util.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BlogArticleMapper {

    public List<BlogArticle> getAll();

    public List<BlogArticle> getAll(Page page);

    public List<BlogArticle> getPublicArticles();

    public List<BlogArticle> getPublicArticles(Page page);

    public List<BlogArticle> getArticlesNotDraft(Page page);

    public List<BlogArticle> getDrafts(Page page);

    public int total();

    public int totalPublic();

    public BlogArticle getById(int id);

    public List<BlogArticle> getArticleByUserName(@Param("user_name") String user_name, @Param("start") int start, @Param("count") int count);

    public List<BlogArticle> getDraftByUserName(@Param("user_name") String user_name, @Param("start") int start, @Param("count") int count);

    public int countByUserName(String user_name);

    public int countArticleByUserName(String user_name);

    public int countDraftByUserName(String user_name);

    public List<BlogArticle> getByCategory(int category);

    public List<BlogArticle> getByCategory(@Param("category") int category, @Param("page") Page page);

    public void add(BlogArticle blogArticle);

    public int countByCategory(int category);

    public void update(BlogArticle blogArticle);

    public void delete(int id);

    public void addClick(int id);

    public List<BlogArticle> searchByKey(String key);

    public List<BlogArticle> searchByKey(@Param("key") String key, @Param("page") Page page);

    public int totalSearch(String key);

    public int totalByCategory(int category);

    public int totalNotDraft();

    public int totalDraft();


}
