package com.wms.controller;

import com.wms.entity.Auth;
import com.wms.entity.Result;
import com.wms.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/auth")
@RestController
public class AuthController {

    // 注入AuthService
    @Autowired
    private AuthService authService;

    // 查询所有权限菜单树的urL接口/auth/auth-tree
    @RequestMapping("/auth-tree")
    public Result loadAllAuthTree() {
        // 执行业务
        List<Auth> allAuthTree = authService.allAuthTree();

        // 响应
        return Result.ok(allAuthTree);
    }
}
