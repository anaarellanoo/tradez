package com.anaarellano.tradez.models;

import java.time.LocalDateTime;

/**
 * Notification Model 
 * -> Service/Controller
 */
public class NotificationModel 
{
    private int id; 
    private Integer recipientId;
    private Integer senderId;
    private Integer itemId; 
    private String type; 
    private String content; 
    private Boolean isRead; 
    private LocalDateTime dateCreated; 

    /**
     * Contructor
     */
    public NotificationModel() {
    }

    /**
     * Parametized COnstructor
     * @param content
     * @param dateCreated
     * @param id
     * @param isRead
     * @param itemId
     * @param recipientId
     * @param senderId
     * @param type
     */
    public NotificationModel(String content, LocalDateTime dateCreated, int id, Boolean isRead, Integer itemId, Integer recipientId, Integer senderId, String type) {
        this.content = content;
        this.dateCreated = dateCreated;
        this.id = id;
        this.isRead = isRead;
        this.itemId = itemId;
        this.recipientId = recipientId;
        this.senderId = senderId;
        this.type = type;
    }
    
    /********************* GETTERS AND SETTER *********************/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Integer recipientId) {
        this.recipientId = recipientId;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }


    
}
