package com.fanxquer.qblog.service.impl;

import com.fanxquer.qblog.dao.BlogArticleMapper;
import com.fanxquer.qblog.dao.CategoryMapper;
import com.fanxquer.qblog.entity.Category;
import com.fanxquer.qblog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    BlogArticleMapper blogArticleMapper;

    @Override
    public Category getById(int id) {
        return categoryMapper.getById(id);
    }

    @Override
    public List<Category> getAll() {
        List<Category> categories = categoryMapper.getAll();
        setCountByCategory(categories);
        return categories;
    }

    @Override
    public List<Category> getPublicAll() {
        List<Category> categories = categoryMapper.getAll();
        setCountByCategory(categories);
        List<Category> result = new ArrayList<>();
        for (Category c : categories) {
            if (c.getCount() != 0) {
                result.add(c);
            }
        }
        return result;
    }

    @Override
    public void add(Category category) {
        categoryMapper.add(category);
    }

    /**
     * 统计分类并给对象设置属性值
     * @param categories
     */
    public void setCountByCategory(List<Category> categories) {
        for (int i = 0;i < categories.size();i++) {
            Category c = categories.get(i);
            int count = blogArticleMapper.countByCategory(c.getId());
            c.setCount(count);
            categories.set(i, c);
        }
    }
}
