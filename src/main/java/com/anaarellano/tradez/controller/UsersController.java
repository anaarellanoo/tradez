package com.anaarellano.tradez.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.anaarellano.tradez.models.UserModel;
import com.anaarellano.tradez.service.UserService;

import jakarta.validation.Valid;


/**
 * Users Controller
 * Handles the actions for a user 
 * register, login
 * updates the users location 
 */
@Controller
@RequestMapping("/tradez")
public class UsersController 
{

    private final UserService userService;

    /**
     * Constructor
     * @param userService
     */
    public UsersController(UserService userService)
    {
        this.userService = userService;
    }

    /**
     * Register the User 
     * get the user information
     * @param model
     * @return
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) 
    {
        model.addAttribute("user", new UserModel());
        return "register";
    }

    /**
     * Handles the registration info 
     * Checks error, if existing 
     * @param user
     * @param result
     * @param model
     * @return
     */
   @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserModel user, BindingResult result, Model model) 
    {
        if (result.hasErrors()) 
        {
            return "register";
        }

        try 
        {
            userService.register(user);
            return "redirect:/tradez/login";
        } 
        catch (RuntimeException e) 
        {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }
        
    /**
     * Show the login form
     * @param model
     * @return
     */    
    @GetMapping("/login")
    public String showLoginForm(Model model) 
    {
        model.addAttribute("user", new UserModel());
        model.addAttribute("pageTitle", "Login");
        return "login";
    }
}
