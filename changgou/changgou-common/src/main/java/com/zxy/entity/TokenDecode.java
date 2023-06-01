package com.zxy.entity;


import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;

//在经过ResourceServerConfigure验证后，用户名，密码信息可以直接从令牌获取，
//获取公钥、令牌、令牌数据
@Component
public class TokenDecode {
    private static final String PUBLIC_KEY="public.key";
    private String publicKey="";

    //获取公钥
    public String getPublicKey(){
        if(StringUtils.isNotEmpty(publicKey)){
            return publicKey;
        }
        Resource resource=new ClassPathResource(PUBLIC_KEY);
        try {
            InputStreamReader inputStreamReader=new InputStreamReader(resource.getInputStream());
            BufferedReader br = new BufferedReader(inputStreamReader);
            publicKey = br.lines().collect(Collectors.joining("\n"));
            return publicKey;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    //读取令牌数据
    public Map<String,String> dcodeToken( String token){
            //校验Jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(getPublicKey()));
        if(!ObjectUtils.isEmpty(jwt)){
            //获取Jwt原始内容
            String claims = jwt.getClaims();
            return JSON.parseObject(claims,Map.class);
        }
        return null;
    }

    //获取用户数据
    public Map<String,String> getUserInfo(){
        OAuth2AuthenticationDetails details= (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return dcodeToken(details.getTokenValue());
    }
}
