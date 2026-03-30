package com.anaarellano.tradez.models;

import java.time.LocalDateTime;

/**
 * Review Entity 
 * -> Data/Repository
 */
public class ReviewEntity
{
    private Integer reviewId;
    private Integer reviewerId;
    private Integer reviewedUserId;
    private Integer rating;
    private String comment;
    private LocalDateTime dateCreated;
    private String reviewerName;
    private String reviewedName;

    /**
     * Constructor
     */
    public ReviewEntity() {
    }

    /**
     * Parametized Contructor
     * @param comment
     * @param dateCreated
     * @param rating
     * @param reviewId
     * @param reviewedUserId
     * @param reviewerId
     * @param reviewerName
     */
    public ReviewEntity(String comment, LocalDateTime dateCreated, Integer rating, Integer reviewId, Integer reviewedUserId, Integer reviewerId, String reviewerName, String reviewedName) {
        this.comment = comment;
        this.dateCreated = dateCreated;
        this.rating = rating;
        this.reviewId = reviewId;
        this.reviewedUserId = reviewedUserId;
        this.reviewerId = reviewerId;
        this.reviewerName = reviewerName;
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

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getReviewedName() {
        return reviewedName;
    }

    public void setReviewedName(String reviewedName) {
        this.reviewedName = reviewedName;
    }
    
}
