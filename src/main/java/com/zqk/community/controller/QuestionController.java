package com.zqk.community.controller;

import com.zqk.community.dto.CommentDTO;
import com.zqk.community.dto.QuestionDTO;
import com.zqk.community.enums.CommentTypeEnum;
import com.zqk.community.model.User;
import com.zqk.community.service.CommentService;
import com.zqk.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id, Model model,
                           HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        QuestionDTO questionDTO = questionService.getById(id);
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        if (user==null){
            List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION, 0);
            questionService.incView(id);
            model.addAttribute("comments",comments);
            model.addAttribute("question",questionDTO);
            model.addAttribute("relatedQuestions",relatedQuestions);
            return "question";
        }else {
            List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION, Math.toIntExact(user.getId()));
            questionService.incView(id);
            model.addAttribute("comments",comments);
            model.addAttribute("question",questionDTO);
            model.addAttribute("relatedQuestions",relatedQuestions);
            return "question";
        }
        //增加阅读数

    }

    @GetMapping("/deletequestion/{id}")
    public String deleteQuestion(@PathVariable(name = "id") Long id){
        questionService.delete(id);
        return "redirect:/profile/questions";
    }
}
