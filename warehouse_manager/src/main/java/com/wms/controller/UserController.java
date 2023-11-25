package com.wms.controller;

import com.wms.dto.AssignRoleDto;
import com.wms.entity.CurrentUser;
import com.wms.entity.Result;
import com.wms.entity.Role;
import com.wms.entity.User;
import com.wms.page.Page;
import com.wms.service.RoleService;
import com.wms.service.UserService;
import com.wms.utils.TokenUtils;
import com.wms.utils.WarehouseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RequestMapping("/user")
@RestController
public class UserController {
    //    注入UserService
    @Autowired
    private UserService userService;

    //    注入TokenUtils
    @Autowired
    private TokenUtils tokenUtils;

    //    注入RoleService
    @Autowired
    private RoleService roleService;

    //    分页查询用户的url接口/user/user-list
    @RequestMapping("/user-list")
    public Result userList(Page page, User user) {
//        执行业务
        page = userService.queryUserByPage(page, user);
//        响应
        return Result.ok(page);
    }

    /*
      添加用户的url接口/user/addUser
      参数@RequestBody User user  --  接受并封装前端传递的json数据的用户信息
    */
    @RequestMapping("/addUser")
    public Result addUser(@RequestBody User user, @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token) {
//        拿到当前登录的用户id
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        int createBy = currentUser.getUserId();
        user.setCreateBy(createBy);

//        执行业务
        Result result = userService.saveUser(user);

//        响应
        return result;
    }

    //    启用或禁用用户的url接口/user/updateState
    @RequestMapping("/updateState")
    public Result updateUserState(@RequestBody User user) {
//        执行业务
        Result result = userService.setUserState(user);
//        响应
        return result;
    }

    /*
        查询角色已分配的角色的url接口/user/user/user-role-list/{userId}
        参数@PathVariable Integer userId -- 表示将路径占位符userId的值（用户id）赋给请求处理方法入参变量userId
    */

    @RequestMapping("/user-role-list/{userId}")
    public Result userRoleList(@PathVariable Integer userId) {
//        执行业务
        List<Role> roleList = roleService.queryUserRoleByUid(userId);
//        响应
        return Result.ok(roleList);
    }

    //    给用户分配角色的url接口/user/assignRole
    @RequestMapping("/assignRole")
    public Result assignRole(@RequestBody AssignRoleDto assignRoleDto) {
//        执行业务
        userService.assignRole(assignRoleDto);
//        响应
        return Result.ok("分配角色成功");
    }

    //    根据用户id删除单个用户的url接口/user/deleteUser/{userId}
    @RequestMapping("/deleteUser/{userId}")
    public Result deleteUser(@PathVariable Integer userId) {
//        执行业务
        Result result = userService.removeUserByIds(Arrays.asList(userId));
//        响应
        return result;
    }

    //    根据用户ids批量删除用户的url接口
    @RequestMapping("/deleteUserList")
    public Result deleteUserByIds(@RequestBody List<Integer> userIdList) {
//        执行业务
        Result result = userService.removeUserByIds(userIdList);
//        响应
        return result;
    }

    //    修改用户的url接口/user/updateUser
    @RequestMapping("/updateUser")
    public Result updateUser(@RequestBody User user, @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token) {
//        拿到当前登录的用户id
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        int updateBy = currentUser.getUserId();
        user.setUpdateBy(updateBy);
//        执行业务
        Result result = userService.setUserById(user);
//        响应
        return result;
    }

    //    重置密码的url接口/user/updatePwd/{userId}
    @RequestMapping("/updatePwd/{userId}")
    public Result resetPassword(@PathVariable Integer userId) {
//        执行业务
        Result result = userService.setPwdByUid(userId);
//        响应
        return result;
    }
}