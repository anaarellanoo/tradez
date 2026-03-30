package com.anaarellano.tradez.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.anaarellano.tradez.data.NotificationRepositoryInterface;
import com.anaarellano.tradez.models.NotificationModel;

/**
 * Notification Service 
 * 
 */
@Service
public class NotificationService 
{

    private final NotificationRepositoryInterface notificationRepo;

    /**
     * Constructor
     * @param notificationRepo
     */
    public NotificationService(NotificationRepositoryInterface notificationRepo) 
    {
        this.notificationRepo = notificationRepo;
    }


    /**
     * Send a notification to user
     * @param recipientId
     * @param senderId
     * @param itemId
     * @param type
     * @param content
     */
    public void send(int recipientId, Integer senderId, Integer itemId, String type, String content) 
    {
        notificationRepo.createNotification(recipientId, senderId, itemId, type, content);
    }

    /**
     * Get all notification 
     * User Specific 
     * @param userId
     * @return
     */
    public List<NotificationModel> getAllForUser(int userId) 
    {
        return notificationRepo.getAllForUser(userId);
    }

    /**
     * Get all the unread notification count 
     * User Specific 
     * @param userId
     * @return
     */
    public int getUnreadCount(int userId) 
    {
        return notificationRepo.getUnreadCount(userId);
    }

    /**
     * Mark specific notification read
     * @param id
     * @param userId
     */
    public void markAsRead(int id, int userId) 
    {
        notificationRepo.markAsRead(id, userId);
    }

    /**
     * Mark allnotifications from a chat as read 
     * @param userId
     * @param partnerId
     * @param itemId
     */
    public void markChatNotificationsAsRead(int userId, int partnerId, int itemId) 
    {
        notificationRepo.markChatAsRead(userId, partnerId, itemId);
    }

    /**
     * Delete notification by Id
     * @param notificationId
     */
    public void deleteNotification(int notificationId) 
    {
        notificationRepo.deleteById(notificationId);
    }
}