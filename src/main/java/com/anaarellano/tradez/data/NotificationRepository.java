package com.anaarellano.tradez.data;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.anaarellano.tradez.models.NotificationModel;

/**
 * Notification Repository 
 * Implements NotificationRepository Interface 
 * Handles all notificationa actions 
 * creates, counts unread, marks read, deletes, 
 * and loads all user specific notif
 */
@Repository
public class NotificationRepository implements NotificationRepositoryInterface
{

    private final JdbcTemplate jdbcTemplate;

    /**
     * Construcotr
     * @param jdbcTemplate
     */
    public NotificationRepository(JdbcTemplate jdbcTemplate) 
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Create a notification 
     */
    @Override
    public void createNotification(int recipientId, Integer senderId, Integer itemId, String type, String content) 
    {
        String sql = "INSERT INTO notifications (recipientId, senderId, itemId, type, content) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, recipientId, senderId, itemId, type, content);
    }

    /**
     * Counts how many notifications 
     * a user has 
     */
    @Override
    public int getUnreadCount(int userId) 
    {
        String sql = "SELECT COUNT(*) FROM notifications WHERE recipientId = ? AND isRead = FALSE";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId);
    }

    /**
     * Mark a notification as read 
     */
    @Override
    public void markAsRead(int id, int userId) 
    {
        String sql = "UPDATE notifications SET isRead = TRUE WHERE id = ? AND recipientId = ?";
        jdbcTemplate.update(sql, id, userId);    
    }
    

    /**
     * Delete the notification by Id
     */
    @Override
    public void deleteById(int id) 
    {
        String sql = "DELETE FROM notifications WHERE id = ?";
        
        try 
        {
            int rowsAffected = jdbcTemplate.update(sql, id);
            if (rowsAffected == 0) 
            {
                System.out.println("Warning: No notification found with ID " + id + " to delete.");
            }
        } 
        catch (Exception e) 
        {
            System.err.println("Error deleting notification: " + e.getMessage());
        }
    }


    /**
     * Get all notifications for a user 
     * newest first 
     * Checks any null 
     */
    @Override
    public List<NotificationModel> getAllForUser(int userId) 
    {
        String sql = "SELECT * FROM notifications WHERE recipientId = ? AND isRead = false ORDER BY dateCreated DESC";
        
        return jdbcTemplate.query(sql, (rs, rowNum) -> 
        {
            NotificationModel n = new NotificationModel();
            n.setId(rs.getInt("id"));
            n.setRecipientId(rs.getInt("recipientId"));
            
            int senderId = rs.getInt("senderId");
            n.setSenderId(rs.wasNull() ? null : senderId);
            
            int itemId = rs.getInt("itemId");
            n.setItemId(rs.wasNull() ? null : itemId);
            
            n.setType(rs.getString("type"));
            n.setContent(rs.getString("content"));
            n.setIsRead(rs.getBoolean("isRead"));
            
            java.sql.Timestamp ts = rs.getTimestamp("dateCreated");
            if (ts != null) 
            {
                n.setDateCreated(ts.toLocalDateTime());
            }
            
            return n;
        }, userId);
    }

    /**
     * Mark all the notifications from a chat
     * as read when opened 
     */
    @Override
    public void markChatAsRead(int userId, int partnerId, int itemId) 
    {
        String sql = "UPDATE notifications SET isRead = true " +
                "WHERE recipientId = ? AND senderId = ? AND itemId = ? AND type = 'MESSAGE'";
        jdbcTemplate.update(sql, userId, partnerId, itemId);
    }
    
}
