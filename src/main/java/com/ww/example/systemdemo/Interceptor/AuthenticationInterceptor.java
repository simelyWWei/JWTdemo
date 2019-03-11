package com.ww.example.systemdemo.Interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ww.example.systemdemo.configuration.annotation.LoginToken;
import com.ww.example.systemdemo.configuration.annotation.PassToken;
import com.ww.example.systemdemo.domain.SysPerson;
import com.ww.example.systemdemo.service.SysPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    SysPersonService sysPersonService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从 http 请求头中取出 token
        String token = request.getHeader("token");
        // 如果不是映射到方法直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)handler;
        Method method=handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(LoginToken.class)) {
            LoginToken userLoginToken = method.getAnnotation(LoginToken.class);
            if (userLoginToken.required()) {
                // 执行认证
                if (token == null) {
                    throw new RuntimeException("无token，请重新登录");
                }
                // 获取 token 中的 user id
                String userName;
                try {
//                    userName = JWT.decode(token).getAudience().get(0);
                    Map<String, Claim> claims = JWT.decode(token).getClaims();
                    userName = claims.get("account").asString();
                } catch (JWTDecodeException j) {
                    throw new RuntimeException("401");
                }
                SysPerson user = sysPersonService.getByUserName(userName);
                if (user == null) {
                    throw new RuntimeException("用户不存在，请重新登录");
                }
                // 验证 token
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
                try {
                    jwtVerifier.verify(token);
                }catch (TokenExpiredException e){
                    throw new RuntimeException("token过期");
                }catch (JWTVerificationException e) {
                    throw new RuntimeException("秘钥信息错误");
                }
                return true;
            }
        }
        return true;
    }


}
