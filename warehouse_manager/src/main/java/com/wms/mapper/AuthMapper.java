package com.wms.mapper;

import com.wms.entity.Auth;

import java.util.List;

public interface AuthMapper {
    //根据用户id查询用户所有权限(菜单)的方法
    public List<Auth> findAuthByUid(Integer userId);
}
