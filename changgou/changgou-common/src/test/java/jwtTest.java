import com.google.common.collect.Maps;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.Date;
import java.util.Map;

public class jwtTest {

    /****
     * 创建Jwt令牌
     *
     * jwt令牌执行流程：服务器端根据用户访问信息，生成对应的jwt令牌，
     * 令牌含有是否登录成功，以及用户信息，返回给客户端，客户端之后的访问都必须要要带有这个令牌
     */
    @Test
    public void testCreateJwt(){
        //生成令牌
        JwtBuilder builder= Jwts.builder()
                .setId("888")             //设置唯一编号
                .setSubject("小白")       //设置主题  可以是JSON数据
                .setIssuedAt(new Date())  //设置签发日期
                .setExpiration(new Date()) //过期时间
                .signWith(SignatureAlgorithm.HS256,"itcast");//设置签名 使用HS256算法，并设置SecretKey(字符串)
        //构建 并返回一个字符串
        System.out.println( builder.compact() );
    }

    //自定义添加数据
    @Test
    public void testCreateMyJwt(){
        //生成令牌
        JwtBuilder builder= Jwts.builder()
                .setId("888")             //设置唯一编号
                .setSubject("小白")       //设置主题  可以是JSON数据
                .setIssuedAt(new Date())  //设置签发日期
                .setExpiration(new Date()) //过期时间
                .signWith(SignatureAlgorithm.HS256,"itcast");//设置签名 使用HS256算法，并设置SecretKey(字符串)
        Map<String,Object> dataMap= Maps.newHashMap();
        dataMap.put("name","xaas");
        dataMap.put("age",18);
        dataMap.put("message","信息");
        builder.setClaims(dataMap);
        //构建 并返回一个字符串
        System.out.println( builder.compact() );
    }

    /***
     * 解析Jwt令牌数据
     */
    @Test
    public void testParseJwt(){
        String compactJwt="eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoieGFhcyIsIm1lc3NhZ2UiOiLkv6Hmga8iLCJhZ2UiOjE4fQ.DH9wvnPluRJLuZIDw0cedtGNdo7aQwARZZJ02a-wnKM";
        Claims claims = Jwts.parser().
                setSigningKey("itcast").
                parseClaimsJws(compactJwt).
                getBody();
        System.out.println(claims);
    }



}