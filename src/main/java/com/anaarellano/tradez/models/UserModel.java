package com.anaarellano.tradez.models;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * User Model 
 * -> Service/Controller
 */
public class UserModel 
{

    private int id;

    @NotBlank(message = "First name is required")
    private String name;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Username is required")
    private String username;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "Password must contain: 1 uppercase letter, 1 number, 1 special character"
    )
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;
    
    private String location = "Location not found"; // Default value
    private Double latitude;
    private Double longitude;

    private LocalDateTime createdAt;  

    private String profileImageURL;

    /**
     * Constructor
     */
    public UserModel() 
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
    public UserModel(int id, String name, String lastName, String username, String password, String email, String location, Double latitude, Double longitude, LocalDateTime createdAt, String profileImageURL) {
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