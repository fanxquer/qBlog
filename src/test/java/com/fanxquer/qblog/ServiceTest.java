package com.fanxquer.qblog;

import com.fanxquer.qblog.entity.SearchResult;
import com.fanxquer.qblog.service.impl.BlogArticleServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QBlogApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServiceTest {
    @Autowired
    BlogArticleServiceImpl blogArticleService;

    @Test
    public void search() {
        String key = "简介";
        List<SearchResult> results = blogArticleService.searchByKey(key);
        for (SearchResult result: results) {
            System.out.println(result.getTextPart());
        }
    }
}
