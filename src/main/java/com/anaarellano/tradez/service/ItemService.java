package com.anaarellano.tradez.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.anaarellano.tradez.data.ItemRepository;
import com.anaarellano.tradez.data.MessageRepository;
import com.anaarellano.tradez.models.Converter;
import com.anaarellano.tradez.models.ItemEntity;
import com.anaarellano.tradez.models.ItemModel;
import com.anaarellano.tradez.models.UserModel;

/**
 * Item Service
 * Handles the create, update, delete,
 * filtering, search, and saving/heart item
 * Works with repository
 */
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final MessageRepository messageRepository;
    private final Converter converter;
    private final NotificationService notificationService;

    /**
     * Construtor
     * 
     * @param itemRepository
     * @param messageRepository
     * @param converter
     * @param notificationService
     */
    public ItemService(ItemRepository itemRepository, MessageRepository messageRepository, Converter converter,
            NotificationService notificationService) {
        this.itemRepository = itemRepository;
        this.converter = converter;
        this.messageRepository = messageRepository;
        this.notificationService = notificationService;
    }

    /**
     * Find item by Id
     * 
     * @param itemId
     * @return
     */
    public ItemModel findById(int itemId) {
        ItemEntity entity = itemRepository.findById(itemId);

        return entity != null ? converter.toModel(entity) : null;
    }

    /**
     * Get all items in the database
     * 
     * @return
     */
    public List<ItemModel> getAllItems() {
        List<ItemEntity> entities = itemRepository.getAllItems();

        List<ItemModel> models = new ArrayList<>();

        for (ItemEntity e : entities)
            models.add(converter.toModel(e));

        return models;
    }

    /**
     * Save a new item
     * 
     * @param item
     * @param imageFile
     * @param user
     * @return
     * @throws IOException
     */
    public ItemModel save(ItemModel item, MultipartFile imageFile, UserModel user) throws IOException {
        item.setUserId(user.getId());

        // Check if not online to set location
        if (item.getIsOnline() != null && !item.getIsOnline()) {
            item.setLatitude(user.getLatitude());
            item.setLongitude(user.getLongitude());
            item.setLocation(user.getLocation());
        } else {
            item.setLocation("Online");
        }

        ItemEntity entity = converter.toEntity(item);
        ItemEntity savedEntity = itemRepository.save(entity);

        // handle the image upload
        if (imageFile != null && !imageFile.isEmpty()) {
            Path uploadPath = Paths.get("uploads/items");

            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            try (var inputStream = imageFile.getInputStream()) {
                Files.copy(inputStream, uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                String finalUrl = "/uploads/items/" + fileName;

                itemRepository.saveImage(savedEntity.getItemId(), finalUrl);
            }
        }

        return converter.toModel(savedEntity);
    }

    /**
     * Update an existing item
     * 
     * @param updatedItem
     * @param imageFile
     * @param user
     * @throws IOException
     */
    public void update(ItemModel updatedItem, MultipartFile imageFile, UserModel user) throws IOException {
        ItemModel existingItem = findById(updatedItem.getItemId());

        if (existingItem == null) {
            throw new RuntimeException("Item not found");
        }

        // Only let owner update
        if (!existingItem.getUserId().equals(user.getId())) {
            throw new SecurityException("Unauthorized!");
        }

        // Keep the status
        updatedItem.setStatus(existingItem.getStatus());

        // Handle images
        if (imageFile != null && !imageFile.isEmpty()) {
            Path uploadPath = Paths.get("uploads/items");

            // Get existing image URL
            ItemEntity existingEntity = itemRepository.findById(updatedItem.getItemId());
            String oldImageUrl = existingEntity.getImageUrl();

            // Delete old image
            if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                Path oldImagePath = Paths.get(".", oldImageUrl);
                try {
                    Files.deleteIfExists(oldImagePath);
                } catch (IOException e) {
                    System.err.println("Failed to delete old image: " + oldImagePath);
                    e.printStackTrace();
                }
            }

            // Save new image
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            try (var inputStream = imageFile.getInputStream()) {
                Files.copy(inputStream, uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                String finalUrl = "/uploads/items/" + fileName;

                itemRepository.updateImage(updatedItem.getItemId(), finalUrl);
            }
        }

        // Location handling
        if (updatedItem.getIsOnline() != null && !updatedItem.getIsOnline()) {
            updatedItem.setLatitude(user.getLatitude());
            updatedItem.setLongitude(user.getLongitude());
            updatedItem.setLocation(user.getLocation());

        } else {
            updatedItem.setLocation("Online");
            updatedItem.setLatitude(null);
            updatedItem.setLongitude(null);
        }

        updatedItem.setUserId(user.getId());
        itemRepository.update(converter.toEntity(updatedItem));
    }

    /**
     * Delete an item
     * 
     * @param itemId
     * @param userId
     */
    public void Delete(int itemId, int userId) {
        ItemEntity item = itemRepository.findById(itemId);

        // Security check only owner can delete
        if (item != null && item.getUserId() == userId) {
            itemRepository.delete(itemId);
        } else {
            throw new SecurityException("You aren't allowed to delete this!");
        }
    }

    /**
     * Update the status of the item
     * avaliable, pending , traded
     * 
     * @param itemId
     * @param newStatus
     * @param userId
     */
    public void updateItemStatus(int itemId, String newStatus, int userId) {
        ItemModel item = findById(itemId);

        // Secuirty only owner can change status
        if (item != null && item.getUserId().equals(userId)) {
            itemRepository.updateStatus(itemId, newStatus);
        }
    }

    /**
     * Check if a user can leave a reviews
     * based on the amount of messages exchanged
     * 
     * @param senderId
     * @param receiverId
     * @param itemId
     * @return
     */
    public boolean canLeaveReview(int senderId, int receiverId, int itemId) {
        int messageCount = messageRepository.countMessagesInThread(senderId, receiverId, itemId);
        return messageCount >= 5;
    }

    /**
     * Get items using the param as filters
     * 
     * @param query
     * @param catId
     * @param condIds
     * @param desiredCategoryId
     * @param isOnline
     * @param currentUser
     * @param maxDist
     * @return
     */
    public List<ItemModel> getFilteredItems(String query, Integer catId, List<Integer> condIds,
            Integer desiredCategoryId,
            Boolean isOnline, UserModel currentUser, Integer maxDist) {
        Double userLat = (currentUser != null) ? currentUser.getLatitude() : null;
        Double userLng = (currentUser != null) ? currentUser.getLongitude() : null;

        List<ItemEntity> entities = itemRepository.findWithFilters(
                query, catId, condIds, desiredCategoryId, isOnline, userLat, userLng, maxDist);

        return entities.stream().map(Converter::toModel).collect(Collectors.toList());
    }

    /**
     * Search items by text
     * 
     * @param keyword
     * @return
     */
    public List<ItemModel> search(String keyword) {
        List<ItemEntity> entities = itemRepository.searchItems(keyword);
        return entities.stream().map(Converter::toModel).collect(Collectors.toList());
    }

    /**
     * Save or Heart an item to add to favorit list
     * send notification
     * 
     * @param userId
     * @param itemId
     */
    public void saveItem(int userId, int itemId) {
        itemRepository.saveItemToUserList(userId, itemId);

        ItemModel item = findById(itemId);

        notificationService.send(item.getUserId(), null, itemId, "SAVE",
                "Your item '" + item.getItemName() + "' was saved!");
    }

    /**
     * Remove an item from favorites
     * 
     * @param userId
     * @param itemId
     */
    public void unsaveItem(int userId, int itemId) {
        itemRepository.removeItemFromUserList(userId, itemId);
    }

    /**
     * Check if an item is saved to favorites
     * 
     * @param userId
     * @param itemId
     * @return
     */
    public boolean isItemSaved(int userId, int itemId) {
        return itemRepository.checkIfSaved(userId, itemId);
    }

    /**
     * Get all the saved items for a user
     * 
     * @param userId
     * @return
     */
    public List<ItemModel> getSavedItemsForUser(int userId) {
        return itemRepository.findSavedByUserId(userId).stream()
                .map(entity -> {
                    ItemModel model = converter.toModel(entity);
                    model.setIsSaved(true);
                    return model;
                }).collect(Collectors.toList());

    }

    /**
     * Get all the items posyed by the specific user
     * 
     * @param userId
     * @return
     */
    public List<ItemModel> getItemsByUserId(int userId) {
        List<ItemEntity> entities = itemRepository.findByUserId(userId);
        return entities.stream().map(entity -> converter.toModel(entity)).collect(Collectors.toList());
    }

}