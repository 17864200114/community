package com.zqk.community.model;

public class Like {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column likerecord.id
     *
     * @mbg.generated Sat May 30 18:43:48 CST 2020
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column likerecord.comment_id
     *
     * @mbg.generated Sat May 30 18:43:48 CST 2020
     */
    private Long commentId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column likerecord.liker_id
     *
     * @mbg.generated Sat May 30 18:43:48 CST 2020
     */
    private Integer likerId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column likerecord.like_type
     *
     * @mbg.generated Sat May 30 18:43:48 CST 2020
     */
    private Integer likeType;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column likerecord.id
     *
     * @return the value of likerecord.id
     *
     * @mbg.generated Sat May 30 18:43:48 CST 2020
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column likerecord.id
     *
     * @param id the value for likerecord.id
     *
     * @mbg.generated Sat May 30 18:43:48 CST 2020
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column likerecord.comment_id
     *
     * @return the value of likerecord.comment_id
     *
     * @mbg.generated Sat May 30 18:43:48 CST 2020
     */
    public Long getCommentId() {
        return commentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column likerecord.comment_id
     *
     * @param commentId the value for likerecord.comment_id
     *
     * @mbg.generated Sat May 30 18:43:48 CST 2020
     */
    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column likerecord.liker_id
     *
     * @return the value of likerecord.liker_id
     *
     * @mbg.generated Sat May 30 18:43:48 CST 2020
     */
    public Integer getLikerId() {
        return likerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column likerecord.liker_id
     *
     * @param likerId the value for likerecord.liker_id
     *
     * @mbg.generated Sat May 30 18:43:48 CST 2020
     */
    public void setLikerId(Integer likerId) {
        this.likerId = likerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column likerecord.like_type
     *
     * @return the value of likerecord.like_type
     *
     * @mbg.generated Sat May 30 18:43:48 CST 2020
     */
    public Integer getLikeType() {
        return likeType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column likerecord.like_type
     *
     * @param likeType the value for likerecord.like_type
     *
     * @mbg.generated Sat May 30 18:43:48 CST 2020
     */
    public void setLikeType(Integer likeType) {
        this.likeType = likeType;
    }
}