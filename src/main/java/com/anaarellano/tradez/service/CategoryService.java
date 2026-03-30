package com.anaarellano.tradez.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.anaarellano.tradez.data.CategoryRepository;
import com.anaarellano.tradez.models.CategoryEntity;
import com.anaarellano.tradez.models.CategoryModel;
import com.anaarellano.tradez.models.Converter;

/**
 * Category Service
 * fetches from the repo
 */
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final Converter converter;

    /**
     * Constructor
     * 
     * @param categoryRepository
     * @param converter
     */
    public CategoryService(CategoryRepository categoryRepository, Converter converter) {
        this.categoryRepository = categoryRepository;
        this.converter = converter;
    }

    /**
     * Get all the categories from the database
     * 
     * @return
     */
    public List<CategoryModel> getAllCategories() {
        List<CategoryEntity> entities = categoryRepository.findAll();

        List<CategoryModel> models = new ArrayList<>();

        for (CategoryEntity entity : entities) {
            CategoryModel model = converter.toModel(entity);
            models.add(model);
        }

        return models;
    }

}
