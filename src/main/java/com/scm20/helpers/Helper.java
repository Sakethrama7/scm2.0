package com.scm20.helpers;



import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class Helper {
   public static String getEmailOfLoggedInUser(Authentication authentication){

         // How to get the user if he logged in with Email and password
         if(authentication instanceof OAuth2AuthenticationToken){
           // If he logged with google
           var aOauth2AuthenticationToken=(OAuth2AuthenticationToken) authentication;
           var clientId=aOauth2AuthenticationToken.getAuthorizedClientRegistrationId();

           var oauth2user=(OAuth2User) authentication.getPrincipal();
           String username="";

           if(clientId.equalsIgnoreCase("google")){
              System.out.println("Getting email from google");
              username=oauth2user.getAttribute("email").toString();
           }

            return username;
         }else{
            System.out.println("Getting data from Local Storage");
            return authentication.getName();
         }
     
   }
}
