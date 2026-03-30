package com.anaarellano.tradez.data;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.anaarellano.tradez.models.CategoryEntity;

/**
 * Category Repository
 * implements the CategoryRepositoryInterface
 */
@Repository
public class CategoryRepository implements CategoryRepositoryInterface
{
     private final JdbcTemplate jdbcTemplate;

     /**
      * Constructor
      * @param jdbcTemplate
      */
    public CategoryRepository(JdbcTemplate jdbcTemplate) 
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Find all the Categories 
     */
    @Override
    public List<CategoryEntity> findAll() 
    {
        String sql = "SELECT categoryId, categoryName FROM categories";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new CategoryEntity(
            rs.getInt("categoryId"),
            rs.getString("categoryName")
        ));
    }

    /**
     * Find the category name from the id
     */
    @Override
    public String findCategoryName(int id) 
    {
        String sql = "SELECT categoryName FROM categories WHERE categoryId = ?";
        
        return jdbcTemplate.queryForObject(sql, String.class, id);
  
    }
    
}
