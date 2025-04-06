package com.scm20.controllers;

import com.scm20.Application;
import com.scm20.entities.User;
import com.scm20.forms.UserForm;
import com.scm20.helpers.Message;
import com.scm20.helpers.MessageType;
import com.scm20.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;





@Controller
public class PageController {

  @Autowired
  private UserService userService;

  
  @RequestMapping("/")
  public String index(){

    return "redirect:/home";
  }
    
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
   @GetMapping("/login")
   public String loginPage(){
    System.out.println("Login page is loading..");
    return "login";
   }

   // Sign up 
   @RequestMapping("/register")
   public String signUpPage(Model model){
    
    UserForm userform=new UserForm();
    // you can also sent the default data also
    model.addAttribute("userform",userform);
    return "register";
   }

   // processing register
   
   
   @RequestMapping(value="/do-register",method=RequestMethod.POST)
   public String processRegister(@Valid @ModelAttribute("userform") UserForm userform,BindingResult rBindingResult, HttpSession session){ // because of this whatever the data comes from the form is stored in this userform object
    System.out.println("processing registration");
    //fetch the form data
     System.out.println(userform);

    // validate form data
       if(rBindingResult.hasErrors()){
        return "register";
       }
    // save to database
    // userservice 
    // userform -> User
     User user=new User();
     user.setName(userform.getName());
     user.setEmail(userform.getEmail());
     user.setPassword(userform.getPassword());
     user.setAbout(userform.getAbout());
     user.setPhoneNumber(userform.getPhoneNumber());
     user.setProfilePic("https://static.independent.co.uk/s3fs-public/thumbnails/image/2017/06/10/19/ben-stokes.jpg");

      User savedUser = userService.saveUser(user);
      System.out.println("User Saved");


    // message = "Registration Successful"
    // add the meassage
    Message message=Message.builder().content("Registration Successful").type(MessageType.green).build();
    session.setAttribute("message", message);

    // redirected to login page 
     return "redirect:/register";
   }

   

}
