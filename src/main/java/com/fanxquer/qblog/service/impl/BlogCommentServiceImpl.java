package com.fanxquer.qblog.service.impl;

import com.fanxquer.qblog.dao.BlogArticleMapper;
import com.fanxquer.qblog.dao.BlogCommetMapper;
import com.fanxquer.qblog.entity.BlogComment;
import com.fanxquer.qblog.service.BlogCommentService;
import com.fanxquer.qblog.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogCommentServiceImpl implements BlogCommentService {
    @Autowired
    BlogCommetMapper blogCommetMapper;
    @Autowired
    BlogArticleMapper blogArticleMapper;

    @Override
    public List<BlogComment> getAll() {
        return blogCommetMapper.getAll();
    }

    @Override
    public List<BlogComment> getAll(Page page) {
        List<BlogComment> comments = blogCommetMapper.getAll(page);
        getArticleName(comments);
        return comments;
    }

    private void getArticleName(List<BlogComment> comments) {
        for (int i = 0;i < comments.size();i++) {
            BlogComment c = comments.get(i);
            String article_name = blogArticleMapper.getById(c.getArticle_id()).getTitle();
            c.setArticle_name(article_name);
            comments.set(i, c);
        }
    }

    @Override
    public BlogComment getById(int id) {
        return blogCommetMapper.getById(id);
    }

    @Override
    public List<BlogComment> getByPid(int pid) {
        return blogCommetMapper.getByPid(pid);
    }

    @Override
    public List<BlogComment> getByArticleId(int article_id) {
        return blogCommetMapper.getByArticleId(article_id);
    }

    @Override
    public List<BlogComment> getByArticleId(int article_id, Page page) {
        return blogCommetMapper.getByArticleId(article_id, page);
    }

    @Override
    public void addComment(BlogComment blogComment) {
        blogCommetMapper.addComment(blogComment);
    }

    @Override
    public void update(BlogComment blogComment) {
        blogCommetMapper.update(blogComment);
    }

    @Override
    public void delete(int id) {
        blogCommetMapper.delete(id);
    }

    @Override
    public void checkAndDelete(int id) {
        //查询回复该评论的评论
        List<BlogComment> comments = blogCommetMapper.getByPid(id);
        //如果存在，修改所有评论回复对象为-1，即"评论被删除"
        if (comments != null) {
            for (BlogComment comment : comments) {
                comment.setPid(-1);
                blogCommetMapper.update(comment);
            }
        }
        blogCommetMapper.delete(id);
    }

    @Override
    public int total() {
        return blogCommetMapper.total();
    }

    @Override
    public int totalByArticleId() {
        return blogCommetMapper.totalByArticleId();
    }
}
