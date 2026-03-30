package com.anaarellano.tradez.data;

import java.util.List;

import com.anaarellano.tradez.models.ReviewEntity;

/**
 * ReviewRepository Interface
 */
public interface ReviewRepositoryInterface 
{
    void save(ReviewEntity review);

    List<ReviewEntity> findByReviewedUserId(int userId);
    
    Double getAverageRating(int userId);
    
    Integer getReviewCount(int userId);

    List<ReviewEntity> findByReviewerId(int reviewerId);
}