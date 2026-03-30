package com.anaarellano.tradez.models;

/**
 * Category Model 
 * -> Service/Controller Layers
 */
public class CategoryModel 
{
    private int categoryId;
    private String categoryName;

    /**
     * Constructor
     */
    public CategoryModel()
    {
        
    }

    /**
     * Parametized Constructor
     * @param categoryId
     * @param categoryName
     */
    public CategoryModel(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    /********************* GETTERS AND SETTER *********************/

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
}
