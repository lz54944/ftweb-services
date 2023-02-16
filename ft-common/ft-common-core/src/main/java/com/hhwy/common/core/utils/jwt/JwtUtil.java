package com.hhwy.common.core.utils.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Map;

/**
 * <br>描 述：
 * <br>创 建 人：jzq
 * <br>创建时间：2019/12/17 15:49
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
public class JwtUtil {

    //签名私钥
    static String key_suffix = "==rsa-key-67:ac:69:a4:c3:5e";

    /**
     *  创建认证token
     *  id:登录用户id 或 UUID
     *  subject：登录用户名
     *  key: 签名私钥 不能太短
     *  ttl: 签名的失效时间，毫秒
     */
    public static String createJwt(String key, Long ttl, String id, String name, Map<String,Object> map) {
        //1.设置失效时间
        long now = System.currentTimeMillis();//当前毫秒
        long exp = now + ttl;
        //2.创建jwtBuilder
        JwtBuilder jwtBuilder = Jwts.builder().setId(id).setSubject(name)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, key+key_suffix);
        //3.根据map设置claims
        if(map != null){
            for(Map.Entry<String,Object> entry : map.entrySet()) {
                jwtBuilder.claim(entry.getKey(),entry.getValue());
            }
        }
        jwtBuilder.setExpiration(new Date(exp));
        //4.创建token
        String token = jwtBuilder.compact();
        return token;
    }


    /**
     * 解析token字符串获取clamis
     * key: 签名私钥
     */
    public static Claims parseJwt(String key, String token) {
        Claims claims = Jwts.parser().setSigningKey(key+key_suffix).parseClaimsJws(token).getBody();
        return claims;
    }

    public static void main(String[] args) {
        String jwt = createJwt("testKey", 3000L, "admin", "admin", null);
        System.err.println(jwt);
        Claims claims = parseJwt("testKey", jwt);
        String subject = claims.getSubject();
        System.err.println(subject);
    }
}
