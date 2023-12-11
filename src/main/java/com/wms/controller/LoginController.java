package com.wms.controller;

import com.google.code.kaptcha.Producer;
import com.wms.entity.*;
import com.wms.service.AuthService;
import com.wms.service.UserService;
import com.wms.utils.DigestUtil;
import com.wms.utils.TokenUtils;
import com.wms.utils.WarehouseConstants;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class LoginController {
    //    注入
    @Resource(name = "captchaProducer")
    private Producer producer;

    //    注入redis模板
    @Autowired
    private StringRedisTemplate redisTemplate;

    //    注入UserService
    @Autowired
    private UserService userService;

    //    注入TokenUtils的bean对象
    @Autowired
    private TokenUtils tokenUtils;

    //    注入AuthService
    @Autowired
    private AuthService authService;

    @RequestMapping("/captcha/captchaImage")
    public void captchaImage(HttpServletResponse response) {

        ServletOutputStream out = null;

        try {
            //        生成验证码图片的文件
            String text = producer.createText();
//        使用验证码文本生成验证码图片
            BufferedImage image = producer.createImage(text);
//        验证码文本保存在redis
            redisTemplate.opsForValue().set(text, "", 60 * 30, TimeUnit.SECONDS);

//        验证码图片响应给image
            response.setContentType("image/jpeg");
//        将验证码给前端
            out = response.getOutputStream();
            ImageIO.write(image, "jpg", out);
//            刷新
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
     登录url接口login
     参数RequestBody LoginUser loginUser 表示接受冰封和在那个前端传递的登录的用户信息的json参数
     返回值Result都西昂，表示向前端响应结果Result对象转的json中，包含响应状态码 成功失败响应 相应信息 响应数据
    */
    @RequestMapping("/login")
    public Result login(@RequestBody LoginUser loginUser) {
//        拿到客户录入的验证码
        if (!redisTemplate.hasKey(loginUser.getVerificationCode())) {
            return Result.err(Result.CODE_ERR_BUSINESS, "验证码错误!");
        }
//        根据账号查询用户
        User user = userService.queryUserByCode(loginUser.getUserCode());
        if (user != null) {
            if (user.getUserState().equals(WarehouseConstants.USER_STATE_PASS)) { //用户已审核
//                拿到用户录入的密码进行md5加密
                String userPwd = DigestUtil.hmacSign(loginUser.getUserPwd());
                if (userPwd.equals(user.getUserPwd())) { // 密码合法
//                    生成jwt token 并存储的redis
                    CurrentUser currentUser = new CurrentUser(user.getUserId(), user.getUserCode(), user.getUserName());
                    String token = tokenUtils.loginSign(currentUser, userPwd);
//                    向客户端响应jwt token
                    return Result.ok("登陆成功!", token);
                } else {  // 密码错误
                    return Result.err(Result.CODE_ERR_BUSINESS, "密码错误!");
                }
            } else {  // 用户未审核
                return Result.err(Result.CODE_ERR_BUSINESS, "用户未审核!");
            }
        } else {  // 账号不存在
            return Result.err(Result.CODE_ERR_BUSINESS, "账号不存在!");
        }
    }

    /*
      获取当前登录的用户信息的url接口/curr-uesr

      参数@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token
      表示请求token的值(前端归还的token赋值给请求处理方法入参变量token)
    */
    @RequestMapping("/curr-user")
    public Result currentUser(@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token) {
//        解析token拿到封装了当前登录用户信息的CurrentUser对象
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
//        响应给前端
        return Result.ok(currentUser);
    }

    /*
      加载用户权限菜单树的url接口/user/auth-list
    */
    @RequestMapping("/user/auth-list")
    public Result loadAuthTree(@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token) {
//        拿到当前登录的用户id
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        int userId = currentUser.getUserId();

//        执行业务
        List<Auth> authTreeList = authService.authTreeByUid(userId);
//        响应
        return Result.ok(authTreeList);
    }

    /*
      登出的url接口/logout
    */
    @RequestMapping("/logout")
    public Result logout(@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token) {
//        从redis中删除token键
        redisTemplate.delete(token);
//        响应
        return Result.ok("退出系统!");
    }
}

