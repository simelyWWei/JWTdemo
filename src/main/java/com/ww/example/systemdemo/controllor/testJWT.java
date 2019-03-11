package com.ww.example.systemdemo.controllor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class testJWT {
    private static final String SECRET = "abcdefg";
    public static void main(String[] args) {

        String token = createJWT();
        System.out.println("Token: "+ token);

        Map<String, Claim> claims = JWT.decode(token).getClaims();

        Map<String, Claim> claimMap = verifyToken(token);
        System.out.println(claimMap.get("name").asString());
        System.out.println(claimMap.get("age").asString());
        System.out.println(claimMap.get("org").asString());
    }

    public static String createJWT(){
        Date iaDate = new Date();

        DateTime now = DateTime.now();
        DateTime expiresDate = now.plusMinutes(1);

        Map<String,Object> map = new HashMap<>();
        map.put("alg","HS256");
        map.put("typ","JWT");
        String token = JWT.create()
                .withHeader(map)
                .withClaim("name","测试姓名")
                .withClaim("age","27")
                .withClaim("org","联通大数据")
                .withExpiresAt(expiresDate.toDate())
                .withIssuedAt(iaDate)
                .sign(Algorithm.HMAC256(SECRET));
        return token;
    }

    public static Map<String,Claim> verifyToken(String token){
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT jwt = null;
        try {
            jwt = jwtVerifier.verify(token);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("证书过期");
        }

        return jwt.getClaims();
    }
}
