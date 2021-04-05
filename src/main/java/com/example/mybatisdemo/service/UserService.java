package com.example.mybatisdemo.service;

import com.example.mybatisdemo.entity.User;
import com.example.mybatisdemo.mapper.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends CrudService<UserDao, User> {

    @Autowired
    private UserDao userDao;

    public User Sel(String id){
        User user = userDao.Sel(id);
        return user;
    }

}
