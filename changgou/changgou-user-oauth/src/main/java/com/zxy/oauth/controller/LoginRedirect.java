package com.zxy.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/oauth")
public class LoginRedirect {

    @GetMapping("/login")
    public String loginRedirect(@RequestParam("FROM") String from , Model model){
        model.addAttribute("from",from);//登录页面需要知道登录成功后跳转的地址
        return "login";
    }
}
