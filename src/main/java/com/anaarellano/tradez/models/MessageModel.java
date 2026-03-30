package com.anaarellano.tradez.models;

import java.time.LocalDateTime;

/**
 * Message Model 
 * -> Service/Controller
 */
public class MessageModel 
{
    private int messageId;
    private int senderId;
    private String senderName; 
    private int receiverId;
    private int itemId;
    private String content;
    private LocalDateTime timestamp;

    /**
     * Constructor
     */
    public MessageModel()
    {

    }

    /**
     * Parametized Constructor 
     * @param content
     * @param itemId
     * @param messageId
     * @param receiverId
     * @param senderId
     * @param senderName
     * @param timestamp
     */
    public MessageModel(String content, int itemId, int messageId, int receiverId, int senderId, String senderName, LocalDateTime timestamp) {
        this.content = content;
        this.itemId = itemId;
        this.messageId = messageId;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.timestamp = timestamp;
    }

    /********************* GETTERS AND SETTER *********************/

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
}
