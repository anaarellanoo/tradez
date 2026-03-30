package com.anaarellano.tradez.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.anaarellano.tradez.data.ReviewRepository;
import com.anaarellano.tradez.models.Converter;
import com.anaarellano.tradez.models.ReviewEntity;
import com.anaarellano.tradez.models.ReviewModel;

/**
 * Review Service Manages all the user reviews create, view, calc average
 * rating, total review
 */
@Service
public class ReviewService {

    private final ReviewRepository reviewRepo;
    private final Converter converter;

    /**
     * Constructor
     *
     * @param reviewRepo
     * @param converter
     */
    public ReviewService(ReviewRepository reviewRepo, Converter converter) {
        this.reviewRepo = reviewRepo;
        this.converter = converter;
    }

    /**
     * Get all the review User Specific
     *
     * @param userId
     * @return
     */
    public List<ReviewModel> getReviewsForUser(int userId) {
        return reviewRepo.findByReviewedUserId(userId).stream()
                .map(converter::toModel)
                .collect(Collectors.toList());
    }

    public List<ReviewModel> getReviewsByReviewer(int reviewerId) {
        return reviewRepo.findByReviewerId(reviewerId).stream()
                .map(converter::toModel)
                .collect(Collectors.toList());
    }

    /**
     * Create
     *
     * @param reviewerId
     * @param sellerId
     * @param itemId
     * @param rating
     * @param comment
     */
    public void saveReview(int reviewerId, int sellerId, int itemId, int rating, String comment) {
        ReviewEntity entity = new ReviewEntity();
        entity.setReviewerId(reviewerId);
        entity.setReviewedUserId(sellerId);
        entity.setRating(rating);
        entity.setComment(comment);

        reviewRepo.save(entity);
    }

    /**
     * Calculate the average ratings for a user
     *
     * @param userId
     * @return
     */
    public Double getAverageRating(int userId) {
        return reviewRepo.getAverageRating(userId);
    }

    /**
     * Get total count of reviews
     *
     * @param userId
     * @return
     */
    public Integer getReviewCount(int userId) {
        return reviewRepo.getReviewCount(userId);
    }

}
