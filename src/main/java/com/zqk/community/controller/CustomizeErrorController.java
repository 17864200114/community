package com.zqk.community.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class CustomizeErrorController implements ErrorController {
    @Override
    public String getErrorPath(){
        return "error";
    }


    @RequestMapping(
            //produces可能不算一个注解，因为什么呢，它是注解@requestMapping注解里面的属性项，
            //它的作用是指定返回值类型，不但可以设置返回值类型还可以设定返回值的字符编码；
            //还有一个属性与其对应，就是consumes： 指定处理请求的提交内容类型（Content-Type），例如application/json, text/html;
            //text/html的意思是将文件的content-type设置为text/html的形式，浏览器在获取到这种文问件时会自动调用html的解析器对文件进行相应的处理。
            produces = {"text/html"}
    )
    public ModelAndView errorHtml(HttpServletRequest request,
                                  Model model) {
        HttpStatus status = this.getStatus(request);
        if(status.is4xxClientError()){
            model.addAttribute("message","请求有错，请更改");
        }
        if(status.is5xxServerError()){
            model.addAttribute("message","服务器繁忙，请稍后再试");
        }
        return new ModelAndView("error");
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer)request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            try {
                return HttpStatus.valueOf(statusCode);
            } catch (Exception var4) {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
    }
}
