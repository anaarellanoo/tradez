package com.anaarellano.tradez.service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.anaarellano.tradez.data.UserRepository;
import com.anaarellano.tradez.models.Converter;
import com.anaarellano.tradez.models.UserEntity;
import com.anaarellano.tradez.models.UserModel;

/**
 * User Service 
 * Handles all the user actions 
 * register, login, updating, 
 * storing profile images, delete 
 */
@Service
public class UserService 
{

    private final UserRepository userRepository;
    private final Converter converter;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor
     * @param userRepository
     * @param userConverter
     * @param passwordEncoder
     */
    public UserService(UserRepository userRepository, Converter converter, PasswordEncoder passwordEncoder) 
    {
        this.userRepository = userRepository;
        this.converter = converter;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Create user account
     * @param userModel
     */
    public void register(UserModel userModel) 
    {
        if (userModel.getLatitude() == null || userModel.getLongitude() == null) {
            throw new RuntimeException("Location not received. Please allow location access.");
        }

        // Check if username exists
        if (userRepository.findByUsername(userModel.getUsername()) != null) 
        {
            throw new RuntimeException("Username is already taken!");
        }
        
        // Check if email in use
        if (userRepository.findByEmail(userModel.getEmail()) != null) 
        {
            throw new RuntimeException("Email is already registered!");
        }

        UserEntity userEntity = converter.toEntity(userModel);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userRepository.save(userEntity);
        System.out.println("LAT: " + userModel.getLatitude());
        System.out.println("LNG: " + userModel.getLongitude());
        System.out.println("CITY: " + userModel.getLocation());
    }

    /**
     * Find by Username
     * @param username
     * @return
     */
    public UserModel findByUsername(String username) 
    {
        UserEntity entity = userRepository.findByUsername(username);
        return entity != null ? converter.toModel(entity) : null;
    }

    /**
     * Find user by Id
     */
    public UserModel findById(int id) 
    {
        UserEntity entity = userRepository.findById(id); 
        return entity != null ? converter.toModel(entity) : null;
    }

    /**
     * Update the users profile
     * @param username
     * @param userModel
     * @param profileImage
     * @return
     * @throws IOException
     */
    public UserModel updateProfile(String username, UserModel userModel, MultipartFile profileImage) throws IOException 
    {
        UserEntity existingUser = userRepository.findByUsername(username);
        
        if (existingUser != null) 
        {
            existingUser.setName(userModel.getName());
            existingUser.setLastName(userModel.getLastName());
            existingUser.setEmail(userModel.getEmail());
            existingUser.setLocation(userModel.getLocation());
            existingUser.setLatitude(userModel.getLatitude());
            existingUser.setLongitude(userModel.getLongitude());

            if (profileImage != null && !profileImage.isEmpty()) 
            {
                String filename = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
                Path uploadPath = Paths.get("uploads/profile_images");
                
                profileImage.transferTo(uploadPath.resolve(filename));
                existingUser.setProfileImageURL("/uploads/profile_images/" + filename);
            }

            userRepository.update(existingUser);
            return converter.toModel(existingUser);
        }

        return null;
    }

    /**
     * Delete the user 
     * @param userId
     */
    public void deleteUser(int userId) 
    {
        userRepository.deleteById(userId);
    }

    public void updateCoordinates(String username, Double lat, Double lng, String city) {
        UserEntity user = userRepository.findByUsername(username);

        if (user != null) {
            if (lat != null) user.setLatitude(lat);
            if (lng != null) user.setLongitude(lng);
            if (city != null) user.setLocation(city);

            userRepository.update(user);

            System.out.println("UPDATED LOCATION: " + lat + ", " + lng + ", " + city);
        }
    }

}