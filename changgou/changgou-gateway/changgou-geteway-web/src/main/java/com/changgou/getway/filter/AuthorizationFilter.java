package com.changgou.getway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthorizationFilter implements GlobalFilter, Ordered {
    //令牌头名字
    @Value("${tokenKey}")
    public String tokenKey;

    private static String[] passUrl={"/api/user/login","/api/brand/search/"};

    private static String loginURL="http://localhost:9001/oauth/login";

    public static boolean hasAuthoriza(String url){
        for(String urlOne:passUrl){
            if(url.equals(urlOne)){
                return true;
            }
        }
        return false;
    }
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request=exchange.getRequest();
        ServerHttpResponse response=exchange.getResponse();
        String url=request.getURI().getPath();//获取url
        //判断url是否是拦截的地址--->像登录和搜索等直接放行，正常业务逻辑需要获取mysql里面的放行列表
        if(hasAuthoriza(url)){
            Mono<Void> filter=chain.filter(exchange);
            return filter;
        }
        //获取jwt,看request中是否含有令牌，如果有说明已经登录过，没有则跳转登录页
       String token= request.getHeaders().getFirst(tokenKey);
        if(StringUtils.isEmpty(token)){
            token=request.getQueryParams().getFirst(tokenKey);
        }
        //从cookie中获取jwt
        HttpCookie tokenCookie= request.getCookies().getFirst(tokenKey);
        if(tokenCookie!=null){
            token=tokenCookie.getValue();
        }
        if(StringUtils.isEmpty(token)){
        /*    response.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
            return response.setComplete();*/
          return   needAuthorization(loginURL+"?FROM="+request.getURI(),exchange);
        }
        //验证令牌是否正确
        try {
          /*  Claims  claims=JwtUtil.parseJWT(token);
            //将jwt信息放置头部，方便后面的接口获取用户信息
            request.mutate().header(tokenKey,claims.toString());*/
          request.mutate().header(tokenKey,"Bearer "+token);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        //放行
        return chain.filter(exchange);
    }

    //设置过滤器执行顺序
    @Override
    public int getOrder() {
        return 0;
    }

    //当未进行验证访问，跳转登录页
    public Mono<Void> needAuthorization(String url,ServerWebExchange exchange){
    ServerHttpResponse response=exchange.getResponse();
        response.setStatusCode(HttpStatus.SEE_OTHER);
        response.getHeaders().set("Location",url);
        return exchange.getResponse().setComplete();
    }
}
