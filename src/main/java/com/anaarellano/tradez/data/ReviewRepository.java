package com.anaarellano.tradez.data;


import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.anaarellano.tradez.models.ReviewEntity;

/**
 * Review Repository 
 * Implements ReviewRepository Interface 
 * Handles on review actions 
 * saves, views, calculates rating, counts how many reviews a user has 
 */
@Repository
public class ReviewRepository implements ReviewRepositoryInterface 
{
    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor 
     * @param jdbcTemplate
     */
    public ReviewRepository(JdbcTemplate jdbcTemplate) 
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Save review 
     */
    @Override
    public void save(ReviewEntity review) 
    {
        String sql = "INSERT INTO reviews (reviewerId, reviewedUserId, rating, comment, dateCreated) VALUES (?, ?, ?, ?, NOW())";
        jdbcTemplate.update(sql, review.getReviewerId(), review.getReviewedUserId(), review.getRating(),
                review.getComment());
    }
    
    @Override
    public List<ReviewEntity> findByReviewerId(int userId) {
        String sql = "SELECT r.*, u.name, u.lastName FROM reviews r " +
                     "JOIN users u ON r.reviewedUserId = u.userId " +
                "WHERE r.reviewerId = ? ORDER BY r.dateCreated DESC";
                     
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ReviewEntity entity = new ReviewEntity();
            entity.setReviewId(rs.getInt("reviewId"));
            entity.setReviewerId(rs.getInt("reviewerId"));
            entity.setReviewedUserId(rs.getInt("reviewedUserId"));
            entity.setRating(rs.getInt("rating"));
            entity.setComment(rs.getString("comment"));

            entity.setReviewerName(rs.getString("name") + " " + rs.getString("lastName"));
            entity.setReviewedName(rs.getString("name") + " " + rs.getString("lastName"));

            entity.setDateCreated(rs.getTimestamp("dateCreated").toLocalDateTime());
            return entity;
        }, userId);
    }
    

    /**
     * Find a review based on the userId
     * to show a users reviews 
     */
    @Override
    public List<ReviewEntity> findByReviewedUserId(int userId) 
    {
        String sql = "SELECT r.*, u.name, u.lastName FROM reviews r " +
                     "JOIN users u ON r.reviewerId = u.userId " +
                     "WHERE r.reviewedUserId = ? ORDER BY r.dateCreated DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> 
        {
            ReviewEntity entity = new ReviewEntity();
            entity.setReviewId(rs.getInt("reviewId"));
            entity.setReviewerId(rs.getInt("reviewerId"));
            entity.setReviewedUserId(rs.getInt("reviewedUserId"));
            entity.setRating(rs.getInt("rating"));
            entity.setComment(rs.getString("comment"));
            entity.setReviewerName(rs.getString("name") + " " + rs.getString("lastName"));
            entity.setDateCreated(rs.getTimestamp("dateCreated").toLocalDateTime());
            return entity;
        }, userId);
    }

    /**
     * Calculate the average rating
     */
    @Override
    public Double getAverageRating(int userId) 
    {
        String sql = "SELECT AVG(rating) FROM reviews WHERE reviewedUserId = ?";
        Double avg = jdbcTemplate.queryForObject(sql, Double.class, userId);
        return (avg != null) ? avg : 0.0;
    }

    /**
     * Count the total reviews given
     * to user
     */
    @Override
    public Integer getReviewCount(int userId) 
    {
        String sql = "SELECT COUNT(*) FROM reviews WHERE reviewedUserId = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId);
    }
}