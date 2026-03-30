package com.anaarellano.tradez.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.anaarellano.tradez.data.UserRepository;
import com.anaarellano.tradez.models.UserEntity;

/**
 * CustomerUserDetailsService
 * Implements UserDetailsService 
 * Used to load data for authentication 
 */
@Service
public class CustomUserDetailsService implements UserDetailsService
{
    private final UserRepository userRepository;

    /**
     * Constructor
     * @param userRepository
     */
    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) 
    {
        this.userRepository = userRepository;
    }

    /**
     * Load user by the username 
     * when they log in
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
    {
        UserEntity user = userRepository.findByUsername(username);

        if (user == null) 
        {
            throw new UsernameNotFoundException("User not found");
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) 
                .roles("USER") 
                .build();
    }
    
}
