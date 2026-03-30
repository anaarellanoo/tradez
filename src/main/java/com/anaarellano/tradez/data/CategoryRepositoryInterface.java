package com.anaarellano.tradez.data;

import java.util.List;

import com.anaarellano.tradez.models.CategoryEntity;

/**
 * Category Interface 
 */
public interface CategoryRepositoryInterface 
{
    List<CategoryEntity> findAll();
    String findCategoryName(int id);
}
