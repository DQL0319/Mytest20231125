package com.wms.service.impl;

import com.alibaba.fastjson.JSON;
import com.wms.entity.Auth;
import com.wms.mapper.AuthMapper;
import com.wms.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

// 指定缓存的名称（缓存的键的前缀），一般是标注@CacheConfig注解类的全路径
@CacheConfig(cacheNames = "com.wms.service.impl.AuthServiceImpl")
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthMapper authMapper;

    // 注入redis模板
    @Autowired
    private StringRedisTemplate redisTemplate;

    /*
      查询用户菜单树的业务方法
      向redis缓存用户菜单树--键authTree:userId 值=菜单树List<Auth>转成的json串
    */
    @Override
    public List<Auth> authTreeByUid(Integer userId) {
        // 先从redis中查询缓存的用户菜单树
        String authTreeJson = redisTemplate.opsForValue().get("authTree" + userId);
        if (StringUtils.hasText(authTreeJson)) {
            // 将菜单树List<Auth>转成的json串转回菜单树List<Auth>并返回
            List<Auth> authTreeList = JSON.parseArray(authTreeJson, Auth.class);
            return authTreeList;
        }

        /*
          说明redis中没有用户菜单树的缓存
        */
        // 查询用户权限下的所有菜单
        List<Auth> allAuthList = authMapper.findAuthByUid(userId);
        // 将所有菜单List<Auth>转成菜单树List<Auth>
        List<Auth> authTreeList = allAuthToAuthTree(allAuthList, 0);
        // 向redis中缓存菜单树List<Auth>
        redisTemplate.opsForValue().set("authTree:" + userId, JSON.toJSONString(authTreeList));
        return authTreeList;
    }

    // 将所有菜单List<Auth>转成菜单树List<Auth>的递归算法
    private List<Auth> allAuthToAuthTree(List<Auth> allAuthList, Integer pid) {
        // 查询出所有一级菜单
        List<Auth> firstLevelAuthList = new ArrayList<>();
        for (Auth auth : allAuthList) {
            if (auth.getParentId().equals(pid)) {
                firstLevelAuthList.add(auth);
            }
        }

        // 拿到每个一级菜单的所有二级菜单
        for (Auth firstAuth : firstLevelAuthList) {
            List<Auth> secondLevelAuthList = allAuthToAuthTree(allAuthList, firstAuth.getAuthId());
            firstAuth.setChildAuth(secondLevelAuthList);
        }

        return firstLevelAuthList;
    }

    /*
      查询所有权限菜单树的业务方法
    */
    // 查询方法上标注@CacheabLe注解并指定缓存的键
    @Cacheable(key = "'all:authTree'")
    @Override
    public List<Auth> allAuthTree() {
        String allAuthTreeJson = redisTemplate.opsForValue().get("all:authTree");
        if (StringUtils.hasText(allAuthTreeJson)) {
            // 将json串转回整个权限（菜单）树List<Auth>并返回
            List<Auth> allAuthTreeList = JSON.parseArray(allAuthTreeJson, Auth.class);
            return allAuthTreeList;
        }

        // redis中没有查到缓存，从数据库表中查询所有权限(菜单)
        List<Auth> allAuthList = authMapper.findAllAuth();

        // 将所有权限菜单List<Auth>转成权限菜单树List<Auth>
        List<Auth> authTreeList = allAuthToAuthTree(allAuthList, 0);

        redisTemplate.opsForValue().set("all:authTree", JSON.toJSONString(authTreeList));

        // 返回整个权限(菜单)树List<Auth>
        return authTreeList;
    }
}
