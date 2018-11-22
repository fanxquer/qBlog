package com.fanxquer.qblog.dao;

import com.fanxquer.qblog.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CategoryMapper {

    public Category getById(int id);

    public List<Category> getAll();

    public void add(Category category);

}
