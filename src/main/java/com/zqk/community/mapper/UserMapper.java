package com.zqk.community.mapper;

import model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.w3c.dom.ls.LSOutput;

import java.sql.SQLOutput;

@Mapper
public interface UserMapper {
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")

    void insert(User user);

}
