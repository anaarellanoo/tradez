package com.anaarellano.tradez.models;

/**
 * Category Entity 
 * -> Data/Repository Layers
 */
public class CategoryEntity 
{
    private int categoryId;
    private String categoryName;
    
    /**
     * Constructor 
     */
    public CategoryEntity() 
    {

    }

    /**
     * Parametized Constructor 
     * @param categoryId
     * @param categoryName
     */
    public CategoryEntity(int categoryId, String categoryName) {
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
