package com.ww.example.systemdemo.controllor;

import com.ww.example.systemdemo.configuration.annotation.LoginToken;
import com.ww.example.systemdemo.domain.SysPerson;
import com.ww.example.systemdemo.service.TokenService;
import com.ww.example.systemdemo.service.SysPersonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class LoginControllor {

    @Resource
    private SysPersonService sysPersonService;

    @Resource
    private TokenService commenService;

    @GetMapping("/login")
    public String loging(String userName, String passwd) {
        SysPerson person = sysPersonService.getByUserName(userName);
        if(person == null){
            return "登录失败,用户不存在";
        }
        if(!person.getPassword().equals(passwd)){
            return "登录失败,密码错误";
        }
        String token = commenService.createToken(userName,passwd);
        return token;
    }

    @LoginToken
    @GetMapping("/getAffirm")
    public String getAffirm(){
        return "token验证通过";
    }

}
