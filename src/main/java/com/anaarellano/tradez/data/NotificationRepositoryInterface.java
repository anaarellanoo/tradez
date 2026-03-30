package com.anaarellano.tradez.data;

import java.util.List;

import com.anaarellano.tradez.models.NotificationModel;

/**
 * NotificationRepository Interface
 */
public interface NotificationRepositoryInterface 
{

    void createNotification(int recipientId, Integer senderId, Integer itemId, String type, String content);

    int getUnreadCount(int userId);

    void markAsRead(int id, int userId);

    List<NotificationModel> getAllForUser(int userId);

    void deleteById(int id);

    void markChatAsRead(int userId, int partnerId, int itemId);
    
}
