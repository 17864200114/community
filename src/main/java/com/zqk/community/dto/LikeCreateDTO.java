package com.zqk.community.dto;

import lombok.Data;

@Data
public class LikeCreateDTO {
    private Long commentId;
    private Long likerId;
    private Integer type;
}
