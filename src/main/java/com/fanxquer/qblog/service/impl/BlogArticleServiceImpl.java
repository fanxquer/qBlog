package com.fanxquer.qblog.service.impl;

import com.fanxquer.qblog.dao.BlogArticleMapper;
import com.fanxquer.qblog.dao.CategoryMapper;
import com.fanxquer.qblog.entity.BlogArticle;
import com.fanxquer.qblog.entity.Category;
import com.fanxquer.qblog.entity.SearchResult;
import com.fanxquer.qblog.service.BlogArticleService;
import com.fanxquer.qblog.util.ColorUtil;
import com.fanxquer.qblog.util.ImageNode;
import com.fanxquer.qblog.util.ImageUtil;
import com.fanxquer.qblog.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BlogArticleServiceImpl implements BlogArticleService {

    @Autowired
    BlogArticleMapper blogArticleMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public List<BlogArticle> getAll() {
        return blogArticleMapper.getAll();
    }

    @Override
    public List<BlogArticle> getAll(Page page) {
        return blogArticleMapper.getAll(page);
    }

    @Override
    public List<BlogArticle> getPublicArticles() {
        return blogArticleMapper.getPublicArticles();
    }

    @Override
    public List<BlogArticle> getPublicArticles(Page page) {
        return blogArticleMapper.getPublicArticles(page);
    }

    @Override
    public List<BlogArticle> getArticlesNotDraft(Page page) {
        return blogArticleMapper.getArticlesNotDraft(page);
    }

    @Override
    public List<BlogArticle> getDrafts(Page page) {
        return blogArticleMapper.getDrafts(page);
    }

    @Override
    public int total() {
        return blogArticleMapper.total();
    }

    @Override
    public int totalPublic() {
        return blogArticleMapper.totalPublic();
    }

    @Override
    public BlogArticle getById(int id) {
        return blogArticleMapper.getById(id);
    }

    @Override
    public List<BlogArticle> getArticleByUserName(String user_name, int start, int count) {
        return blogArticleMapper.getArticleByUserName(user_name, start, count);
    }

    @Override
    public List<BlogArticle> getDraftByUserName(String user_name, int start, int count) {
        return blogArticleMapper.getDraftByUserName(user_name, start, count);
    }

    @Override
    public int countByUserName(String user_name) {
        return blogArticleMapper.countByUserName(user_name);
    }

    @Override
    public int countArticleByUserName(String user_name) {
        return blogArticleMapper.countArticleByUserName(user_name);
    }

    @Override
    public int countDraftByUserName(String user_name) {
        return blogArticleMapper.countDraftByUserName(user_name);
    }

    @Override
    public List<BlogArticle> getByCategory(int category) {
        return blogArticleMapper.getByCategory(category);
    }

    @Override
    public List<BlogArticle> getByCategory(int category, Page page) {
        return blogArticleMapper.getByCategory(category, page);
    }

    @Override
    public void add(BlogArticle blogArticle) {
        blogArticleMapper.add(blogArticle);
    }

    @Override
    public int countByCategory(int category) {
        return blogArticleMapper.countByCategory(category);
    }

    @Override
    public void update(BlogArticle blogArticle) {
        blogArticleMapper.update(blogArticle);
    }

    @Override
    public void delete(int id) {
        blogArticleMapper.delete(id);
    }

    @Override
    public void addClick(int id) {
        blogArticleMapper.addClick(id);
    }

    @Override
    public List<SearchResult> searchByKey(String key) {
        List<BlogArticle> articles = blogArticleMapper.searchByContent(key);
        List<SearchResult> results = new ArrayList<>();
        for (BlogArticle a: articles) {
            SearchResult result = new SearchResult();
            result.setArticleId(a.getId());
            result.setArticleTitle(a.getTitle());
            result.setKey(key);
            Category category = categoryMapper.getById(a.getCategory());
            result.setCategoryName(category.getName());
            result.setTextPart(getPartByKey(a.getContent_md(), key, 400));

            results.add(result);
        }
        return results;
    }

    @Override
    public List<SearchResult> searchByKey(String key, Page page) {
        List<BlogArticle> byTitle = blogArticleMapper.searchByTitle(key);
        List<BlogArticle> byDesc = blogArticleMapper.searchByDesc(key);
        List<BlogArticle> byContent = blogArticleMapper.searchByContent(key);
        List<BlogArticle> articles = addArticelList(byTitle, byDesc);
        articles = addArticelList(articles, byContent);
        List<SearchResult> results = new ArrayList<>();
        for (BlogArticle a: articles) {
            SearchResult result = new SearchResult();
            result.setArticleId(a.getId());
            result.setArticleTitle(a.getTitle());
            result.setKey(key);
            Category category = categoryMapper.getById(a.getCategory());
            result.setCategoryName(category.getName());
            result.setDesc(a.getDesc_text());
            result.setTextPart(getPartByKey(a.getContent_md(), key, 400));

            results.add(result);
        }
        page.caculateLast(results.size());
        int end = page.getStart()+page.getCount();
        if (end+1 > results.size()) {
            end = results.size();
        }
        results = results.subList(page.getStart(), end);
        return results;
    }

    @Override
    public int totalSearch(String key) {
        return blogArticleMapper.totalSearch(key);
    }

    @Override
    public int totalByCategory(int category) {
        return blogArticleMapper.totalByCategory(category);
    }

    @Override
    public int totalNotDraft() {
        return blogArticleMapper.totalNotDraft();
    }

    @Override
    public int totalDraft() {
        return blogArticleMapper.totalDraft();
    }

    /**
     * 根据关键词获取主导颜色列表
     * @param key
     * @return
     */
    @Override
    public List<ImageNode> getColorList(String key) {
        Date date1 = new Date();
        ImageUtil imageUtil = null;
        List<BufferedImage> BIs = null;
        try {
            //根据关键词构造图片获取类
            imageUtil = new ImageUtil(key);
            //获取10张图片
            BIs = imageUtil.getImageWithKey(10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<ImageNode> colors = new ArrayList<>();
        Date date2 = new Date();
        //遍历10张图片
        for (BufferedImage bufferedImage: BIs) {
            //从一张图片获取主导颜色列表
            List<ImageNode> nodes = ColorUtil.getColorFromOne(bufferedImage);
            //只保存列表中出现最多的颜色
            colors.add(nodes.get(0));
            for (ImageNode node: nodes) {
                System.out.println("Result:  r:"+node.r+" g:"+node.g+" b:"+node.b);
            }
        }
        //降低颜色饱和度
        for (int i = 0;i < colors.size();i++) {
            colors.set(i, ColorUtil.reduceSaturation(colors.get(i)));
//            colors.set(i, ColorUtil.balanceSaturation(colors.get(i)));
        }
        Date date3 = new Date();
        System.out.println("总耗时"+(date3.getTime()-date1.getTime())+"ms");
        System.out.println("聚类耗时"+(date3.getTime()-date2.getTime())+"ms");
        return colors;
    }

    public static String getPartByKey(String text, String key, int size) {
        size = size/2;
        int index = text.indexOf(key);
        int start = 0,end = 0;
        if (index > size) {
            start = index - size;
        }
        if (index+size > text.length()) {
            end = text.length()-1;
        } else {
            end = index + size - 1;
        }
        System.out.println("start:"+start+" end:"+end);
        return text.substring(start, end);
    }

    public static List<BlogArticle> addArticelList(List<BlogArticle> list1, List<BlogArticle> list2) {
        List<BlogArticle> list = list1;
        for (int i = 0;i < list2.size();i++) {
            boolean flag = true;
            for (int j = 0;j < list1.size();j++) {
                if (list2.get(i).getId() == list1.get(j).getId()) {
                    flag = false;
                }
            }
            if (flag) {
                list.add(list2.get(i));
            }
        }

        return list;
    }
}
