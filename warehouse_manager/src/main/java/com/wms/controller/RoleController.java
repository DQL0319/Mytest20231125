package com.wms.controller;

import com.wms.entity.CurrentUser;
import com.wms.entity.Result;
import com.wms.entity.Role;
import com.wms.page.Page;
import com.wms.service.RoleService;
import com.wms.utils.TokenUtils;
import com.wms.utils.WarehouseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/role")
@RestController
public class RoleController {

    //    注入RoleService
    @Autowired
    private RoleService roleService;

    //    注入TokenUtils的bean对象
    @Autowired
    private TokenUtils tokenUtils;

    //    查询所有角色的url接口/role/role-list
    @RequestMapping("/role-list")
    public Result roleList() {
//        执行业务
        List<Role> roleList = roleService.queryAllRole();
//        响应
        return Result.ok(roleList);
    }

    //    分页查询角色的url接口/role/role-page-list
    @RequestMapping("role-page-list")
    public Result roleListPage(Page page, Role role) {
//        执行业务
        page = roleService.queryRolePage(page, role);
//        响应
        return Result.ok(page);
    }

    //    添加角色的url接口/role/role-add
    @RequestMapping("/role-add")
    public Result addRole(@RequestBody Role role, @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token) {
//        拿到当前登录的用户id
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        int createBy = currentUser.getUserId();

        role.setCreateBy(createBy);

//        执行业务
        Result result = roleService.saveRole(role);

        return result;
    }

    //    启用或禁用角色的url接口/role/role-state-update
    @RequestMapping("/role-state-update")
    public Result updateRoleState(@RequestBody Role role) {
//        执行业务
        Result result = roleService.setRoleState(role);
//        响应
        return result;
    }

}

