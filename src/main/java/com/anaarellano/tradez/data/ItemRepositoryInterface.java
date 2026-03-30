package com.anaarellano.tradez.data;

import java.util.List;

import com.anaarellano.tradez.models.ItemEntity;

/**
 * ItemRepository Interface
 */
public interface  ItemRepositoryInterface 
{
    ItemEntity findById(int itemId);
    ItemEntity save(ItemEntity item);
    List<ItemEntity> getAllItems();
    void update(ItemEntity item);
    void delete(int itemId);
    void updateStatus(int itemId, String status);
    void saveItemToUserList(int userId, int itemId);
    void removeItemFromUserList(int userId, int itemId);
    boolean checkIfSaved(int userId, int itemId);
    List<ItemEntity> findSavedByUserId(int userId);
    List<ItemEntity> findByUserId(int userId);
    
}
