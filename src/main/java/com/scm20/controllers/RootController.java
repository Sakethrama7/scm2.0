
package com.scm20.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm20.entities.User;
import com.scm20.helpers.Helper;
import com.scm20.services.UserService;

@ControllerAdvice  // when ever you write this annotation then all the methods present in this will be executed for every request in this project.
public class RootController {

    private Logger logger=LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @ModelAttribute  // When ever you write this attribute it means this method is executed before the execution of an handler methods.
    public void addLoggedInUserInformation(Model model,Authentication authentication){
        if(authentication == null){
            return ;
        }

        System.out.println("Adding logged in user information to the model");

        String username=Helper.getEmailOfLoggedInUser(authentication);
        logger.info("User logged in : {}" ,username);

        // To get user from the data base ( who have logged in)
        User user=userService.getUserByEmail(username);

            System.out.println(user);

            System.out.println(user.getName());
            System.out.println(user.getEmail());
            model.addAttribute("loggedInUser", user);
        
    }
}
