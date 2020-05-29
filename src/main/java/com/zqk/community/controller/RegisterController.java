package com.zqk.community.controller;

import com.zqk.community.mapper.UserMapper;
import com.zqk.community.model.User;
import com.zqk.community.model.UserExample;
import com.zqk.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class RegisterController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/register")
    public String register(){
        return  "register";
    }

    @PostMapping("/register")
    public String doRegister(@RequestParam("name") String name,
                           @RequestParam("password") String password,
                           @RequestParam("repeatPassword") String repeatPassword,
                           HttpServletRequest request,
                           HttpServletResponse response,
                           Model model){
        if(name == null || name == ""){
            model.addAttribute("error","用户名不能为空");
            return "register";
        }
        if(password == null || password == ""){
            model.addAttribute("error","密码不能为空");
            return "register";
        }
        if(!repeatPassword.equals(password)){
            model.addAttribute("error","密码和重置密码不同");
            return "register";
        }
        if(userService.nameExists(name))
        {
            model.addAttribute("error","用户名已被注册");
            return "register";
        }
        User user = userService.register(name,password);
        Cookie cookie = new Cookie("token", user.getToken());
        //cookie最长生存时间，单位为s
        cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
