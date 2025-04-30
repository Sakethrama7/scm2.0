package com.scm20.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm20.entities.Providers;
import com.scm20.entities.User;
import com.scm20.helpers.AppConstants;
import com.scm20.repositories.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

    Logger logger=LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Autowired
    private UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        
                logger.info("OAuthAuthenticationSuccessHandler");
               
                // Identify th provider
                var OAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
                String authorizedClientRegistrationId=OAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
                logger.info(authorizedClientRegistrationId);

                var oauthuser=(DefaultOAuth2User)authentication.getPrincipal();
                // saving data in the database

                // logger.info(oauthuser.getName());

                // oauthuser.getAttributes().forEach((key,value) -> {
                //     logger.info("{} => {}",key,value);
                // });

                // logger.info(oauthuser.getAuthorities().toString());

                User user = new User();
                user.setUserId(UUID.randomUUID().toString());
                user.setRoleList(List.of(AppConstants.ROLE_USER));
                user.setEmailverified(true);
                user.setEnabled(true);
                user.setPassword("dummy");
                
                // Create user and save in the data base
                if(authorizedClientRegistrationId.equalsIgnoreCase("google")){
                    // google 
                    // google attributes
                    user.setEmail(oauthuser.getAttribute("email").toString());
                    user.setProfilePic(oauthuser.getAttribute("picture").toString());
                    user.setName(oauthuser.getAttribute("name").toString());
                    user.setProviderUserId(oauthuser.getName());
                    user.setProvider(Providers.GOOGLE);
                    user.setAbout("This account is created using google.");
                }else{
                    logger.info("OAuthAuthenicationSuccessHandler: Unknown provider");
                }

                // save the user 
                User user2 = userRepo.findByEmail(user.getEmail()).orElse(null);
                if (user2 == null) {
                    userRepo.save(user);
                    System.out.println("user saved:" + user.getEmail());
                }

                new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }

}
