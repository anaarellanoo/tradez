package com.anaarellano.tradez.models;

import org.springframework.stereotype.Component;

/*
 * This class is a converter for the UserModel and UserEntity
 * It converts between the two classes
 * It is used in the UserService to convert between the two classes
 * It is used in the UsersController to convert between the two classes
 */
@Component
public class Converter 
{
    // Convert UserEntity to UserModel
    public static UserModel toModel(UserEntity entity)
    {
        UserModel model = new UserModel();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setLastName(entity.getLastName());
        model.setUsername(entity.getUsername());
        model.setPassword(entity.getPassword());
        model.setEmail(entity.getEmail());
        model.setLocation(entity.getLocation());
        model.setLatitude(entity.getLatitude());
        model.setLongitude(entity.getLongitude());
        model.setCreatedAt(entity.getCreatedAt());
        model.setProfileImageURL(entity.getProfileImageURL());
        return model;
    }

    // Convert UserModel to UserEntity
    public static UserEntity toEntity(UserModel model)
    {
        UserEntity entity = new UserEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setLastName(model.getLastName());
        entity.setUsername(model.getUsername());
        entity.setPassword(model.getPassword());
        entity.setEmail(model.getEmail());
        entity.setLocation(model.getLocation());
        entity.setLatitude(model.getLatitude());
        entity.setLongitude(model.getLongitude());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setProfileImageURL(model.getProfileImageURL());
        return entity;
    }

    public static ItemModel toModel(ItemEntity entity) 
    {
        ItemModel model = new ItemModel();
        model.setItemId(entity.getItemId());
        model.setItemName(entity.getItemName());
        model.setDescription(entity.getDescription());
        model.setDateCreated(entity.getDateCreated());
        model.setStatus(entity.getStatus());
        model.setLocation(entity.getLocation());
        model.setLatitude(entity.getLatitude());
        model.setLongitude(entity.getLongitude());
        model.setIsOnline(entity.getIsOnline());
        model.setUserId(entity.getUserId());
        model.setCategoryId(entity.getCategoryId());
        model.setConditionId(entity.getConditionId());
        model.setDesiredCategoryId(entity.getDesiredCategoryId());
        model.setCategoryName(entity.getCategoryName());
        model.setConditionName(entity.getConditionName());
        model.setDesiredCategoryName(entity.getDesiredCategoryName());
        model.setUserFullName(entity.getUserFullName()); 
        model.setDesiredReturn(entity.getDesiredReturn());
        model.setImageUrl(entity.getImageUrl());
        model.setSellerAverageRating(entity.getSellerAverageRating());
        model.setSellerReviewCount(entity.getSellerReviewCount());
        model.setIsSaved(entity.getIsSaved());
        return model;
    }

    public static ItemEntity toEntity(ItemModel model) 
    {
        ItemEntity entity = new ItemEntity();
        entity.setItemId(model.getItemId());
        entity.setItemName(model.getItemName());
        entity.setDescription(model.getDescription());
        entity.setDateCreated(model.getDateCreated());
        entity.setStatus(model.getStatus());
        entity.setLocation(model.getLocation());
        entity.setLatitude(model.getLatitude());
        entity.setLongitude(model.getLongitude());
        entity.setIsOnline(model.getIsOnline());
        entity.setUserId(model.getUserId());
        entity.setCategoryId(model.getCategoryId());
        entity.setConditionId(model.getConditionId());
        entity.setDesiredCategoryId(model.getDesiredCategoryId());
        entity.setDesiredReturn(model.getDesiredReturn());
        entity.setImageUrl(model.getImageUrl());
        entity.setSellerAverageRating(model.getSellerAverageRating());
        entity.setSellerReviewCount(model.getSellerReviewCount());
        entity.setIsSaved(model.getIsSaved());
        return entity;
    }

    public static CategoryModel toModel(CategoryEntity entity)
    {
        CategoryModel model = new CategoryModel();
        model.setCategoryId(entity.getCategoryId());
        model.setCategoryName(entity.getCategoryName());
        return model;
    }

    public static CategoryEntity toEntity(CategoryModel model)
    {
        CategoryEntity entity = new CategoryEntity();
        entity.setCategoryId(model.getCategoryId());
        entity.setCategoryName(model.getCategoryName());
        return entity;
    }

    public static ConditionModel toModel(ConditionEntity entity)
    {
        ConditionModel model = new ConditionModel();
        model.setConditionId(entity.getConditionId());
        model.setConditionName(entity.getConditionName());
        model.setDescription(entity.getDescription());
        return model;
    }

    public static ConditionEntity toEntity(ConditionModel model)
    {
        ConditionEntity entity = new ConditionEntity();
        entity.setConditionId(model.getConditionId());
        entity.setConditionName(model.getConditionName());
        entity.setDescription(model.getDescription());
        return entity;
    }

    public ReviewModel toModel(ReviewEntity entity) 
    {
        ReviewModel model = new ReviewModel();
        model.setReviewId(entity.getReviewId());
        model.setReviewerName(entity.getReviewerName());
        model.setReviewedName(entity.getReviewedName());
        model.setRating(entity.getRating());
        model.setComment(entity.getComment());

        if (entity.getDateCreated() != null) 
        {
            java.time.Duration duration = java.time.Duration.between(entity.getDateCreated(), java.time.LocalDateTime.now());
            long seconds = duration.getSeconds();

            if (seconds < 60) model.setRelativeTime("Just now");
            else if (seconds < 3600) model.setRelativeTime((seconds / 60) + "m ago");
            else if (seconds < 86400) model.setRelativeTime((seconds / 3600) + "h ago");
            else model.setRelativeTime((seconds / 86400) + "d ago");
        }
        return model;
    }

    public ReviewEntity toEntity(ReviewModel model) 
    {
        ReviewEntity entity = new ReviewEntity();
        entity.setReviewId(model.getReviewId());
        entity.setReviewerName(model.getReviewerName());
        entity.setReviewedName(model.getReviewedName());
        entity.setRating(model.getRating());
        entity.setComment(model.getComment());
        return entity;
    }

    public MessageModel toModel(MessageEntity entity)
    {
        MessageModel model = new MessageModel();
        model.setMessageId(entity.getMessageId());
        model.setSenderId(entity.getSenderId());
        model.setSenderName(entity.getSenderName());
        model.setReceiverId(entity.getReceiverId());
        model.setItemId(entity.getItemId());
        model.setContent(entity.getContent());
        model.setTimestamp(entity.getTimestamp());

        return model;
    }

    public MessageEntity toEntity(MessageModel model)
    {
        MessageEntity entity = new MessageEntity();
        entity.setMessageId(model.getMessageId());
        entity.setSenderId(model.getSenderId());
        entity.setSenderName(model.getSenderName());
        entity.setReceiverId(model.getReceiverId());
        entity.setItemId(model.getItemId());
        entity.setContent(model.getContent());
        entity.setTimestamp(model.getTimestamp());

        return entity;
    }
    
}
