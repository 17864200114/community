package com.zqk.community.controller;

import com.zqk.community.model.User;
import com.zqk.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PersonController {

    @Autowired
    private UserService userService;

    @GetMapping("/person")
    public String person(){
        return  "person";
    }

    @PostMapping("/person")
    public String editInformation(HttpServletRequest request,
                                  Model model,
                                  @RequestParam("id") Long id,
                                  @RequestParam("bio") String bio,
                                  @RequestParam("password") String password,
                                  @RequestParam("avatarUrl") String avatarUrl){
        if(password == null || password == ""){
            model.addAttribute("error","密码不能为空");
            return "person";
        }

        User user = userService.edit(id,bio,password,avatarUrl);
        //更新session用户信息
        request.getSession().removeAttribute("user");
        request.getSession().setAttribute("user", user);
        return "person";
    }
}
