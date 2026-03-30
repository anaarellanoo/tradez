package com.anaarellano.tradez.models;

/**
 * Reiview Model 
 * -> Service/Controller
 */
public class ReviewModel 
{
    private Integer reviewId;
    private Integer reviewerId;
    private Integer reviewedUserId;
    private Integer rating;
    private String comment;

    private String reviewerName;
    private String reviewedName; 
    private String relativeTime;

    /**
     * Constructor
     */
    public ReviewModel() {
    }

    /**
     * Parametized COnstructor 
     * @param comment
     * @param relativeTime
     * @param rating
     * @param reviewId
     * @param reviewedUserId
     * @param reviewerId
     * @param reviewerName
     */
    public ReviewModel(String comment, String relativeTime, Integer rating, Integer reviewId, Integer reviewedUserId,
            Integer reviewerId, String reviewerName, String reviewedName) 
    {
        this.comment = comment;
        this.rating = rating;
        this.reviewId = reviewId;
        this.reviewedUserId = reviewedUserId;
        this.reviewerId = reviewerId;
        this.reviewerName = reviewerName;
        this.relativeTime = relativeTime;
        this.reviewedName = reviewedName;
    }

    /********************* GETTERS AND SETTER *********************/

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public Integer getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Integer reviewerId) {
        this.reviewerId = reviewerId;
    }

    public Integer getReviewedUserId() {
        return reviewedUserId;
    }

    public void setReviewedUserId(Integer reviewedUserId) {
        this.reviewedUserId = reviewedUserId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getRelativeTime() {
        return relativeTime;
    }

    public void setRelativeTime(String relativeTime) {
        this.relativeTime = relativeTime;
    }

    public String getReviewedName() {
        return reviewedName;
    }

    public void setReviewedName(String reviewedName) {
        this.reviewedName = reviewedName;
    }
    
}
