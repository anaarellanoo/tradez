package com.anaarellano.tradez.data;

import java.util.List;
import java.util.Map;

import com.anaarellano.tradez.models.MessageEntity;

/**
 * MessageRepository Interface
 */
public interface MessageRepositoryInterface {
    void save(MessageEntity message);

    List<MessageEntity> getChatHistory(int userA, int userB, int itemId);

    int countMessagesInThread(int userA, int userB, int itemId);

    List<Map<String, Object>> getUserInbox(int userId);
}
