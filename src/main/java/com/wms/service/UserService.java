package com.wms.service;

import com.wms.dto.AssignRoleDto;
import com.wms.entity.Result;
import com.wms.entity.Role;
import com.wms.entity.User;
import com.wms.page.Page;

import java.util.List;

/*
  user_info的service接口
  */
public interface UserService {
    // 根据账号查询用户的业务方法
    public User queryUserByCode(String userCode);

    // 分页查询用户的业务方法
    public Page queryUserByPage(Page page, User user);

    // 添加用户的业务方法
    public Result saveUser(User user);

    // 启用或禁用业务的方法
    public Result setUserState(User user);

    // 给用户分配角色的业务方法
    public void assignRole(AssignRoleDto assignRoleDto);

    // 根据用户id删除用户的业务方法
    public Result removeUserByIds(List<Integer> userIdList);

    // 修改用户的业务方法
    public Result setUserById(User user);

    // 根据用户id修改用户密码的方法
    public Result setPwdByUid(Integer userId);

}
