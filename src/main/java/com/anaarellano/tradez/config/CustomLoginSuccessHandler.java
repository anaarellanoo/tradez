package com.anaarellano.tradez.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.anaarellano.tradez.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    public CustomLoginSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        String username = authentication.getName();

        // get location from login form
        Double lat = parseDouble(request.getParameter("latitude"));
        Double lng = parseDouble(request.getParameter("longitude"));
        String city = request.getParameter("location");

        // update user location
        userService.updateCoordinates(username, lat, lng, city);

        // redirect after login
        response.sendRedirect("/tradez/home");
    }

    private Double parseDouble(String value) {
        try {
            return value != null ? Double.parseDouble(value) : null;
        } catch (Exception e) {
            return null;
        }
    }
}