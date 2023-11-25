package com.wms.service.impl;

import com.alibaba.fastjson.JSON;
import com.wms.entity.Auth;
import com.wms.mapper.AuthMapper;
import com.wms.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.w3c.dom.ls.LSInput;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthMapper authMapper;

    //    注入redis模板
    @Autowired
    private StringRedisTemplate redisTemplate;

    /*
      查询用户菜单树的业务方法
      向redis缓存用户菜单树--键authTree:userId 值=菜单树List<Auth>转成的json串
    */
    @Override
    public List<Auth> authTreeByUid(Integer userId) {
//        先从redis中查询缓存的用户菜单树
        String authTreeJson = redisTemplate.opsForValue().get("authTree" + userId);
        if (StringUtils.hasText(authTreeJson)) {
//            将菜单树List<Auth>转成的json串转回菜单树List<Auth>并返回
            List<Auth> authTreeList = JSON.parseArray(authTreeJson, Auth.class);
            return authTreeList;
        }
        /*
          说明redis中没有用户菜单树的缓存
        */
//        查询用户权限下的所有菜单
        List<Auth> allAuthList = authMapper.findAuthByUid(userId);
//        将所有菜单List<Auth>转成菜单树List<Auth>
        List<Auth> authTreeList = allAuthToAuthTree(allAuthList, 0);
//        向redis中缓存菜单树List<Auth>
        redisTemplate.opsForValue().set("authTree:" + userId, JSON.toJSONString(authTreeList));
        return authTreeList;
    }

    //    将所有菜单List<Auth>转成菜单树List<Auth>的递归算法
    private List<Auth> allAuthToAuthTree(List<Auth> allAuthList, Integer pid) {
//        查询出所有一级菜单
        List<Auth> firstLevelAuthList = new ArrayList<>();
        for (Auth auth : allAuthList) {
            if (auth.getParentId().equals(pid)) {
                firstLevelAuthList.add(auth);
            }
        }

//        拿到每个一级菜单的所有二级菜单
        for (Auth firstAuth : firstLevelAuthList) {
            List<Auth> secondLevelAuthList = allAuthToAuthTree(allAuthList, firstAuth.getAuthId());
            firstAuth.setChildAuth(secondLevelAuthList);
        }

        return firstLevelAuthList;
    }
}
