package com.fanxquer.qblog.dao;

import com.fanxquer.qblog.entity.BlogComment;
import com.fanxquer.qblog.util.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BlogCommetMapper {

    public List<BlogComment> getAll();

    public List<BlogComment> getAll(Page page);

    public BlogComment getById(int id);

    public List<BlogComment> getByPid(int pid);

    public List<BlogComment> getByArticleId(int article_id);

    public List<BlogComment> getByArticleId(@Param("article_id") int article_id, @Param("page") Page page);

    public void addComment(BlogComment blogComment);

    public void update(BlogComment blogComment);

    public void delete(int id);

    public int total();

    public int totalByArticleId();

}
