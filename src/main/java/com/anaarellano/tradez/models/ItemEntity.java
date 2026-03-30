package com.anaarellano.tradez.models;

import java.time.LocalDateTime;

/**
 * Item Entity 
 * -> Data/Repository Layer
 */
public class ItemEntity 
{
    private Integer itemId;
    private String itemName;
    private String description;
    private LocalDateTime dateCreated;
    private String status = "available";
    private String location;
    private Double latitude;
    private Double longitude;
    private Boolean isOnline;
        
    private Integer userId;
    private String userFullName; 
    
    private Integer categoryId;
    private String categoryName; 
    
    private Integer conditionId;
    private String conditionName; 

    private String desiredReturn;
    private Integer desiredCategoryId;
    private String desiredCategoryName;

    private String imageUrl;

    private Double sellerAverageRating;
    private Integer sellerReviewCount;

    private boolean isSaved;

    /**
     * Constructor
     */
    public ItemEntity() 
    {

    }

    /**
     * Parametized COnstructor
     * @param categoryId
     * @param categoryName
     * @param conditionId
     * @param conditionName
     * @param dateCreated
     * @param description
     * @param desiredCategoryId
     * @param desiredCategoryName
     * @param desiredReturn
     * @param itemId
     * @param itemName
     * @param location
     * @param latitude
     * @param longitude
     * @param isOnline
     * @param userFullName
     * @param userId
     * @param imageUrl
     * @param sellerAverageRating
     * @param sellerReviewCount
     * @param isSaved
     */
    public ItemEntity(Integer categoryId, String categoryName, Integer conditionId, String conditionName, LocalDateTime dateCreated, String description, Integer desiredCategoryId, String desiredCategoryName, String desiredReturn, Integer itemId, String itemName, String location, Double latitude, Double longitude, Boolean isOnline, String userFullName, Integer userId, String imageUrl, Double sellerAverageRating, Integer sellerReviewCount, Boolean isSaved) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.conditionId = conditionId;
        this.conditionName = conditionName;
        this.dateCreated = dateCreated;
        this.description = description;
        this.desiredCategoryId = desiredCategoryId;
        this.desiredCategoryName = desiredCategoryName;
        this.desiredReturn = desiredReturn;
        this.itemId = itemId;
        this.itemName = itemName;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isOnline = isOnline;
        this.userFullName = userFullName;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.sellerAverageRating = sellerAverageRating;
        this.sellerReviewCount = sellerReviewCount;
        this.isSaved = isSaved;
    }

    /********************* GETTERS AND SETTER *********************/

    public String getRelativeTime() 
    {
        if (this.dateCreated == null) return "";
        
        java.time.Duration duration = java.time.Duration.between(this.dateCreated, java.time.LocalDateTime.now());
        long seconds = duration.getSeconds();

        if (seconds < 60) return "Just now";
        if (seconds < 3600) return (seconds / 60) + "m ago";
        if (seconds < 86400) return (seconds / 3600) + "h ago";
        return (seconds / 86400) + "d ago";
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getConditionId() {
        return conditionId;
    }

    public void setConditionId(Integer conditionId) {
        this.conditionId = conditionId;
    }

    public String getDesiredReturn() {
        return desiredReturn;
    }

    public void setDesiredReturn(String desiredReturn) {
        this.desiredReturn = desiredReturn;
    }

    public Integer getDesiredCategoryId() {
        return desiredCategoryId;
    }

    public void setDesiredCategoryId(Integer desiredCategoryId) {
        this.desiredCategoryId = desiredCategoryId;
    }

    public String getDesiredCategoryName() {
        return desiredCategoryName;
    }

    public void setDesiredCategoryName(String desiredCategoryName) {
        this.desiredCategoryName = desiredCategoryName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getSellerAverageRating() {
        return sellerAverageRating;
    }

    public void setSellerAverageRating(Double sellerAverageRating) {
        this.sellerAverageRating = sellerAverageRating;
    }

    public Integer getSellerReviewCount() {
        return sellerReviewCount;
    }

    public void setSellerReviewCount(Integer sellerReviewCount) {
        this.sellerReviewCount = sellerReviewCount;
    }

    public boolean getIsSaved() {
        return isSaved;
    }

    public void setIsSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

}