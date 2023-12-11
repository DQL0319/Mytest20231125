package com.wms.mapper;

import com.wms.entity.RoleAuth;

import java.util.List;

public interface RoleAuthMapper {

    // 根据角色id删除角色权限关系的方法
    public int removeRoleAuthByRid(Integer roleId);

    // 根据角色id查询分配的所有权限菜单的方法
    public List<Integer> findAuthIdsByRid(Integer roleId);

    // 添加角色权限关系的方法
    public int insertRoleAuth(RoleAuth roleAuth);
}
