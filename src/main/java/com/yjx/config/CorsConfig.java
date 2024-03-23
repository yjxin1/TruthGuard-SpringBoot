package com.yjx.config;


import com.yjx.Intercepter.LoginCheckInterceptor;
import com.yjx.utils.ValueProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//用于配置全局的跨域请求
@Configuration  //配置类
public class CorsConfig implements WebMvcConfigurer {
    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;
    @Autowired
    private ValueProperties valueProperties;
    //配置全局跨域请求
    @Override
    public void addCorsMappings(CorsRegistry registry) {   // 重写addCorsMappings方法
        registry.addMapping("/**")  // 添加跨域映射，表示对所有路径都允许跨域访问
                //放行哪些原始域
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH");
    }
    //请求拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login")  //拦截除了login外的所有资源
                .excludePathPatterns("/image/**")
                .excludePathPatterns("/register");
    }

}
