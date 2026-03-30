package com.anaarellano.tradez.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.anaarellano.tradez.models.ItemModel;
import com.anaarellano.tradez.models.ReviewModel;
import com.anaarellano.tradez.models.UserModel;
import com.anaarellano.tradez.service.ItemService;
import com.anaarellano.tradez.service.ReviewService;
import com.anaarellano.tradez.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Profile Controller 
 * manages profile actions view, edits, delete
 */
@Controller
@RequestMapping("/tradez/profile")
public class ProfileController 
{

    private final UserService userService;
    private final ItemService itemService;
    private final ReviewService reviewService;

    /**
     * Constructor 
     * @param userService
     * @param itemService
     * @param reviewService
     */
    public ProfileController(UserService userService, ItemService itemService, ReviewService reviewService) 
    {
        this.userService = userService;
        this.itemService = itemService;
        this.reviewService = reviewService;
    }

    /**
     * Shows the profile
     * items, personal info, form for changes 
     * @param model
     * @param auth
     * @return
     */
    @GetMapping
    public String viewProfile(Model model, Authentication auth) 
    {
        UserModel user = userService.findByUsername(auth.getName()); 
        List<ItemModel> items = itemService.getItemsByUserId(user.getId());
        List<ReviewModel> reviewsGiven = reviewService.getReviewsByReviewer(user.getId());
        model.addAttribute("reviewsGiven", reviewsGiven);

        model.addAttribute("items", items);
        model.addAttribute("user", user);

        return "profile"; 
    }
    
    /**
     * View other peoples profile 
     * 
     * @param id
     * @param model
     * @param principal
     * @return
     */   
    @GetMapping("/{id}")
    public String viewPublicProfile(@PathVariable int id, Model model, Principal principal) 
    {
        UserModel seller = userService.findById(id);
        if (seller == null) 
        {
            return "redirect:/tradez/home";
        }

        if (principal != null && userService.findByUsername(principal.getName()).getId() == id) 
        {
            return "redirect:/tradez/profile";
        }

        model.addAttribute("seller", seller);
        model.addAttribute("items", itemService.getItemsByUserId(id));
        model.addAttribute("reviews", reviewService.getReviewsForUser(id));
        
        model.addAttribute("sellerAverageRating", reviewService.getAverageRating(id));
        model.addAttribute("sellerReviewCount", reviewService.getReviewCount(id));
        
        return "publicprofile"; 
    }

    /**
     * Save any changes made 
     * name, email, profile pic 
     * @param formUser
     * @param profileImage
     * @param auth
     * @return
     * @throws IOException
     */
    @PostMapping("/edit")
    public String editProfile(@ModelAttribute("user") UserModel formUser, @RequestParam("profileImage") MultipartFile profileImage, Authentication auth) throws IOException 
    {
        userService.updateProfile(auth.getName(), formUser, profileImage);

        return "redirect:/tradez/profile";
    }

    /**
     * Delete the profile permentaly 
     * @param auth
     * @return
     */
    @PostMapping("/delete")
    public String deleteProfile(Authentication auth, HttpServletRequest request, HttpServletResponse response) 
    {
        UserModel user = userService.findByUsername(auth.getName()); 
        if (user != null) 
        {
            userService.deleteUser(user.getId());
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/tradez/checkemail"; 
    }

    @PostMapping("/update-location")
    @ResponseBody
    public String updateLocation(@RequestBody Map<String, Object> data, Principal principal) 
    {
        String username = principal.getName();
        Double lat = ((Number) data.get("latitude")).doubleValue();
        Double lng = ((Number) data.get("longitude")).doubleValue();
        String location = (String) data.get("location");

        userService.updateCoordinates(username, lat, lng, location);
        return "ok";
    }
}