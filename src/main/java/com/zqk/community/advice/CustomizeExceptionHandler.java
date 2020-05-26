package com.zqk.community.advice;

import com.alibaba.fastjson.JSON;
import com.zqk.community.dto.ResultDTO;
import com.zqk.community.exception.CustomizeErrorCode;
import com.zqk.community.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomizeExceptionHandler {
    // /error请求的触发前提是系统中抛出的异常到最终都没有被处理掉，
    // Spring Boot可以通过@ControllerAdvice和@ExceptionHandler实现捕获系统中的异常**，
    // 需要注意的是，如果@ControllerAdvice中如果有其他异常没有捕获到，最终仍然会通过BasicErrorController处理这些异常。
    //这个方法还处理不了的异常交给customizeErrorController来处理
    @ExceptionHandler(Exception.class)
    @ResponseBody
    ModelAndView handle(HttpServletRequest request, Throwable e, Model model, HttpServletResponse response)  {
        String contentType = request.getContentType();
        if ("application/json".equals(contentType)) {
            ResultDTO resultDTO;
            //返回json
            if (e instanceof CustomizeException) {
                resultDTO = ResultDTO.errorOf((CustomizeException)e);
            } else {
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYSTEM_ERROR);
            }
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException ioe) {
            }
            return null;
        } else {
            //instanceof 严格来说是Java中的一个双目运算符，用来测试一个对象是否为一个类的实例
            if (e instanceof CustomizeException) {
                model.addAttribute("message", e.getMessage());
            } else {
                model.addAttribute("message", CustomizeErrorCode.SYSTEM_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }
    }

}
