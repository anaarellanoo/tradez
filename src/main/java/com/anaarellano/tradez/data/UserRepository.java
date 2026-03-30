package com.anaarellano.tradez.data;

import java.time.LocalDateTime;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.anaarellano.tradez.models.UserEntity;

/**
 * User Repository 
 * Implements UserRepository Interface 
 * Handles the crud for a user 
 * */ 
@Repository
public class UserRepository implements UserRepositoryInterface 
{

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor 
     * @param jdbcTemplate
     */
    public UserRepository(JdbcTemplate jdbcTemplate) 
    {
        this.jdbcTemplate = jdbcTemplate;
    }


    /**
     * Find the user by Id
     */
    @Override
    public UserEntity findById(int userId) 
    {
        String sql = "SELECT * FROM users WHERE userId = ?";
        try 
        {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> 
                new UserEntity(
                    rs.getInt("userId"),
                    rs.getString("name"),
                    rs.getString("lastName"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("location"),
                    rs.getDouble("latitude"), 
                    rs.getDouble("longitude"), 
                    rs.getTimestamp("createdAt") != null ? rs.getTimestamp("createdAt").toLocalDateTime() : null,
                    rs.getString("profileImageURL")
                ), userId);
        } 
        catch (org.springframework.dao.EmptyResultDataAccessException e) 
        {
            return null;
        }
    }

    /**
     * Find user by the username 
     * unique
     */
    @Override
    public UserEntity findByUsername(String username) 
    {
        String sql = "SELECT userId, name, lastName, username, password, email, location, latitude, longitude, createdAt, profileImageURL FROM users WHERE username = ?";
        try 
        {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> 
                new UserEntity(
                    rs.getInt("userId"),
                    rs.getString("name"),
                    rs.getString("lastName"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("location"),
                    rs.getDouble("latitude"), 
                    rs.getDouble("longitude"), 
                    rs.getTimestamp("createdAt") != null ? rs.getTimestamp("createdAt").toLocalDateTime() : null,
                    rs.getString("profileImageURL")
                ), username);
        } 
        catch (EmptyResultDataAccessException e) 
        {
            return null;
        }
    }

    /**
     * Save a new user 
     */
    @Override
    public UserEntity save(UserEntity userEntity) 
    {
        String sql = "INSERT INTO users (name, lastName, username, password, email, location, latitude, longitude, createdAt, profileImageURL) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        LocalDateTime now = LocalDateTime.now();
        jdbcTemplate.update(sql,
            userEntity.getName(),
            userEntity.getLastName(),
            userEntity.getUsername(),
            userEntity.getPassword(),
            userEntity.getEmail(),
            userEntity.getLocation(),
            userEntity.getLatitude(), 
            userEntity.getLongitude(),
            now,
            userEntity.getProfileImageURL()
        );

        userEntity.setCreatedAt(now);
        return userEntity;
    }

    /**
     * Update an existing user
     */
    @Override
    public UserEntity update(UserEntity userEntity) 
    {
        String sql = "UPDATE users SET name = ?, lastName = ?, email = ?, location = ?, latitude = ?, longitude = ?, profileImageURL = ? WHERE userId = ?";
        jdbcTemplate.update(sql,
            userEntity.getName(),
            userEntity.getLastName(),
            userEntity.getEmail(),
            userEntity.getLocation(),
            userEntity.getLatitude(), 
            userEntity.getLongitude(), 
            userEntity.getProfileImageURL(),
            userEntity.getId()
        );
        return userEntity;
    }

    /**
     * Find the user by email 
     */
    @Override
    public UserEntity findByEmail(String email) 
    {
        String sql = "SELECT userId, name, lastName, username, password, email, location, latitude, longitude, createdAt, profileImageURL FROM users WHERE email = ?";
        try 
        {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> 
                new UserEntity(
                    rs.getInt("userId"),
                    rs.getString("name"),
                    rs.getString("lastName"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("location"),
                    rs.getDouble("latitude"),
                    rs.getDouble("longitude"),
                    rs.getTimestamp("createdAt") != null ? rs.getTimestamp("createdAt").toLocalDateTime() : null,
                    rs.getString("profileImageURL")
                ), email);
        } 
        catch (EmptyResultDataAccessException e) 
        {
            return null;
        }
    }

    /**
     * Delete the user by Id
     */
    @Override
    public void deleteById(int userId) 
    {
        String sql = "DELETE FROM users WHERE userId = ?";
        jdbcTemplate.update(sql, userId);
    }
}