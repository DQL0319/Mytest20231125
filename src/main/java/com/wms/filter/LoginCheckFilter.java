package com.wms.filter;

import com.alibaba.fastjson.JSON;
import com.wms.entity.Result;
import com.wms.utils.WarehouseConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import jakarta.servlet.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/*
  自定义的登陆限制过滤器
*/
public class LoginCheckFilter implements Filter {

    private StringRedisTemplate redisTemplate;

    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //    过滤器拦截到请求执行的方法
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        /*
          1.白名单请求直接放行
        */
//        创建List<String>放报名url接口
        List<String> urlList = new ArrayList<>();
        urlList.add("/captcha/captchaImage");
        urlList.add("/login");
        urlList.add("/logout");
        urlList.add("/product/img-upload");
//        过滤器拦截到的当前请求的url接口
        String url = request.getServletPath();
        if (urlList.contains(url) || url.contains("/img/upload")) {
            chain.doFilter(request, response);
            return;
        }
        /*
          2.其他请求都校验是否携带token，以及判断redis中是否存在token的键
        */
        String token = request.getHeader(WarehouseConstants.HEADER_TOKEN_NAME);
//        1)有，说明已登陆
        if (StringUtils.hasText(token) && redisTemplate.hasKey(token)) {
            chain.doFilter(request, response);
            return;
        }
//        2)没有，说明未登录或token国企，请求不放行，并给前端做出响应
        Result result = Result.err(Result.CODE_ERR_UNLOGINED, "您尚未登陆");
        String jsonStr = JSON.toJSONString(result);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(jsonStr);
        out.flush();
        out.close();
    }
}
