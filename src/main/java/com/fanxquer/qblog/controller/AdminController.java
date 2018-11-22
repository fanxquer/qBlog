package com.fanxquer.qblog.controller;

import com.fanxquer.qblog.entity.BlogArticle;
import com.fanxquer.qblog.entity.BlogComment;
import com.fanxquer.qblog.entity.Category;
import com.fanxquer.qblog.entity.User;
import com.fanxquer.qblog.service.impl.BlogArticleServiceImpl;
import com.fanxquer.qblog.service.impl.BlogCommentServiceImpl;
import com.fanxquer.qblog.service.impl.CategoryServiceImpl;
import com.fanxquer.qblog.service.impl.UserServiceImpl;
import com.fanxquer.qblog.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    BlogArticleServiceImpl blogArticleService;
    @Autowired
    CategoryServiceImpl categoryService;
    @Autowired
    BlogCommentServiceImpl blogCommentService;

    /**
     * ajax请求，检查是否存在用户
     * @param name
     * @return 0代表不存在，1代表存在
     */
    @RequestMapping("/hasUser")
    @ResponseBody
    public int hasUser(String name){
        if (userService.countByName(name) == 0) {
            return 0;
        }
        else {
            return 1;
        }
    }

    /**
     * 登录界面
     * @return
     */
    @RequestMapping("/loginPage")
    public String loginPage() {
        return "login";
    }

    /**
     * 登录请求
     * @param user
     * @param request
     * @return
     */
    @RequestMapping("/login")
    public ModelAndView login(User user, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        User user1 = userService.getByName(user.getName());
        //用户存在且密码正确，将用户写入session，并转到管理界面
        if (user1 != null && user1.getPassword().equals(user.getPassword())) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            mav.setViewName("redirect:/showArticles");
        }
        //否则返回登录界面，传回密码错误信息
        else {
            mav.setViewName("login");
            mav.addObject("user", user);
            mav.addObject("result", "false");
        }
        return mav;
    }

    @RequestMapping("/logout")
    public ModelAndView logout(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("redirect:/loginPage");
        HttpSession session = request.getSession();
        if (userService.isUserInSession(request)) {
            session.removeAttribute("user");
        }
        return mav;
    }

    /**
     * 分页获取文章
     * @param page
     * @param request
     * @return
     */
    @RequestMapping("/showArticles")
    public ModelAndView showArticles(Page page, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        //判断用户是否登录
        if(!userService.isUserInSession(request)) {
            mav.setViewName("redirect:/loginPage");
            return mav;
        }
        //设置每页数量
        page.setCount(12);
        //计算最后页和越界处理
        page.caculateLast(blogArticleService.totalNotDraft());
        List<BlogArticle> articles = blogArticleService.getArticlesNotDraft(page);
        mav.addObject("articles", articles);
        mav.addObject("nav", 0);
        mav.setViewName("admin");
        return mav;
    }

    /**
     * 分页获取回复
     * @param page
     * @param request
     * @return
     */
    @RequestMapping("/showComments")
    public ModelAndView showComments(Page page, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        //判断用户是否登录
        if(!userService.isUserInSession(request)) {
            mav.setViewName("redirect:/loginPage");
            return mav;
        }

        //设置每页数量
        page.setCount(12);
        //计算最后页和越界处理
        page.caculateLast(blogCommentService.total());
        List<BlogComment> comments = blogCommentService.getAll(page);
        mav.addObject("comments", comments);
        mav.addObject("nav", 1);
        mav.setViewName("admin");
        return mav;
    }

    /**
     * 分页获取草稿
     * @param page
     * @param request
     * @return
     */
    @RequestMapping("/showDrafts")
    public ModelAndView showDrafts(Page page, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        //判断用户是否登录
        if(!userService.isUserInSession(request)) {
            mav.setViewName("redirect:/loginPage");
            return mav;
        }

        //设置每页数量
        page.setCount(12);
        //计算最后页和越界处理
        page.caculateLast(blogArticleService.totalDraft());
        List<BlogArticle> drafts = blogArticleService.getDrafts(page);
        mav.addObject("drafts", drafts);
        mav.addObject("nav", 2);
        mav.setViewName("admin");
        return mav;
    }

    /**
     * 新建文章界面
     * @return
     */
    @RequestMapping("/edit")
    public ModelAndView edit() {
        ModelAndView mav = new ModelAndView("edit");
        List<Category> categories = categoryService.getAll();
        mav.addObject("categories", categories);
        return mav;
    }

    /**
     * 保存文章
     * @param blogArticle
     * @param request
     * @return
     */
    @RequestMapping("/submitArticle")
    public ModelAndView submitArticle(BlogArticle blogArticle, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        //判断用户是否登录
        if(!userService.isUserInSession(request)) {
            mav.setViewName("redirect:/loginPage");
            return mav;
        }

        //从session获取用户
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        //设置文章作者
        blogArticle.setUser_name(user.getName());
        //获取并设置md格式文章正文
        blogArticle.setContent_md(request.getParameter("test-editormd-markdown-doc"));
        blogArticleService.add(blogArticle);
        mav.setViewName("redirect:/showArticles");
        return mav;
    }

    /**
     * 修改文章界面
     * @param id
     * @return
     */
    @RequestMapping("/updateEdit")
    public ModelAndView updateEdit(int id) {
        ModelAndView mav = new ModelAndView("updateEdit");
        List<Category> categories = categoryService.getAll();
        BlogArticle article = blogArticleService.getById(id);
        mav.addObject("article", article);
        mav.addObject("categories", categories);
        return mav;
    }

    /**
     * 修改文章
     * @param blogArticle
     * @param request
     * @return
     */
    @RequestMapping("/updateArticle")
    public ModelAndView updateArticle(BlogArticle blogArticle, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        //判断用户是否登录
        if(!userService.isUserInSession(request)) {
            mav.setViewName("redirect:/loginPage");
            return mav;
        }

        //获取并设置点击次数到新对象
        blogArticle.setClick_count(blogArticleService.getById(blogArticle.getId()).getClick_count());
        //获取并设置md格式文章正文
        blogArticle.setContent_md(request.getParameter("test-editormd-markdown-doc"));
        blogArticleService.update(blogArticle);
        mav.setViewName("redirect:/showArticles");
        return mav;
    }

    @RequestMapping("/deleteArticle")
    public ModelAndView deleteArticle(int id, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        //判断用户是否登录
        if(!userService.isUserInSession(request)) {
            mav.setViewName("redirect:/loginPage");
            return mav;
        }

        blogArticleService.delete(id);
        mav.setViewName("redirect:/showArticles");
        return mav;
    }

    @RequestMapping("/deleteComment")
    public ModelAndView deleteComment(int id, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        //判断用户是否登录
        if(!userService.isUserInSession(request)) {
            mav.setViewName("redirect:/loginPage");
            return mav;
        }

        //检查是否有下级回复，并进行处理，然后删除
        blogCommentService.checkAndDelete(id);
        mav.setViewName("redirect:/showComments");
        return mav;
    }

    /**
     * ajax请求，添加分类
     * @param category
     * @return 1代表添加成功
     */
    @RequestMapping("/addCategory")
    @ResponseBody
    public int addCategory(Category category) {
        categoryService.add(category);
        return 1;
    }
}
