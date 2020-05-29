package com.zqk.community.mapper;

import com.zqk.community.model.Comment;


import java.util.List;

public interface CommentExtMapper {
    int incCommentCount(Comment comment);
    List<Long> selectComment(Long id);
}