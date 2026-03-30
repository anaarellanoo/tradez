package com.anaarellano.tradez.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.anaarellano.tradez.models.ItemModel;
import com.anaarellano.tradez.models.ReviewModel;
import com.anaarellano.tradez.models.UserModel;
import com.anaarellano.tradez.service.CategoryService;
import com.anaarellano.tradez.service.ConditionService;
import com.anaarellano.tradez.service.ItemService;
import com.anaarellano.tradez.service.ReviewService;
import com.anaarellano.tradez.service.UserService;

/**
 * Item Controller 
 * Manages all the actions for item 
 * Search, create, file upload, saving items, delete
 */
@Controller
@RequestMapping("/tradez")
public class ItemController 
{
    private final ItemService itemService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ConditionService conditionService;
    private final ReviewService reviewService; 

    /**
     * Constructor
     * @param itemService
     * @param userService
     * @param categoryService
     * @param conditionService
     * @param reviewService
     */
    public ItemController(ItemService itemService, UserService userService, CategoryService categoryService, ConditionService conditionService, ReviewService reviewService) 
    {
        this.itemService = itemService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.conditionService = conditionService;
        this.reviewService = reviewService; 
    }

   
    /**
     * Handles the searching and filtering for items 
     * using the params
     * @param query
     * @param categoryId
     * @param conditionId
     * @param desiredCategoryId
     * @param dist
     * @param model
     * @param principal
     * @return
     */
    @GetMapping("/search")
    public String search(@RequestParam(value = "query", required = false) String query,
        @RequestParam(value = "cat", required = false) Integer categoryId,
        @RequestParam(value = "cond", required = false) List<Integer> conditionId,
        @RequestParam(value = "desiredCat", required = false) Integer desiredCategoryId,
        @RequestParam(value = "dist", required = false) String dist,
        Model model, Principal principal) 
    {

        UserModel currentUser = userService.findByUsername(principal.getName());

        boolean hasFilters = (categoryId != null) || (conditionId != null && !conditionId.isEmpty()) || (dist != null && !dist.isEmpty());

        List<ItemModel> results;
        if (!hasFilters && query != null && !query.isEmpty()) 
        {
            results = itemService.search(query);
        } 
        else 
        {
            Boolean isOnline = "online".equals(dist);
            Integer distance = null;

            if (dist != null && !dist.isEmpty() && !isOnline) 
            {
                try 
                {
                    distance = Integer.parseInt(dist);
                } 
                catch (NumberFormatException e) 
                { 
                    distance = null; 
                }
            }

            results = itemService.getFilteredItems(query, categoryId, conditionId, desiredCategoryId, isOnline, currentUser, distance);
        }

        model.addAttribute("items", results);
        model.addAttribute("searchQuery", query);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("selectedConditions", conditionId);
        model.addAttribute("online", "online".equals(dist));
        model.addAttribute("dist", dist != null && !dist.isEmpty() ? dist : null);
        
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("conditions", conditionService.getAllConditions());
        model.addAttribute("desiredCategoryId", desiredCategoryId); // for sticky UI
        model.addAttribute("item", new ItemModel());

        return "home";
    }

    /**
     * Display the details for an item, 
     * seller reviews and a sidebar items
     * @param id
     * @param model
     * @param principal
     * @return
     */
    @GetMapping("/item/{id}")
    public String viewItem(@PathVariable("id") int id, Model model, Principal principal) 
    {

        ItemModel item = itemService.findById(id);

        // Sidebar logic to suggest other items 
        List<ItemModel> sidebarItems = itemService.getAllItems().stream()
            .filter(i -> i.getItemId() != id)
            .limit(6)
            .toList();
            
        if (principal != null) 
        {
            UserModel currentUser = userService.findByUsername(principal.getName());
            model.addAttribute("currentUserId", currentUser.getId());
            model.addAttribute("isSaved", itemService.isItemSaved(currentUser.getId(), id));
        }

        // Fetch review details 
        List<ReviewModel> reviews = reviewService.getReviewsForUser(item.getUserId());
        item.setSellerAverageRating(reviewService.getAverageRating(item.getUserId()));
        item.setSellerReviewCount(reviewService.getReviewCount(item.getUserId()));

        model.addAttribute("item", item);
        model.addAttribute("reviews", reviews);
        model.addAttribute("sidebarItems", sidebarItems);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("conditions", conditionService.getAllConditions());
        model.addAttribute("isEditing", false); 
        
        return "item";
    }

