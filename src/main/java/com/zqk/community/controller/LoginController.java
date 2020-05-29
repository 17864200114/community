package com.zqk.community.controller;

import com.zqk.community.model.User;
import com.zqk.community.service.NotificationService;
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
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/login")
    public String login(){
        return  "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam("name") String name,
                          @RequestParam("password") String password,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          Model model){
        if(name == null || name == ""){
            model.addAttribute("error","登录名不能为空");
            return "login";
        }
        if(password == null || password == ""){
            model.addAttribute("error","密码不能为空");
            return "login";
        }
        User user = userService.login(name,password);
        if(user != null){
            Cookie cookie = new Cookie("token",user.getToken());
            //cookie最长生存时间，单位为s
            cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
            response.addCookie(cookie);
            return "redirect:/";
        }else{
            model.addAttribute("error","您输入的账号或密码有误，请重新输入");
            return "login";
        }
    }
}
