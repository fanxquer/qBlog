package com.fanxquer.qblog.service;

import com.fanxquer.qblog.entity.BlogComment;
import com.fanxquer.qblog.util.Page;

import java.util.List;

public interface BlogCommentService {

    public List<BlogComment> getAll();

    public List<BlogComment> getAll(Page page);

    public BlogComment getById(int id);

    public List<BlogComment> getByPid(int pid);

    public List<BlogComment> getByArticleId(int article_id);

    public List<BlogComment> getByArticleId(int article_id, Page page);

    public void addComment(BlogComment blogComment);

    public void update(BlogComment blogComment);

    public void delete(int id);

    public void checkAndDelete(int id);

    public int total();

    public int totalByArticleId();

}