    /**
     * Create/Save an item 
     * file system, and location that takes from the user profile 
     * @param item
     * @param imageFiles
     * @param principal
     * @return
     * @throws IOException
     */
    @PostMapping("/save")
    public String saveItem(@ModelAttribute("item") ItemModel item, 
                        @RequestParam("imageFile") MultipartFile imageFile, 
                        Principal principal) throws IOException 
    {
       UserModel user = userService.findByUsername(principal.getName());
        
        // Tip: Move that big Path/Files logic into this service method!
        ItemModel savedItem = itemService.save(item, imageFile, user);
        
        return "redirect:/tradez/item/" + savedItem.getItemId();
    }

    /**
     * Edit an item
     * Opens the edit form 
     * reload page correctly with sidebar 
     * and owner or customer details 
     * 
     * @param id
     * @param model
     * @param principal
     * @return
     */
    @GetMapping("/edit/{id}")
    public String editItem(@PathVariable("id") int id, Model model, Principal principal) 
    {
        ItemModel item = itemService.findById(id);
        UserModel currentUser = userService.findByUsername(principal.getName());

        List<ItemModel> sidebarItems = itemService.getAllItems().stream()
            .filter(i -> i.getItemId() != id)
            .limit(6)
            .toList();


        if (item != null && item.getUserId().equals(currentUser.getId())) 
        {
            model.addAttribute("currentUserId", currentUser.getId());
            model.addAttribute("item", item);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("conditions", conditionService.getAllConditions());
            model.addAttribute("sidebarItems", sidebarItems);
            // ADD THIS LINE: It tells the HTML to run the 'open modal' script
            model.addAttribute("isEditing", true); 
            
            return "item"; 
        }

        return "redirect:/tradez/home";
    }

    /**
     * Process the update
     * Updates the listing information 
     * If fails go back home
     * @param item
     * @param imageFile
     * @param principal
     * @return
     */
    @PostMapping("/update")
    public String updateItem(@ModelAttribute("item") ItemModel item, @RequestParam(value = "imageFile", required = false) MultipartFile imageFile, Principal principal) 
    {
        try 
        {
            UserModel user = userService.findByUsername(principal.getName());
            itemService.update(item, imageFile, user);
            return "redirect:/tradez/item/" + item.getItemId();
        } 
        catch (Exception e) 
        {
            return "redirect:/tradez/home?failed=true";
        }
    }

    /**
     * Deletes the item if the user is the owner
     * @param itemId
     * @param principal
     * @return
     */
    @PostMapping("/delete")
    public String deleteItem(@RequestParam("itemId") int itemId, Principal principal) 
    {
        try {
            UserModel user = userService.findByUsername(principal.getName());
            itemService.Delete(itemId, user.getId()); 
            return "redirect:/tradez/home";
        } catch (Exception e) {
            return "redirect:/tradez/home?failed=true";
        }
    }

    /**
     * Change the item status 
     * Avaliable, Pending, Traded 
     * @param itemId
     * @param newStatus
     * @param principal
     * @return
     */
    @PostMapping("/status/update")
    public String updateStatus(@RequestParam int itemId, @RequestParam String newStatus, Principal principal) 
    {
        UserModel user = userService.findByUsername(principal.getName());
        itemService.updateItemStatus(itemId, newStatus, user.getId());
        
        return "redirect:/tradez/item/" + itemId;
    }

    /**
     * Handles the save button 
     * Will save or unsave an item 
     * @param itemId
     * @param principal
     * @param request
     * @return
     */
    @PostMapping("/item/save-toggle")
    public String toggleSaveItem(@RequestParam int itemId, Principal principal, jakarta.servlet.http.HttpServletRequest request) 
    {
        UserModel user = userService.findByUsername(principal.getName());
        
        boolean isSaved = itemService.isItemSaved(user.getId(), itemId);
        
        if (isSaved) 
        {
            itemService.unsaveItem(user.getId(), itemId);
        } 
        else
        {
            itemService.saveItem(user.getId(), itemId);
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/tradez/home");

    }

    /**
     * Handles the list of user saved items 
     * @param model
     * @param principal
     * @return
     */
    @GetMapping("/saved")
    public String viewSavedItems(Model model, Principal principal) 
    {
        if (principal == null) return "redirect:/login";

        UserModel user = userService.findByUsername(principal.getName());
        
        List<ItemModel> savedItems = itemService.getSavedItemsForUser(user.getId());
        
        for (ItemModel item : savedItems) 
        {
            item.setIsSaved(true);
        }

        model.addAttribute("items", savedItems);
        model.addAttribute("item", new ItemModel()); 
        
        return "saved"; 
    }
}