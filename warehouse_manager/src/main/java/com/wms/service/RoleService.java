package com.wms.service;

import com.wms.entity.Result;
import com.wms.entity.Role;
import com.wms.page.Page;

import java.util.List;

public interface RoleService {

    //    查询所有角色的业务方法
    public List<Role> queryAllRole();

    //    查询用户已分配的角色的业务方法
    public List<Role> queryUserRoleByUid(Integer userId);

    //    分页查询角色的业务方法
    public Page queryRolePage(Page page, Role role);

    //    添加角色的业务方法
    public Result saveRole(Role role);

    //    启用或禁用角色的方法
    public Result setRoleState(Role role);

}
