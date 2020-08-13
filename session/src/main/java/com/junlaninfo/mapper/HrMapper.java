package com.junlaninfo.mapper;

import com.junlaninfo.model.Hr;
import com.junlaninfo.model.Role;

import java.util.List;

/**
 * Created by 辉 on 2020/8/11.
 */
public interface HrMapper {
    Hr loadUserByUsername(String s);

    List<Role> sselectRolesByHrId(Integer id);
}
