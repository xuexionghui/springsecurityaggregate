package com.junlaninfo.service;

import com.junlaninfo.mapper.HrMapper;
import com.junlaninfo.model.Hr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by 辉 on 2020/8/11.
 */
@Service
public class HrService implements UserDetailsService {
    @Autowired
    HrMapper hrMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Hr hr = hrMapper.loadUserByUsername(s);
        if (hr == null) {
            throw new
                    UsernameNotFoundException("没有这个用户");
        }
        hr.setRoles(hrMapper.sselectRolesByHrId(hr.getId()));
        return  hr;
    }
}
