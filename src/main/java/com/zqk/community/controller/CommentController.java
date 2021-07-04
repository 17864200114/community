package com.zqk.community.controller;

import com.zqk.community.dto.CommentCreateDTO;
import com.zqk.community.dto.CommentDTO;
import com.zqk.community.dto.LikeCreateDTO;
import com.zqk.community.dto.ResultDTO;
import com.zqk.community.enums.CommentTypeEnum;
import com.zqk.community.exception.CustomizeErrorCode;
import com.zqk.community.model.Comment;
import com.zqk.community.model.User;
import com.zqk.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class CommentController {


    @Autowired
    private CommentService commentService;
    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    //@RequestBody主要用来接收前端传递给后端的json字符串中的数据的(请求体中的数据的);
    //GET方式无请求体，所以使用@RequestBody接收数据时，前端不能使用GET方式提交数据，而是用POST方式进行提交。
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){

        //通过session来获得评论人信息即user
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if(commentCreateDTO==null|| StringUtils.isBlank(commentCreateDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_IS_EMPTY);
        }

        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setCommenter(user.getId());
        comment.setLikeCount(0L);
        commentService.insert(comment,user);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET )
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name="id") Long id){
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT, 0);
        return ResultDTO.okOf(commentDTOS);
    }

    @ResponseBody
    @RequestMapping(value = "/commentLike",method = RequestMethod.POST)
    public Object likeType(@RequestBody LikeCreateDTO likeCreateDTO,
                       HttpServletRequest request){
        commentService.likeOrNotLike(likeCreateDTO.getLikerId(),likeCreateDTO.getCommentId(),likeCreateDTO.getType());
        return ResultDTO.okOf();
    }
}
