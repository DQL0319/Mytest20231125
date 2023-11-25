package com.wms.mapper;


import com.wms.entity.User;
import com.wms.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/*
  user_info表的接口
 */
public interface UserMapper {

    // 根据账号查询用户信息的方法
    public User findUserByCode(String userCode);

    // 查询用户行数的方法
    public Integer findUserRowCount(User user);

    // 分页查询用户的方法
    public List<User> findUserByPage(@Param("page") Page page, @Param("user") User user);

    // 添加用户的方法
    public int insertUser(User user);

    // 根据用户id修改用户状态的方法
    public int setStateByUid(Integer userId, String userState);

    // 根据用户ids修改用户删除状态的方法
    public int setIsDeleteByUids(List<Integer> userIdList);

    //    根据用户id修改用户昵称的方法
    public int setUserNameByUid(User user);

    //    根据用户id修改密码的方法
    public int setPwdByUid(Integer userId, String password);

}
