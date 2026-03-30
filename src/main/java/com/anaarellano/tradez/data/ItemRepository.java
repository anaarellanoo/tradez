package com.anaarellano.tradez.data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.anaarellano.tradez.models.ItemEntity;

/**
 * Item Repository 
 * Implements ItemRepository Interface 
 * Handles the CRUD, search, filtering, 
 * and saving images for an item 
 */
@Repository
public class ItemRepository implements ItemRepositoryInterface 
{

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor
     * @param jdbcTemplate
     */
    public ItemRepository(JdbcTemplate jdbcTemplate) 
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Get all the items 
     */
    @Override
    public List<ItemEntity> getAllItems() 
    {
        String sql = "SELECT i.*, u.name, u.lastName, c.categoryName, con.conditionName, " +
                        "dc.categoryName AS desiredCategoryName, " +
                        "(SELECT img.imageUrl FROM item_images img WHERE img.itemId = i.itemId LIMIT 1) AS imageUrl " +
                        "FROM items i " +
                        "JOIN users u ON i.userId = u.userId " +
                        "JOIN categories c ON i.categoryId = c.categoryId " +
                        "JOIN conditions con ON i.conditionId = con.conditionId " +
                        "LEFT JOIN categories dc ON i.desiredCategoryId = dc.categoryId " +
                        "WHERE i.status IN ('available','pending')" +
                        "ORDER BY i.dateCreated DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToEntity(rs));
    }

