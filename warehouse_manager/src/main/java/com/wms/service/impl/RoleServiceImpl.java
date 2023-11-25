package com.wms.service.impl;

import com.wms.entity.Result;
import com.wms.entity.Role;
import com.wms.mapper.RoleMapper;
import com.wms.page.Page;
import com.wms.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

//2.制定缓存的名称（数据保存到redis键中的前缀，一般值给标注@CacheConfig注解的类的全路径）
@CacheConfig(cacheNames = "com.wms.service.impl.RoleServiceImpl")
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    /*
        查询所有角色的业务方法
    */
//    3.查询方法上标注@CacheConfig指定缓存的键
    @Cacheable(key = "'all:role'")
    @Override
    public List<Role> queryAllRole() {
        return roleMapper.findAllRole();
    }

    //    查询用户已分配的角色的业务方法
    @Override
    public List<Role> queryUserRoleByUid(Integer userId) {
        return roleMapper.findUserRoleByUid(userId);
    }

    //    分页查询角色的业务方法
    @Override
    public Page queryRolePage(Page page, Role role) {
//        查询角色行数
        Integer count = roleMapper.findRoleRowCount(role);

//        分页查询角色
        List<Role> roleList = roleMapper.findRolePage(page, role);

//        组装分页信息
        page.setTotalNum(count);
        page.setResultList(roleList);

        return page;
    }

    //    添加角色的业务方法
    @CacheEvict(key = "'all:role'")
    @Override
    public Result saveRole(Role role) {
//        判断角色是否已存在
        Role r = roleMapper.findRoleByNameOrCode(role.getRoleName(), role.getRoleCode());
        if (r != null) {  // 角色已存在
            return Result.err(Result.CODE_ERR_BUSINESS, "角色已存在");
        }

//        角色不存在,添加角色
        int i = roleMapper.insertRole(role);
        if (i > 0) {
            return Result.ok("角色添加成功!");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "角色添加失败!");
    }

    //    启用或禁用角色的方法
    @CacheEvict(key = "'all:role'")
    @Override
    public Result setRoleState(Role role) {
        int i = roleMapper.setRoleStateByRid(role.getRoleId(), role.getRoleState());
        if (i > 0) {
            return Result.ok("角色启用或禁用成功!");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "角色启用或禁用失败!");
    }
}
