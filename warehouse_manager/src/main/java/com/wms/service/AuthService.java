package com.wms.service;

import com.wms.entity.Auth;

import java.util.List;

public interface AuthService {

    //    查询用户菜单树的业务方法
    public List<Auth> authTreeByUid(Integer userId);
}
