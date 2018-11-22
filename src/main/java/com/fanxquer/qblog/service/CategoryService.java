package com.fanxquer.qblog.service;

import com.fanxquer.qblog.entity.Category;

import java.util.List;

public interface CategoryService {

    public Category getById(int id);

    public List<Category> getAll();

    public List<Category> getPublicAll();

    public void add(Category category);

}
