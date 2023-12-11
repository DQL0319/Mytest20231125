package com.wms.service.impl;

import com.wms.dto.AssignRoleDto;
import com.wms.entity.Result;
import com.wms.entity.User;
import com.wms.entity.UserRole;
import com.wms.mapper.RoleMapper;
import com.wms.mapper.UserMapper;
import com.wms.mapper.UserRoleMapper;
import com.wms.page.Page;
import com.wms.service.UserService;
import com.wms.utils.DigestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    //    注入UserMapper
    @Autowired
    private UserMapper userMapper;

    //    根据账号查询用户的业务方法
    @Override
    public User queryUserByCode(String userCode) {
        return userMapper.findUserByCode(userCode);
    }

    //    分页查询用户的业务方法
    @Override
    public Page queryUserByPage(Page page, User user) {
//        查询用户行数
        Integer count = userMapper.findUserRowCount(user);

//        分页查询用户
        List<User> userList = userMapper.findUserByPage(page, user);

//        组装所有分页信息
        page.setTotalNum(count);
        page.setResultList(userList);

        return page;
    }

    //    添加用户的业务方法
    @Override
    public Result saveUser(User user) {
//        判断账号是否已存在
        User u = userMapper.findUserByCode(user.getUserCode());
        if (u != null) {
            return Result.err(Result.CODE_ERR_BUSINESS, "账号已存在!");
        }
//        对密码做加密处理
        String password = DigestUtil.hmacSign(user.getUserPwd());
        user.setUserPwd(password);

//        执行添加
        int i = userMapper.insertUser(user);
        if (i > 0) {
            return Result.ok("用户添加成功!");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "用户添加失败!");
    }

    //    启用或禁用业务的方法
    @Override
    public Result setUserState(User user) {
        int i = userMapper.setStateByUid(user.getUserId(), user.getUserState());
        if (i > 0) {
            return Result.ok("启用或禁用用户成功!");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "启用或禁用用户失败!");
    }

    /*
        给用户分配角色的业务方法
    */

    //    注入RoleMapper
    @Autowired
    private RoleMapper roleMapper;

    //    注入UserRoleMapper
    @Autowired
    private UserRoleMapper userRoleMapper;

    //    事务处理
    @Transactional
    @Override
    public void assignRole(AssignRoleDto assignRoleDto) {
        userRoleMapper.removeUserRoleByUid(assignRoleDto.getUserId());
        List<String> roleNameList = assignRoleDto.getRoleCheckList();
        for (String roleName : roleNameList) {
            Integer roleId = roleMapper.findRoleIdByName(roleName);
            UserRole userRole = new UserRole();
            userRole.setRoleId(roleId);
            userRole.setUserId(assignRoleDto.getUserId());
            userRoleMapper.insertUserRole(userRole);
        }
    }

    //    根据用户id删除用户的业务方法
    @Override
    public Result removeUserByIds(List<Integer> userIdList) {
        int i = userMapper.setIsDeleteByUids(userIdList);
        if (i > 0) {
            return Result.ok("删除用户成功!");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "删除用户失败!");
    }

    //    修改用户的业务方法
    @Override
    public Result setUserById(User user) {
        int i = userMapper.setUserNameByUid(user);
        if (i > 0) {
            return Result.ok("用户修改成功!");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "用户修改失败!");
    }

    //    根据用户id修改用户密码的方法
    @Override
    public Result setPwdByUid(Integer userId) {

//        给定初始密码123456并加密
        String password = DigestUtil.hmacSign("123456");

//        根据用户id修改密码
        int i = userMapper.setPwdByUid(userId, password);
        if (i > 0) {
            return Result.ok("密码重置成功!");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "密码重置失败!");
    }
}
