package com.anaarellano.tradez.data;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.anaarellano.tradez.models.MessageEntity;

/**
 * Message Repository 
 * Implements the MessageRepository Interface 
 * Handles all message actions
 * saves, chats, counts messages, and inbox 
 */
@Repository
public class MessageRepository implements MessageRepositoryInterface 
{
    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor
     * @param jdbcTemplate
     */
    public MessageRepository(JdbcTemplate jdbcTemplate) 
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Save a new message
     */
    @Override
    public void save(MessageEntity msg) 
    {
        String sql = "INSERT INTO messages (senderId, receiverId, itemId, content) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, msg.getSenderId(), msg.getReceiverId(), msg.getItemId(), msg.getContent());
    }

    /**
     * Gets chat between two specific 
     * users, chat history 
     */
    @Override
    public List<MessageEntity> getChatHistory(int userA, int userB, int itemId) 
    {
        String sql = "SELECT * FROM messages WHERE itemId = ? AND " +
                     "((senderId = ? AND receiverId = ?) OR (senderId = ? AND receiverId = ?)) " +
                     "ORDER BY timestamp ASC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> 
        {
            MessageEntity entity = new MessageEntity();
            entity.setMessageId(rs.getInt("messageId"));
            entity.setSenderId(rs.getInt("senderId"));
            entity.setReceiverId(rs.getInt("receiverId"));
            entity.setItemId(rs.getInt("itemId"));
            entity.setContent(rs.getString("content"));
            entity.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
            return entity;
        }, itemId, userA, userB, userB, userA);
    }

    /**
     * Counts total messages in a chat 
     * for reviews 
     */
    @Override
    public int countMessagesInThread(int userA, int userB, int itemId) 
    {
        String sql = "SELECT COUNT(*) FROM messages WHERE itemId = ? AND " +
                     "((senderId = ? AND receiverId = ?) OR (senderId = ? AND receiverId = ?))";
        return jdbcTemplate.queryForObject(sql, Integer.class, itemId, userA, userB, userB, userA);
    }

    /**
     * Get the user inbox, 
     * all the coversations 
     * User Specific 
     * @param userId
     * @return
     */
    public List<Map<String, Object>> getUserInbox(int userId) 
    {
        String sql = "SELECT m.*, i.itemName, u.name, u.lastName " +
                    "FROM messages m " +
                    "JOIN items i ON m.itemId = i.itemId " +
                    "JOIN users u ON (CASE WHEN m.senderId = ? THEN m.receiverId ELSE m.senderId END) = u.userId " +
                    "WHERE (m.senderId = ? OR m.receiverId = ?) " +
                    "AND m.messageId IN (" +
                    "    SELECT MAX(messageId) " +
                    "    FROM messages " +
                    "    WHERE senderId = ? OR receiverId = ? " +
                    "    GROUP BY itemId, " +
                    "             (CASE WHEN senderId < receiverId THEN senderId ELSE receiverId END), " +
                    "             (CASE WHEN senderId > receiverId THEN senderId ELSE receiverId END)" +
                    ") " +
                    "ORDER BY m.timestamp DESC";
                    
        return jdbcTemplate.queryForList(sql, userId, userId, userId, userId, userId);
    }

}