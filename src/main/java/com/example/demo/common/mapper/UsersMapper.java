package com.example.demo.common.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.example.demo.common.entity.Users;

/** ユーザマッパー */
@Mapper
public interface UsersMapper {

    @Select("select * from users")
    Users selectAll();
}