    /**
     * Find item by Id and get all the info 
     * including ratings 
     */
    @Override
    public ItemEntity findById(int itemId) 
    {
        String sql = "SELECT i.*, u.name, u.lastName, c.categoryName, con.conditionName, " +
        "dc.categoryName AS desiredCategoryName, " +
        "(SELECT img.imageUrl FROM item_images img WHERE img.itemId = i.itemId LIMIT 1) AS imageUrl, " +
        "(SELECT AVG(rating) FROM reviews WHERE reviewedUserId = i.userId) AS sellerAverageRating, " +
        "(SELECT COUNT(*) FROM reviews WHERE reviewedUserId = i.userId) AS sellerReviewCount " +
        "FROM items i " +
        "JOIN users u ON i.userId = u.userId " +
        "JOIN categories c ON i.categoryId = c.categoryId " +
        "JOIN conditions con ON i.conditionId = con.conditionId " +
        "LEFT JOIN categories dc ON i.desiredCategoryId = dc.categoryId " +
        "WHERE i.itemId = ?";

        try 
        {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> 
            {
                ItemEntity entity = mapRowToEntity(rs);
                entity.setSellerAverageRating(rs.getDouble("sellerAverageRating"));
                entity.setSellerReviewCount(rs.getInt("sellerReviewCount"));
                return entity;
            }, itemId);
        } 
        catch (org.springframework.dao.EmptyResultDataAccessException e) 
        {
            return null;
        }

    }

    /**
     * Find all items a user owns 
     * User Specific
     * as item cards so more info
     */
    @Override
    public List<ItemEntity> findByUserId(int userId) 
    {
        String sql = "SELECT i.*, u.name, u.lastName, c.categoryName, con.conditionName, " +
        "dc.categoryName AS desiredCategoryName, " +
        "(SELECT img.imageUrl FROM item_images img WHERE img.itemId = i.itemId LIMIT 1) AS imageUrl, " +
        "(SELECT AVG(rating) FROM reviews WHERE reviewedUserId = i.userId) AS sellerAverageRating, " +
        "(SELECT COUNT(*) FROM reviews WHERE reviewedUserId = i.userId) AS sellerReviewCount " +
        "FROM items i " +
        "JOIN users u ON i.userId = u.userId " +
        "JOIN categories c ON i.categoryId = c.categoryId " +
        "JOIN conditions con ON i.conditionId = con.conditionId " +
        "LEFT JOIN categories dc ON i.desiredCategoryId = dc.categoryId " +
        "WHERE i.userId = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> 
        {
            ItemEntity entity = mapRowToEntity(rs);
            entity.setSellerAverageRating(rs.getDouble("sellerAverageRating"));
            entity.setSellerReviewCount(rs.getInt("sellerReviewCount"));
            return entity;
        }, userId);
    }

    /**
     * Create/Save an item 
     */
    @Override
    public ItemEntity save(ItemEntity item) 
    {

        String sql = "INSERT INTO items (itemName, description, userId, categoryId, conditionId, status, location, latitude, longitude, desiredReturn, desiredCategoryId, isOnline) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Capture auto primary key 
        org.springframework.jdbc.support.GeneratedKeyHolder keyHolder = new org.springframework.jdbc.support.GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
        java.sql.PreparedStatement ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, item.getItemName());
        ps.setString(2, item.getDescription());
        ps.setInt(3, item.getUserId());
        ps.setInt(4, item.getCategoryId());
        ps.setInt(5, item.getConditionId());
        ps.setString(6, "available");
        ps.setString(7, item.getLocation());
        ps.setObject(8, item.getLatitude());
        ps.setObject(9, item.getLongitude());
        ps.setString(10, item.getDesiredReturn());
        ps.setInt(11, item.getDesiredCategoryId());
        ps.setBoolean(12, item.getIsOnline());

        return ps;

        }, keyHolder);

        item.setItemId(keyHolder.getKey().intValue());

        return item;
    }

    /**
     * Upadte a Item 
     */
    @Override
    public void update(ItemEntity item) 
    {

        String sql = "UPDATE items SET itemName = ?, description = ?, categoryId = ?, conditionId = ?, " +
        "status = ?, location = ?, latitude = ?, longitude = ?, desiredReturn = ?, " +
        "desiredCategoryId = ?, isOnline = ? WHERE itemId = ?";

        jdbcTemplate.update(sql,
        item.getItemName(), item.getDescription(), item.getCategoryId(), item.getConditionId(),
        item.getStatus(), item.getLocation(), item.getLatitude(), item.getLongitude(),
        item.getDesiredReturn(), item.getDesiredCategoryId(), item.getIsOnline(), item.getItemId()
        );
    }

    /**
     * Update the status of a Item 
     * available, pending, traded
     */
    @Override
    public void updateStatus(int itemId, String status) 
    {
        String sql = "UPDATE items SET status = ? WHERE itemId = ?";
        jdbcTemplate.update(sql, status, itemId);
    }

    /**
     * Delete a Item 
     */
    @Override
    public void delete(int itemId)
    {
        // Delete depedent tables before 
        jdbcTemplate.update("DELETE FROM messages WHERE itemId = ?", itemId);
        jdbcTemplate.update("DELETE FROM saved_items WHERE itemId = ?", itemId);
        jdbcTemplate.update("DELETE FROM item_images WHERE itemId = ?", itemId);

        // Then delete the item 
        jdbcTemplate.update("DELETE FROM items WHERE itemId = ?", itemId);

    }

    /**
     * Search for items 
     * Search based on 
     * description, desiredReturn description, categories, and status
     * @param query
     * @return
     */
    public List<ItemEntity> searchItems(String query) 
    {
        String sql = "SELECT i.*, u.name, u.lastName, c.categoryName, con.conditionName, " +
                    "dc.categoryName AS desiredCategoryName, " +
                    "(SELECT img.imageUrl FROM item_images img WHERE img.itemId = i.itemId LIMIT 1) AS imageUrl " +
                    "FROM items i " +
                    "JOIN users u ON i.userId = u.userId " +
                    "JOIN categories c ON i.categoryId = c.categoryId " +
                    "JOIN conditions con ON i.conditionId = con.conditionId " +
                    "LEFT JOIN categories dc ON i.desiredCategoryId = dc.categoryId " +
                    "WHERE (i.itemName LIKE ? " +
                    " OR i.description LIKE ? " +
                    " OR i.desiredReturn LIKE ? " + 
                    " OR c.categoryName LIKE ? " + 
                    " OR dc.categoryName LIKE ?) " +
                    "AND i.status = 'available'";

        String searchPattern = "%" + query + "%";

        return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> mapRowToEntity(rs),
            searchPattern,
            searchPattern,
            searchPattern,
            searchPattern,
            searchPattern
        );
    }

    /**
     * Filter search
     * Search based on distance, 
     * category, condition, location, distance
     * @param query
     * @param categoryId
     * @param conditionId
     * @param desiredCategoryId
     * @param isOnline
     * @param userLat
     * @param userLng
     * @param maxDistance
     * @return
     */
    public List<ItemEntity> findWithFilters(String query, Integer categoryId, List<Integer> conditionId,
                                            Integer desiredCategoryId, Boolean isOnline,
                                            Double userLat, Double userLng, Integer maxDistance) 
    {
        StringBuilder sql = new StringBuilder(
            "SELECT i.*, u.name, u.lastName, c.categoryName, con.conditionName, dc.categoryName AS desiredCategoryName, " +
            "(SELECT img.imageUrl FROM item_images img WHERE img.itemId = i.itemId LIMIT 1) AS imageUrl ");

        List<Object> params = new ArrayList<>();

        // Calculate the disance if there is location
        if (maxDistance != null && userLat != null && userLng != null) 
        {
            sql.append(", (3959 * acos(cos(radians(?)) * cos(radians(i.latitude)) * cos(radians(i.longitude) - radians(?)) + sin(radians(?)) * sin(radians(i.latitude)))) AS distance ");

            params.add(userLat); 
            params.add(userLng); 
            params.add(userLat);
        } 
        else 
        {
            sql.append(", 0 AS distance ");
        }

        // Table joins
        sql.append("FROM items i " +

        "JOIN users u ON i.userId = u.userId " +

        "JOIN categories c ON i.categoryId = c.categoryId " +

        "JOIN conditions con ON i.conditionId = con.conditionId " +

        "LEFT JOIN categories dc ON i.desiredCategoryId = dc.categoryId " +

        "WHERE i.status = 'available' ");


        // Filter for a Category
        if (categoryId != null) 
        {
            sql.append("AND i.categoryId = ? ");
            params.add(categoryId);
        }

        // Filter for a Condition
        if (conditionId != null) { 
            sql.append("AND i.conditionId = ? ");
            params.add(conditionId);
        }

        // Filter for a Category
        if (desiredCategoryId != null) 
        {
            sql.append("AND i.desiredCategoryId = ? ");
            params.add(desiredCategoryId);
        }

        // Filter for online items or location
        if (Boolean.TRUE.equals(isOnline)) 
        {
            sql.append("AND i.isOnline = true ");
        } 
        else if (maxDistance != null && userLat != null && userLng != null) 
        {
            sql.append("AND i.isOnline = false AND i.latitude IS NOT NULL AND i.longitude IS NOT NULL ");

            sql.append("HAVING distance <= ? ");

            params.add(maxDistance);
        }

        sql.append(" ORDER BY i.dateCreated DESC");

        return jdbcTemplate.query(sql.toString(), (rs, rowNum) -> mapRowToEntity(rs), params.toArray());

    }

    /**
     * Save the item image
     * @param itemId
     * @param imageUrl
     */
    public void saveImage(int itemId, String imageUrl) 
    {
        String sql = "INSERT INTO item_images (itemId, imageUrl) VALUES (?, ?)";

        jdbcTemplate.update(sql, itemId, imageUrl);
    }

    /**
     * Update the item image 
     * @param itemId
     * @param imageUrl
     */
    public void updateImage(int itemId, String imageUrl) 
    {
        String checkSql = "SELECT COUNT(*) FROM item_images WHERE itemId = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, itemId);

        if (count != null && count > 0) 
        {
            jdbcTemplate.update("DELETE FROM item_images WHERE itemId = ? AND imageUrl <> ?", itemId, imageUrl);
            // Update image
            String updateSql = "UPDATE item_images SET imageUrl = ? WHERE itemId = ?";
            jdbcTemplate.update(updateSql, imageUrl, itemId);
        } 
        else 
        {
            // Save image 
            saveImage(itemId, imageUrl);
        }
        
    }

    /**
     * Save an item to a list 
     * User specific
     */
    @Override
    public void saveItemToUserList(int userId, int itemId) 
    {
        String sql = "INSERT IGNORE INTO saved_items (userId, itemId) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, itemId);
    }

    /**
     * Unsave a item form the list 
     * User Specific
     */
    @Override
    public void removeItemFromUserList(int userId, int itemId) 
    {
        String sql = "DELETE FROM saved_items WHERE userId = ? AND itemId = ?";
        jdbcTemplate.update(sql, userId, itemId);
    }

    /**
     * Check if the item is saved or not
     * to shows if saved or not saved 
     */
    @Override
    public boolean checkIfSaved(int userId, int itemId) 
    {
        String sql = "SELECT COUNT(*) FROM saved_items WHERE userId = ? AND itemId = ?";
        Integer count = jdbcTemplate.queryForObject(sql,Integer.class, userId, itemId);
        return count != null && count > 0;
    }

    /**
     * Get all the saved items 
     * as cards so more info
     * User Specific 
     */
    @Override
    public List<ItemEntity> findSavedByUserId(int userId) 
    {
        String sql = "SELECT i.*, u.name, u.lastName, c.categoryName, con.conditionName, " +
        "dc.categoryName AS desiredCategoryName, " + 
        "(SELECT img.imageUrl FROM item_images img WHERE img.itemId = i.itemId LIMIT 1) AS imageUrl " +
        "FROM items i " +
        "JOIN saved_items s ON i.itemId = s.itemId " + 
        "JOIN users u ON i.userId = u.userId " +
        "JOIN categories c ON i.categoryId = c.categoryId " +
        "JOIN conditions con ON i.conditionId = con.conditionId " +
        "LEFT JOIN categories dc ON i.desiredCategoryId = dc.categoryId " + 
        "WHERE s.userId = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToEntity(rs), userId);
    }

    /**
     * Helper Method 
     * Map all items information
     * @param rs
     * @return
     * @throws java.sql.SQLException
     */
    private ItemEntity mapRowToEntity(java.sql.ResultSet rs) throws java.sql.SQLException 
    {

        ItemEntity entity = new ItemEntity();
        entity.setItemId(rs.getInt("itemId"));
        entity.setItemName(rs.getString("itemName"));
        entity.setDescription(rs.getString("description"));
        entity.setStatus(rs.getString("status"));
        entity.setLocation(rs.getString("location"));
        entity.setLatitude(rs.getDouble("latitude"));
        entity.setLongitude(rs.getDouble("longitude"));
        entity.setIsOnline(rs.getBoolean("isOnline"));
        entity.setDesiredReturn(rs.getString("desiredReturn"));
        entity.setUserId(rs.getInt("userId"));
        entity.setCategoryId(rs.getInt("categoryId"));
        entity.setConditionId(rs.getInt("conditionId"));
        entity.setDesiredCategoryId(rs.getInt("desiredCategoryId"));
        entity.setUserFullName(rs.getString("name") + " " + rs.getString("lastName"));
        entity.setCategoryName(rs.getString("categoryName"));
        entity.setConditionName(rs.getString("conditionName"));
        entity.setDesiredCategoryName(rs.getString("desiredCategoryName"));
        entity.setImageUrl(rs.getString("imageUrl"));
        java.sql.Timestamp ts = rs.getTimestamp("dateCreated");

        if (ts != null) 
        {
            entity.setDateCreated(ts.toLocalDateTime());
        }
        return entity;
    }
}