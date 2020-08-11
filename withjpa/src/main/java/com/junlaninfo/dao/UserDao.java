package com.junlaninfo.dao;

import com.junlaninfo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 辉 on 2020/8/3.
 */
public interface UserDao extends JpaRepository<User,Long> {
    User  findUserByUsername(String  username);
}
