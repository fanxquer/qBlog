package com.fanxquer.qblog;

import com.fanxquer.qblog.dao.BlogArticleMapper;
import com.fanxquer.qblog.dao.BlogCommetMapper;
import com.fanxquer.qblog.dao.CategoryMapper;
import com.fanxquer.qblog.dao.UserMapper;
import com.fanxquer.qblog.entity.BlogArticle;
import com.fanxquer.qblog.entity.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QBlogApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DaoTest {

    @Autowired
    UserMapper userMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    BlogArticleMapper blogArticleMapper;

    @Autowired
    BlogCommetMapper blogCommetMapper;

    @Test
    public void test1() {
        System.out.println(userMapper.getByName("qiufukang").getPassword());
    }

    @Test
    public void Test2() {
        List<Category> list = categoryMapper.getAll();
        for (Category c: list) {
            System.out.println(c.getName());
        }
    }
    @Test
    public void search() {
        String key = "%java%";
        List<BlogArticle> as = blogArticleMapper.searchByContent(key);
        for (BlogArticle b: as) {
            System.out.println(b.getTitle());
        }
    }

    @Test
    public void comment() {

    }

}
