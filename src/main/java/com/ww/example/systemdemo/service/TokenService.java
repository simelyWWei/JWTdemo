package com.ww.example.systemdemo.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {
    public String getToken (String userName,String passwd){
        String token = "";
        token = JWT.create().withAudience(userName)
                .sign(Algorithm.HMAC256(passwd));
        return token;
    }

    public String createToken(String userName,String passwd){
        // 设置超时时间
        DateTime now = DateTime.now();
        DateTime dateTime = now.plusMinutes(15);
        Date iaDate = now.toDate();
        Date expiresDate = dateTime.toDate();
        // 设置jwt头部
        // {
        //   "typ": "JWT",
        //   "alg": "HS256"
        // }
        Map<String,Object> map = new HashMap<>(3);
        map.put("alg","HS256");
        map.put("typ","JWT");
        // 设置playlod -- cliams
        // {
        //   "name":"Free码农",
        //   "age":"28",
        //   "org":"今日头条"
        // }
       String token = "";
        token = JWT.create()
                .withHeader(map) //设置头信息
                .withClaim("account",userName)  //设置 playlod
                .withExpiresAt(expiresDate) //设置过期时间
                .withIssuedAt(iaDate)   //设置签发时间
                .sign(Algorithm.HMAC256(passwd)); //设置签证
        return token;
    }
}
