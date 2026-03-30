package com.anaarellano.tradez.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.anaarellano.tradez.models.ItemModel;
import com.anaarellano.tradez.models.UserModel;
import com.anaarellano.tradez.service.CategoryService;
import com.anaarellano.tradez.service.ConditionService;
import com.anaarellano.tradez.service.ItemService;
import com.anaarellano.tradez.service.UserService;

/**
 * Home Controller 
 * Shows all the items 
 */
@Controller
@RequestMapping("/tradez")
public class HomeController 
{

    private final ItemService itemService;
    private final CategoryService categoryService;
    private final ConditionService conditionService;
    private final UserService userService;
    
    /**
     * Constructor
     * @param itemService
     * @param categoryService
     * @param conditionService
     * @param userService
     */
    public HomeController(ItemService itemService, CategoryService categoryService, ConditionService conditionService, UserService userService) 
    {
        this.itemService = itemService;
        this.categoryService = categoryService;
        this.conditionService = conditionService;
        this.userService = userService;
    }

    /**
     * Home Get Request for homepage 
     * Fetches all the items 
     * Checks if they are saved byt the user  
     * @param model
     * @param principal
     * @return
     */
    @GetMapping("/home")
    public String home(Model model, java.security.Principal principal) 
    {
        List<ItemModel> items = itemService.getAllItems();

        if (principal != null) 
        {
            UserModel user = userService.findByUsername(principal.getName());
            for (ItemModel item : items) 
            {
                boolean savedStatus = itemService.isItemSaved(user.getId(), item.getItemId());
                item.setIsSaved(savedStatus);
            }
        }

        model.addAttribute("items", items);
        model.addAttribute("item", new ItemModel());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("conditions", conditionService.getAllConditions());
        return "home";
    }
}