package com.zqk.community.service;

import com.zqk.community.dto.CommentDTO;
import com.zqk.community.enums.CommentTypeEnum;
import com.zqk.community.enums.NotificationStatusEnum;
import com.zqk.community.enums.NotificationTypeEnum;
import com.zqk.community.exception.CustomizeErrorCode;
import com.zqk.community.exception.CustomizeException;
import com.zqk.community.mapper.*;
import com.zqk.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private LikeMapper likeMapper;

    @Transactional //事物
    public void insert(Comment comment, User commenter) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复评论(即对 帖子里的人回复的comment进行回复）
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            commentMapper.insert(comment);
            //增加评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);


            //创建通知
            createNotify(comment, dbComment.getCommenter(), commenter.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT, question.getId());
        } else {
            //回复问题 （对发帖人回复）
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            comment.setCommentCount(0);
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
            //创建通知
            createNotify(comment, question.getCreator(), commenter.getName(), question.getTitle(), NotificationTypeEnum.REPLY_QUESTION, question.getId());

            //修改gmtmodeified时间
            changeGmtModified(comment.getParentId());
        }
    }



    private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType, Long outerId) {
        if (receiver == comment.getCommenter()) {
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterid(outerId);
        notification.setNotifier(comment.getCommenter());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }


    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type, int likerId) {
        CommentExample commentExample = new CommentExample();
        //这里的评论单单指对楼主的回复，即此处的id为楼主的question的id
        //要在每个commentdto里加一个likedto
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create asc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size() == 0) {
            return new ArrayList<>();
        }
        //获取去重的评论人
        Set<Long> commenters = comments.stream().map(comment -> comment.getCommenter()).collect(Collectors.toSet());
        List userIds = new ArrayList();
        userIds.addAll(commenters);

        //获取评论人并转化为map
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));


        //转换comment为commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommenter()));
            if(likerId!=0){
                LikeExample likeExample = new LikeExample();
                likeExample.createCriteria().andCommentIdEqualTo(commentDTO.getId()).andLikerIdEqualTo(likerId);
                List<Like> likeList = likeMapper.selectByExample(likeExample);
                if(likeList.size()==1){
                    commentDTO.setLikeOrNotLike(1);
                }
                else {
                    commentDTO.setLikeOrNotLike(0);
                }
                System.out.println("Controller传进来的值为"+commentDTO.getLikeOrNotLike());
            }else{
                commentDTO.setLikeOrNotLike(0);
            }
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }

    public void likeOrNotLike(Long likerId, Long commentId, Integer type) {
        System.out.println(type);
        if (type==0){
            Like like = new Like();
            like.setCommentId(commentId);
            like.setLikerId(Math.toIntExact(likerId));
            like.setLikeType(1);
            likeMapper.insert(like);
            Comment comment = new Comment();
            comment.setId(commentId);
            commentExtMapper.incCommentLikeCount(comment);
        }else {
            LikeExample likeExample = new LikeExample();
            likeExample.createCriteria().andLikerIdEqualTo(Math.toIntExact(likerId)).andCommentIdEqualTo(commentId);
            likeMapper.deleteByExample(likeExample);
            Comment comment = new Comment();
            comment.setId(commentId);
            commentExtMapper.decCommentLikeCount(comment);
        }
    }

    private void changeGmtModified(Long parentId) {
        Question question = new Question();
        question.setId(parentId);
        question.setGmtModified(System.currentTimeMillis());
        questionMapper.updateByPrimaryKeySelective(question);
    }
}
