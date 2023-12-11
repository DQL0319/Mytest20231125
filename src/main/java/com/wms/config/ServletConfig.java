package com.wms.config;

import com.wms.filter.LoginCheckFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/*
  原生servlet的配置类
*/
@Configuration
public class ServletConfig {

    // 注入redis模板
    @Autowired
    private StringRedisTemplate redisTemplate;

    /*
      配置FilterRegistrationBean的bean对象--注册原生servlet的过滤器
    */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        // 创建FilterRegistrationBean的bean对象
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        // 创建自定义的过滤器
        LoginCheckFilter loginCheckFilter = new LoginCheckFilter();
        // 手动注入redis模板
        loginCheckFilter.setRedisTemplate(redisTemplate);
        // 将自定义的过滤器注册到FilterRegistrationBean中
        filterRegistrationBean.setFilter(loginCheckFilter);
        // 给过滤器制定拦截的请求
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }

}
