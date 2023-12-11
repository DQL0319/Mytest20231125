package com.wms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/*
  权限表
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auth implements Serializable {
    private Integer authId;//权限(菜单)id

    private Integer parentId;//父权限(菜单)id

    private String authName;//权限(菜单)名称

    private String authDesc;//权限(菜单)描述

    private Integer authGrade;//权限(菜单)层级

    private String authType;//权限(菜单)类型

    private String authUrl;//权限(菜单)访问的url接口

    private String authCode;//权限(菜单)标识

    private Integer authOrder;//权限(菜单)的优先级

    private String authState;//权限(菜单)状态(1.启用,0.禁用)

    private Integer createBy;//创建权限(菜单)的用户id

    private Date createTime;//权限(菜单)的创建时间

    private Integer updateBy;//修改权限(菜单)的用户id

    private Date updateTime;//权限(菜单)的修改时间

    //追加的List<Auth>集合属性 -- 用于存储当前权限(菜单)的子级权限(菜单)
    private List<Auth> childAuth;

}
