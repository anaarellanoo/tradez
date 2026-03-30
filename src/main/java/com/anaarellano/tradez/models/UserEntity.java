package com.anaarellano.tradez.models;

import java.time.LocalDateTime;

/**
 * User Entity 
 * -> Data/Repository
 */
public class UserEntity 
{
    private int id;

    private String name;

    private String lastName;

    private String username;

    private String password;

    private String email;

    private String location;

    private Double latitude;

    private Double longitude;

    private LocalDateTime createdAt;

    private String profileImageURL;

    /**
     * Constructor
     * */
    public UserEntity() 
    {
        
    }

    /**
     * Parametized Constructor 
     * @param id
     * @param name
     * @param lastName
     * @param username
     * @param password
     * @param email
     * @param location
     * @param latitude
     * @param longitude
     * @param createdAt
     * @param profileImageURL
     */
    public UserEntity(int id, String name, String lastName, String username, String password, String email, String location, Double latitude, Double longitude, LocalDateTime createdAt, String profileImageURL) 
    {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = createdAt;
        this.profileImageURL = profileImageURL;
    }
    
    /********************* GETTERS AND SETTER *********************/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}