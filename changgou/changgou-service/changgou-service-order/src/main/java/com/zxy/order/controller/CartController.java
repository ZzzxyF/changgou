package com.zxy.order.controller;

import com.zxy.entity.Result;
import com.zxy.entity.StatusCode;
import com.zxy.entity.TokenDecode;
import com.zxy.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zxy
 */
@RestController
@CrossOrigin
@RequestMapping("/cart")
public class CartController {
    @Autowired
    TokenDecode tokenDecode;
    @Autowired
    CartService cartService;

    @GetMapping("/add")
    public Result add(Integer num, Long id) {
        String username= null;
        try {
            username = tokenDecode.getUserInfo().get("username");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cartService.add(num, id, username);
    }

    @GetMapping("/list")
    public Result list(){
        String username= null;
        try {
            username = tokenDecode.getUserInfo().get("username");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(true, StatusCode.OK,"查询成功",cartService.orderList(username)) ;
    }


}
