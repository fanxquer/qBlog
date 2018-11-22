package com.fanxquer.qblog.controller;

import com.fanxquer.qblog.entity.*;
import com.fanxquer.qblog.entity.BlogArticle;
import com.fanxquer.qblog.entity.BlogComment;
import com.fanxquer.qblog.entity.Category;
import com.fanxquer.qblog.entity.SearchResult;
import com.fanxquer.qblog.service.impl.BlogArticleServiceImpl;
import com.fanxquer.qblog.service.impl.BlogCommentServiceImpl;
import com.fanxquer.qblog.service.impl.CategoryServiceImpl;
import com.fanxquer.qblog.service.impl.UserServiceImpl;
import com.fanxquer.qblog.util.*;
import com.fanxquer.qblog.util.CommentMatcher;
import com.fanxquer.qblog.util.ImageNode;
import com.fanxquer.qblog.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.*;

@Controller
public class FontEndController {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    BlogArticleServiceImpl blogArticleService;
    @Autowired CategoryServiceImpl categoryService;
    @Autowired BlogCommentServiceImpl blogCommentService;

    @RequestMapping("/test")
    public String test() {
        return "test";
    }

    @RequestMapping("/testPage")
    public String testPage() {
        return "/include/head";
    }

    /**
     * 主页，显示最近文章
     * @return
     */
    @RequestMapping({"/home", ""})
    public ModelAndView home() {
        List<BlogArticle> blogArticles = blogArticleService.getPublicArticles();
        //最多显示8篇文章
        if (blogArticles.size() > 8) {
            blogArticles = blogArticles.subList(0, 7);
        }
        //将文章列表分成两份发送(前端需求)
        int half = blogArticles.size()/2;
        List<BlogArticle> blogArticles1 = blogArticles.subList(0, half);
        List<BlogArticle> blogArticles2 = blogArticles.subList(half, blogArticles.size());
        List<Category> categories = categoryService.getPublicAll();
        ModelAndView mav = new ModelAndView("home");
        mav.addObject("blogArticles1", blogArticles1);
        mav.addObject("blogArticles2", blogArticles2);
        mav.addObject("categories", categories);
        return mav;
    }

    /**
     * 关于页面
     * @return
     */
    @RequestMapping("/about")
    public ModelAndView about() {
        ModelAndView mav = new ModelAndView("about");
        BlogArticle about = blogArticleService.getById(1);
        mav.addObject("about", about);
        return mav;
    }

    /**
     * 显示所有网络日志
     * @param page
     * @return
     */
    @RequestMapping("/listArticle")
    public ModelAndView listArticle(Page page) {
        page.setCount(6);
        page.caculateLast(blogArticleService.totalPublic());
        List<BlogArticle> blogArticles = blogArticleService.getPublicArticles(page);
        List<Category> categories = categoryService.getPublicAll();
        ModelAndView mav = new ModelAndView("listArticle");
        mav.addObject("blogArticles", blogArticles);
        mav.addObject("categories", categories);
        return mav;
    }

    /**
     * 显示文章详情及分页回复
     * @param id
     * @param page
     * @return
     */
    @RequestMapping("/showArticle")
    public ModelAndView showArticle(int id, Page page) {
        ModelAndView mav = new ModelAndView("showArticle");
        //设置每页数量
        page.setCount(10);
        List<Category> categories = categoryService.getPublicAll();
        List<BlogComment> comments = blogCommentService.getByArticleId(id, page);
        BlogArticle article = blogArticleService.getById(id);
        mav.addObject("blogArticle", article);
        mav.addObject("categories", categories);
        mav.addObject("comments", comments);
        blogArticleService.addClick(id);
        return mav;
    }

    /**
     * ajax请求，保存回复
     * @param blogComment
     * @return 1代表回复成功
     */
    @RequestMapping("/submitComment")
    @ResponseBody
    public int submitComment(BlogComment blogComment) {
        System.out.println("id:"+CommentMatcher.matcherId(blogComment.getContent()));
        System.out.println("text"+CommentMatcher.getText(blogComment.getContent()));
        blogComment.setPid(CommentMatcher.matcherId(blogComment.getContent()));
        blogComment.setContent(CommentMatcher.getText(blogComment.getContent()));
        blogCommentService.addComment(blogComment);
        return 1;
    }

//    @RequestMapping("/setStyle")
//    public String setStyle(String key, Model model) throws IOException {
//        Date date1 = new Date();
//        ImageUtil imageUtil = new ImageUtil(key);
//        List<BufferedImage> BIs = imageUtil.getImageWithKey(10);
//        List<ImageNode> colors = new ArrayList<>();
//        for (BufferedImage bufferedImage: BIs) {
//            List<ImageNode> nodes = ColorUtil.getColorFromOne(bufferedImage);
//            colors.add(nodes.get(0));
//            for (ImageNode node: nodes) {
//                System.out.println("Result:  r:"+node.r+" g:"+node.g+" b:"+node.b);
//            }
//        }
//        for (int i = 0;i < colors.size();i++) {
//            colors.set(i, ColorUtil.reduceSaturation(colors.get(i)));
//            colors.set(i, ColorUtil.balanceSaturation(colors.get(i)));
//        }
//        model.addAttribute("list", colors);
//        Date date2 = new Date();
//        System.out.println("耗时"+(date2.getTime()-date1.getTime())+"ms");
//        return "color";
//    }

    /**
     * ajax请求，根据关键字改变前端风格(爬取图片、聚类、颜色处理)
     * @param key
     * @return
     * @throws IOException
     */
    @RequestMapping("/submitStyle")
    @ResponseBody
    public Map<String, Object> submitStyle(String key) {
        List<ImageNode> colors = blogArticleService.getColorList(key);
        Map<String, Object> map = new HashMap<>();
        map.put("code", "1");
        map.put("list", colors);
        return map;
    }

    @RequestMapping("/testStyle")
    public String testStyle(String key, Model model) {
        List<ImageNode> colors = blogArticleService.getColorList(key);
        model.addAttribute("list", colors);
        return "color";
    }

    /**
     * 根据关键字搜索文章，分页显示
     * @param key
     * @param page
     * @return
     */
    @RequestMapping("/search")
    public ModelAndView searchByKey(String key, Page page) {
        //设置每页数量
        page.setCount(8);
        ModelAndView mav = new ModelAndView("searchResult");
        List<SearchResult> results = blogArticleService.searchByKey(key, page);
        mav.addObject("results", results);
        mav.addObject("key", key);
        return mav;
    }

    /**
     * 根据分类显示文章
     * @param category
     * @param page
     * @return
     */
    @RequestMapping("/articleByCategory")
    public ModelAndView articleByCategory(int category, Page page) {
        //设置每页数量
        page.setCount(6);
        //计算最后页和越界处理
        page.caculateLast(blogArticleService.totalByCategory(category));
        List<BlogArticle> blogArticles = blogArticleService.getByCategory(category, page);
        List<Category> categories = categoryService.getPublicAll();
        ModelAndView mav = new ModelAndView("listArticle");
        mav.addObject("categoryId", blogArticles.get(0).getCategory());
        mav.addObject("blogArticles", blogArticles);
        mav.addObject("categories", categories);
        return mav;
    }

}
