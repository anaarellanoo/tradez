package com.anaarellano.tradez.data;

import com.anaarellano.tradez.models.UserEntity;

/**
 * UserRepository Interface 
 */
public interface UserRepositoryInterface 
{
    UserEntity findByUsername(String username);
    UserEntity save(UserEntity user);
    void deleteById(int userId);
    UserEntity update(UserEntity userEntity);
    UserEntity findByEmail(String email);
    UserEntity findById(int id);
    
}
