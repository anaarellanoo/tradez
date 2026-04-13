package com.anaarellano.tradez.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.anaarellano.tradez.models.NotificationModel;
import com.anaarellano.tradez.models.UserModel;
import com.anaarellano.tradez.service.NotificationService;
import com.anaarellano.tradez.service.UserService;

/**
 * Global Interceptor for all the web controllers
 * Make sure notifications are injected into UI
 */
@ControllerAdvice
public class GlobalControllerAdvice {
    private final NotificationService notificationService;
    private final UserService userService;

    /**
     * Parameter Constructor
     * 
     * @param notificationService
     * @param userService
     */
    public GlobalControllerAdvice(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    /**
     * Injects the notification to model
     * 
     * @param model
     * @param principal
     */
    @ModelAttribute
    public void addNotificationsToModel(Model model, Principal principal) {
        if (principal == null || principal.getName() == null) {
            model.addAttribute("notifications", List.of());
            model.addAttribute("unreadCount", 0);
            return;
        }

        UserModel user = userService.findByUsername(principal.getName());

        if (user == null) {
            model.addAttribute("notifications", List.of());
            model.addAttribute("unreadCount", 0);
            return;
        }

        List<NotificationModel> notifications = notificationService.getAllForUser(user.getId());

        if (notifications == null) {
            notifications = List.of();
        }

        model.addAttribute("notifications", notifications);

        long unreadCount = notifications.stream()
                .filter(n -> n.getIsRead() != null && !n.getIsRead())
                .count();

        model.addAttribute("unreadCount", unreadCount);

    }
}
