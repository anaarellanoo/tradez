package com.anaarellano.tradez.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.anaarellano.tradez.data.MessageRepository;
import com.anaarellano.tradez.models.Converter;
import com.anaarellano.tradez.models.MessageEntity;
import com.anaarellano.tradez.models.MessageModel;

/**
 * Message Service 
 * Handles the crud for messages 
 * saves, views, chats. notif triggers
 */
@Service
public class MessageService 
{
    private final MessageRepository messageRepository;
    private final NotificationService notificationService;
    private final Converter converter;

    /**
     * Constructor
     * @param messageRepository
     * @param notificationService
     */
    public MessageService(MessageRepository messageRepository, NotificationService notificationService, Converter converter) 
    {
        this.messageRepository = messageRepository;
        this.notificationService = notificationService;
        this.converter = converter;
    }

    /**
     * Sends a message from one user to another 
     * Item Specific
     * @param senderId
     * @param receiverId
     * @param itemId
     * @param content
     */
    public void sendMessage(int senderId, int receiverId, int itemId, String content) 
    {
        MessageEntity msg = new MessageEntity();
        msg.setSenderId(senderId);
        msg.setReceiverId(receiverId);
        msg.setItemId(itemId);
        msg.setContent(content);

        messageRepository.save(msg);
        notificationService.send(
            msg.getReceiverId(),
            msg.getSenderId(),
            msg.getItemId(),
            "MESSAGE", 
            "You have a new message about an item!"
        );
    }

    /**
     * Get all the messages from two users 
     * create chat 
     * @param userA
     * @param userB
     * @param itemId
     * @return
     */
    public List<MessageModel> getConversation(int userA, int userB, int itemId) 
    {
        return messageRepository.getChatHistory(userA, userB, itemId).stream().map(converter::toModel)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all the messages for inbox 
     * User Specific 
     * @param userId
     * @return
     */
    public List<Map<String, Object>> getUserInbox(int userId) 
    {
        return messageRepository.getUserInbox(userId);
    }

    /**
     * Counts how many messages are in conversation 
     * @param userA
     * @param userB
     * @param itemId
     * @return
     */
    public int countMessagesInThread(int userA, int userB, int itemId) 
    {
        return messageRepository.countMessagesInThread(userA, userB, itemId);
    }
}