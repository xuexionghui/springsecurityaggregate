package com.junlaninfo.service;

import com.junlaninfo.dao.UserDao;
import com.junlaninfo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by 辉 on 2020/8/3.
 */
@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserDao userDao;

    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userDao.findUserByUsername(s);
        if (user == null) {
            throw new RuntimeException("没有这个用户");
        }

        return user;
    }
}
