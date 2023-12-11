package com.wms.controller;

import com.wms.dto.AssignAuthDto;
import com.wms.entity.CurrentUser;
import com.wms.entity.Result;
import com.wms.entity.Role;
import com.wms.page.Page;
import com.wms.service.RoleService;
import com.wms.utils.TokenUtils;
import com.wms.utils.WarehouseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/role")
@RestController
public class RoleController {

    // 注入RoleService
    @Autowired
    private RoleService roleService;

    // 注入TokenUtils的bean对象
    @Autowired
    private TokenUtils tokenUtils;

    // 查询所有角色的url接口/role/role-list
    @RequestMapping("/role-list")
    public Result roleList() {
        // 执行业务
        List<Role> roleList = roleService.queryAllRole();
        // 响应
        return Result.ok(roleList);
    }

    //    分页查询角色的url接口/role/role-page-list
    @RequestMapping("role-page-list")
    public Result roleListPage(Page page, Role role) {
        // 执行业务
        page = roleService.queryRolePage(page, role);
        // 响应
        return Result.ok(page);
    }

    // 添加角色的url接口/role/role-add
    @RequestMapping("/role-add")
    public Result addRole(@RequestBody Role role, @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token) {
        // 拿到当前登录的用户id
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        int createBy = currentUser.getUserId();

        role.setCreateBy(createBy);

        // 执行业务
        Result result = roleService.saveRole(role);

        return result;
    }

    // 用或禁用角色的url接口/role/role-state-update
    @RequestMapping("/role-state-update")
    public Result updateRoleState(@RequestBody Role role) {
        //  执行业务
        Result result = roleService.setRoleState(role);
        //  响应
        return result;
    }

    // 删除角色的url接口/role/role-delete/{roleId}
    @RequestMapping("/role-delete/{roleId}")
    public Result deleteRole(@PathVariable Integer roleId) {
        // 执行业务
        Result result = roleService.deleteRoleById(roleId);
        // 响应
        return result;
    }

    // 查询角色分配的所有权限菜单的urL接口/role/role-auth
    @RequestMapping("/role-auth")
    public Result roleAuth(Integer roleId) {
        // 执行业务
        List<Integer> authList = roleService.queryRoleAuthIds(roleId);
        // 响应
        return Result.ok(authList);
    }

    // 给角色分配菜单权限的urL接口/roLe/auth-grant
    @RequestMapping("/auth-grant")
    public Result authGrant(@RequestBody AssignAuthDto assignAuthDto) {
        // 执行业务
        roleService.saveRoleAuth(assignAuthDto);
        // 响应
        return Result.ok("权限分配成功!");
    }

    // 修改角色的url接口/role/role-update
    @RequestMapping("/role-update")
    public Result updateRole(@RequestBody Role role, @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token) {
        // 拿到当前登录的用户id
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        int updateBy = currentUser.getUserId();

        role.setUpdateBy(updateBy);

        // 执行业务
        Result result = roleService.setRoleByRid(role);

        // 响应
        return result;
    }
}

