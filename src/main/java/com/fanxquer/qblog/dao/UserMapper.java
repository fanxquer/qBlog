package com.fanxquer.qblog.dao;

import com.fanxquer.qblog.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Repository
@Mapper
public interface UserMapper {

    public User getByName(String name);

    public int countByName(String name);
}
