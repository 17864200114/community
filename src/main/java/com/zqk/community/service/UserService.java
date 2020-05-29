package com.zqk.community.service;

import com.zqk.community.mapper.UserMapper;
import com.zqk.community.model.User;
import com.zqk.community.model.UserExample;
import org.hibernate.criterion.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public void createOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        if(users.size() == 0)
        {
            //插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else{
            //更新
            User dbUser = users.get(0);

            User updateUser = new User();
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser,example);
        }
    }

    public User login(String name, String password) {
        UserExample userExample  = new UserExample();
        userExample.createCriteria().andNameEqualTo(name)
                .andPasswordEqualTo(password);
        List<User> users = userMapper.selectByExample(userExample);
        if(users.size() != 0){
            return users.get(0);
        }
        else
            return null;
    }

    public boolean nameExists(String name) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNameEqualTo(name);
        List<User> user =userMapper.selectByExample(userExample);
        if(user.size()!=0)
            return true;
        else
            return false;
    }

    public User register(String name, String password) {
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtModified(user.getGmtCreate());
        user.setAvatarUrl("https://avatars3.githubusercontent.com/u/44757830?v=4");
        userMapper.insert(user);
        //通过token获取插入表之后的user.id值
        UserExample userExample = new UserExample();
        userExample.createCriteria().andTokenEqualTo(user.getToken());
        List<User> users = userMapper.selectByExample(userExample);
        user = users.get(0);
        return user;
    }


    public User edit(Long id, String bio, String password, String avatarUrl) {
        User user = new User();
        user.setId(id);
        user.setAvatarUrl(avatarUrl);
        user.setPassword(password);
        user.setBio(bio);
        userMapper.updateByPrimaryKeySelective(user);
        user= userMapper.selectByPrimaryKey(id);
        return user;
    }
}
