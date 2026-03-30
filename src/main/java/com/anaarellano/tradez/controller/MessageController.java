package com.anaarellano.tradez.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.anaarellano.tradez.models.ItemModel;
import com.anaarellano.tradez.models.MessageModel;
import com.anaarellano.tradez.models.UserModel;
import com.anaarellano.tradez.service.ItemService;
import com.anaarellano.tradez.service.MessageService;
import com.anaarellano.tradez.service.NotificationService;
import com.anaarellano.tradez.service.UserService;

/**
 * Messages Controller 
 * handls all message things
 * viewing, sending, conversations 
 */
@Controller
@RequestMapping("/tradez")
public class MessageController 
{

    private final MessageService messageService;
    private final UserService userService;
    private final ItemService itemService;
    private final NotificationService notificationService;

    /**
     * Constructor
     * @param messageService
     * @param userService
     * @param itemService
     * @param notificationService
     */
    public MessageController(MessageService messageService, UserService userService, ItemService itemService, NotificationService notificationService) 
    {
        this.messageService = messageService;
        this.userService = userService;
        this.itemService = itemService;
        this.notificationService = notificationService;
    }

    /**
     * Sends message
     * saves the message and then reloads the chat
     * @param receiverId
     * @param itemId
     * @param content
     * @param principal
     * @return
     */
    @PostMapping("/message/send")
    public String sendMessage(@RequestParam int receiverId, @RequestParam int itemId, @RequestParam String content, Principal principal) 
    {
        UserModel sender = userService.findByUsername(principal.getName());
        messageService.sendMessage(sender.getId(), receiverId, itemId, content);
        
        return "redirect:/tradez/messages/" + itemId + "/" + receiverId;
    }

    /**
     * Loads the history of chats between two people 
     * Specific item Conversations 
     * Checks if you can leave a review 
     * Marks notif as read once chat is open 
     * @param itemId
     * @param partnerId
     * @param model
     * @param principal
     * @return
     */
    @GetMapping("/messages/{itemId}/{partnerId}")
    public String viewChat(@PathVariable int itemId, @PathVariable int partnerId, Model model, Principal principal) 
    {
        UserModel currentUser = userService.findByUsername(principal.getName());
        UserModel partner = userService.findById(partnerId);
        ItemModel item = itemService.findById(itemId);        
        
        List<MessageModel> messages = messageService.getConversation(currentUser.getId(), partnerId, itemId);

        int messageCount = messageService.countMessagesInThread(currentUser.getId(), partnerId, itemId);
        
        model.addAttribute("messages", messages);
        model.addAttribute("item", item);
        model.addAttribute("chatPartnerId", partnerId);
        model.addAttribute("chatPartnerFullName", partner.getName() + " " + partner.getLastName());
        model.addAttribute("currentUserId", currentUser.getId());
        model.addAttribute("canReview", messageCount >= 5);
        
       notificationService.markChatNotificationsAsRead(currentUser.getId(), partnerId, itemId);
        
        return "messages"; 
    }

    /**
     * Shows the list of all the conversations you have
     * @param model
     * @param principal
     * @return
     */
    @GetMapping("/inbox")
    public String viewInbox(Model model, Principal principal) 
    {
        UserModel user = userService.findByUsername(principal.getName());
        
        List<Map<String, Object>> conversations = messageService.getUserInbox(user.getId());
        
        model.addAttribute("conversations", conversations);
        model.addAttribute("currentUserId", user.getId());

        return "inbox"; 
    }

    
}