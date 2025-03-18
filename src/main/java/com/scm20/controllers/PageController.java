package com.scm20.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class PageController {
    
 @RequestMapping("/home")
   public String home(Model model){
    System.out.println("Home page Handler");
    // sending data to view
    model.addAttribute("name","Substring Technologies" );
    model.addAttribute("youtubeChannel", "Saketh Rama Ranga");
    model.addAttribute("githubRepo", "https://github.com/");
     return "home";
   }

   // about route
   @RequestMapping("/about")
   public String aboutPage(Model model){
    model.addAttribute("isLogin", false);
    System.out.println("About page Loading...");
    return "about";
   }

   // services
   @RequestMapping("/services")
   public String servicePage(){
    System.out.println("Service page is loading..");
    return "services";
   }

   // contacts
   @RequestMapping("/contact")
   public String contactPage(){
    System.out.println("Contact page is loading..");
    return "contact";
   }

   // login
   @RequestMapping("/login")
   public String loginPage(){
    System.out.println("Login page is loading..");
    return "login";
   }

   // Sign up 
   @RequestMapping("/register")
   public String signUpPage(){
    System.out.println("Signup page is loading..");
    return "register";
   }
}
