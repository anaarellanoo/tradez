package com.anaarellano.tradez.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.anaarellano.tradez.models.NotificationModel;
import com.anaarellano.tradez.models.UserModel;
import com.anaarellano.tradez.service.NotificationService;
import com.anaarellano.tradez.service.UserService;

/**
 * Notification Controller 
 * manages all the notification stuff 
 * shows list of alerts, clears them
 */
@Controller
@RequestMapping("/tradez")
public class NotificationController 
{

    private final NotificationService notificationService;
    private final UserService userService;

    /**
     * Constructor
     * @param notificationService
     * @param userService
     */
    public NotificationController(NotificationService notificationService, UserService userService) 
    {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    /**
     * Shows the notification page aand shows all the notifications
     * Will mark read
     * @param model
     * @param principal
     * @return
     */
    @GetMapping("/notifications")
    public String viewNotifications(Model model, Principal principal) 
    {
        UserModel user = userService.findByUsername(principal.getName());

        List<NotificationModel> list =
                notificationService.getAllForUser(user.getId());

        model.addAttribute("notifications", list);

        return "notification";
    }

    /**
     * Mark a notification as read
     * @param id
     * @param principal
     * @return
     */
    @PostMapping("/read/{id}")
    public String markAsRead(@PathVariable int id, Principal principal) 
    {

        UserModel user = userService.findByUsername(principal.getName());

        notificationService.markAsRead(id, user.getId());
        return "redirect:/tradez/notifications";
    }


    /**
     * Deletes the notification once been read 
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    public String deleteNotification(@PathVariable int id) 
    {
        notificationService.deleteNotification(id); 
        return "redirect:/tradez/notifications";
    }

}