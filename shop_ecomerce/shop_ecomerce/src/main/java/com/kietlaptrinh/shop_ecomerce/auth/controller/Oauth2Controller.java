package com.kietlaptrinh.shop_ecomerce.auth.controller;

import com.kietlaptrinh.shop_ecomerce.auth.config.JWTTokenHelper;
import com.kietlaptrinh.shop_ecomerce.auth.entities.User;
import com.kietlaptrinh.shop_ecomerce.auth.services.OAuth2Service;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/oauth2")
public class Oauth2Controller {
    @Autowired
    OAuth2Service oAuth2Service;
    @Autowired
    private JWTTokenHelper jwtTokenHelper;
    @GetMapping("/success")
    public void callbackOauth2(@AuthenticationPrincipal OAuth2User oAuth2User, HttpServletResponse response) throws IOException {
        String userName = oAuth2User.getAttribute("email");
        User user = oAuth2Service.getUser(userName);
        if(null == user){
            user = oAuth2Service.createUser(oAuth2User,"google");
        }
        String token = jwtTokenHelper.generateToken(user.getUsername());
        response.sendRedirect("http://localhost:3000/oauth2/callback?token="+token);
    }
}
