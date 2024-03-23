package com.yjx.Intercepter;

import org.json.JSONObject;
import com.yjx.pojo.Result;
import com.yjx.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component //交给IOC容器管理
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override //目标资源运行前运行，返回值为true为放行，flase为拦截
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {

        //1. 获取请求的url
        String url = req.getRequestURL().toString();
        log.info("请求的url:{}",url);
        //2. 判断请求url中是否包含login，如果包含，证明是登录操作，放行。
        if(url.contains("login")){
            log.info("登录操作，放行...");
            return true;
        }
        //3. 获取请求头中的令牌（token）
        String jwt = req.getHeader("token");
        //4. 判断令牌是否存在，如果不存在，返回错误结果（未登录）
        if(!StringUtils.hasLength(jwt)){
            log.info("请求头token为空，返回未登录的信息,jwt:",jwt);
            Result error = Result.error("NOT_LOGIN");
            //手动转换，对象——json——————>利用阿里巴巴的fastJSON
            String notLogin = JSONObject.valueToString(error);  //前面由于引入了RestController注释，会自动将Result对象转换为json格式，这里需要手动转换
            resp.getWriter().write(notLogin);
            return false;
        }
        //5. 解析token，如果解析失败，返回错误结果（未登录）
        try{
            JwtUtils.parseJWT(jwt);
        }catch (Exception e){//解析失败
            e.printStackTrace();
            log.info("解析令牌失败，返回未登录的错误信息");
            Result error = Result.error("NOT_LOGIN");
            //手动转换，对象——json——————>利用阿里巴巴的fastJSON
            String notLogin = JSONObject.valueToString(error);  //前面由于引入了RestController注释，会自动将Result对象转换为json格式，这里需要手动转换

            resp.getWriter().write(notLogin);
            return false;
        }
        //6. 放行
        log.info("令牌合法，放行");
        return true;
    }

    @Override //目标资源方法运行后运行
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle...");
    }

    @Override //视图渲染完毕后运行，最后运行
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("afterComletion...");
    }
}
