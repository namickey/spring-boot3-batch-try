package com.example.demo.common.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.example.demo.common.entity.Users;

@Mapper
public interface UsersMapper {

    @Insert("insert into users(id, name, department, created_at) values(#{id}, #{name}, #{department}, #{createdAt})")
    void regist(Users users);

    @Delete("truncate table users")
    void truncate();
}
