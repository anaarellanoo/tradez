package com.anaarellano.tradez.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.anaarellano.tradez.models.UserModel;
import com.anaarellano.tradez.service.ReviewService;
import com.anaarellano.tradez.service.UserService;

/**
 * Review Controller 
 */
@Controller
@RequestMapping("/tradez")
public class ReviewController 
{
    private final ReviewService reviewService;
    private final UserService userService;

    /**
     * Constructor 
     * @param reviewService
     * @param userService
     */
    public ReviewController(ReviewService reviewService, UserService userService) 
    {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    /**
     * handles the submisson of the review 
     * @param sellerId
     * @param itemId
     * @param rating
     * @param comment
     * @param principal
     * @return
     */
    @PostMapping("/reviews/add")
    public String addReview(@RequestParam int sellerId, @RequestParam int itemId, @RequestParam int rating, @RequestParam String comment, Principal principal) 
    {
        UserModel reviewer = userService.findByUsername(principal.getName());

        reviewService.saveReview(reviewer.getId(), sellerId, itemId, rating, comment);        
        
        return "redirect:/tradez/messages/" + itemId + "/" + sellerId + "?success=Review+submitted!";
    }
    
}
