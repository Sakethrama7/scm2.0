package com.scm20.controllers;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm20.entities.Contact;
import com.scm20.entities.User;
import com.scm20.forms.ContactForm;
import com.scm20.forms.ContactSearchForm;
import com.scm20.helpers.AppConstants;
import com.scm20.helpers.Helper;
import com.scm20.helpers.Message;
import com.scm20.helpers.MessageType;
import com.scm20.services.ContactService;
import com.scm20.services.ImageService;
import com.scm20.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

   private Logger logger=LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    // add contact page handler
    @RequestMapping("/add")
    public String addContact(Model model){

        ContactForm contactForm=new ContactForm();
        
        model.addAttribute("contactForm",contactForm);

        return "user/add_contact";
    }

    @RequestMapping(value="/add",method=RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm,BindingResult result,Authentication authentication,HttpSession session){
       
        // processing the form data

        // validate the form
        if(result.hasErrors()){
            session.setAttribute("message",com.scm20.helpers.Message.builder()
            .content("Please correct the following errors")
            .type(MessageType.red)
            .build()
            );
            return "user/add_contact";
        }

        // using Authentcation you can the name of the user
        String Username=Helper.getEmailOfLoggedInUser(authentication);
        User user=userService.getUserByEmail(Username);

        // process the contact picture
        // Image uploading code


        // convert contactForm -> contact
        Contact contact=new Contact();
        contact.setName(contactForm.getName());
        contact.setFavourite(contactForm.isFavorite());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());


        if(contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()){
            String filename=UUID.randomUUID().toString();
            String fileURL=imageService.uploadImage(contactForm.getContactImage(),filename);
            contact.setPicture(fileURL);
            contact.setCloudinaryImagePublicId(filename);
        }


        contactService.save(contact);

        // 

        // set the message to display on the view
        session.setAttribute("message",com.scm20.helpers.Message.builder()
        .content("You have Successfully added a new contact")
        .type(MessageType.green)
        .build()
        );

        System.out.println(contactForm);
        return "redirect:/user/contacts/add";
    }

    // view contacts page handler

    @RequestMapping
    public String viewContacts(@RequestParam(value="page",defaultValue="0") int page,
    @RequestParam(value="size",defaultValue=AppConstants.PAGE_SIZE+"") int size,
    @RequestParam(value="sortBy",defaultValue="name") String sortBy,
    @RequestParam(value="direction",defaultValue="asc") String direction,
    Model model,Authentication authentication){

        // load all the user contacts
        String username=Helper.getEmailOfLoggedInUser(authentication);

        User user= userService.getUserByEmail(username);

        Page<Contact> PageContacts=contactService.getByUser(user,page,size,sortBy,direction);

        model.addAttribute("PageContacts",PageContacts);
        model.addAttribute("pageSize",AppConstants.PAGE_SIZE);

        model.addAttribute("contactSearchForm",new ContactSearchForm());

        return "user/contacts";
    }

    // search handler

    @RequestMapping("/search")
    public String searchHandler(
        @ModelAttribute ContactSearchForm contactSearchForm,
        @RequestParam(value="size", defaultValue=AppConstants.PAGE_SIZE + "") int size,
        @RequestParam(value="page",defaultValue="0" ) int page,
        @RequestParam(value="sortBy",defaultValue="name") String sortBy,
        @RequestParam(value="direction",defaultValue="asc") String direction,
        Model model,
        Authentication authentication
    ){
        logger.info("field {} keyword {}",contactSearchForm.getField(),contactSearchForm.getValue());

        var user=userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));

        Page<Contact> PageContacts=null;
        if(contactSearchForm.getField().equalsIgnoreCase("name")){
           PageContacts= contactService.searchByName(contactSearchForm.getValue(), size, page, sortBy, direction,user);
        }else if(contactSearchForm.getField().equalsIgnoreCase("email")){
            PageContacts= contactService.searchByEmail(contactSearchForm.getValue(), size, page, sortBy, direction,user);
        }else if(contactSearchForm.getField().equalsIgnoreCase("phone")){
            PageContacts= contactService.searchByPhoneNumber(contactSearchForm.getValue(), size, page, sortBy, direction,user);
        }

        model.addAttribute("contactSearchForm",contactSearchForm);

        model.addAttribute("PageContacts",PageContacts);
        model.addAttribute("pageSize",AppConstants.PAGE_SIZE);
        

        return "user/search";
    }

 
    // delete contact handler

    @RequestMapping("/delete/{contactId}")
    public String deleteContact(@PathVariable("contactId") String contactId,
     HttpSession session
    ){

        contactService.delete(contactId);
        logger.info("contactId {} deleted", contactId);

        session.setAttribute("message",
        Message.builder()
        .content("Contact is Deleted Successfully")
        .type(MessageType.green)
        .build()
        );

        return "redirect:/user/contacts";
    }


    // update contact form vie

    @GetMapping("/view/{contactId}")
    public String updateContactFormView(
        @PathVariable("contactId") String contactId,
        Model model
    ){

        var contact=contactService.getById(contactId);
        ContactForm contactForm = new ContactForm();
        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setFavorite(contact.isFavourite());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setLinkedInLink(contact.getLinkedInLink());
        contactForm.setPicture(contact.getPicture());
        
        model.addAttribute("contactForm",contactForm);
        model.addAttribute("contactId",contactId);
        return "user/update_contact_view";
    }


    @RequestMapping(value="/update/{contactId}",method=RequestMethod.POST)
    public String updateContact(@PathVariable("contactId") String contactId,@Valid @ModelAttribute ContactForm contactForm,BindingResult bindingResult,Model model){

        // update the contact

        if(bindingResult.hasErrors()){
            return "user/update_contact_view";
        }


        var con=contactService.getById(contactId);

        con.setId(contactId);
        con.setName(contactForm.getName());
        con.setEmail(contactForm.getEmail());
        con.setPhoneNumber(contactForm.getPhoneNumber());
        con.setAddress(contactForm.getAddress());
        con.setDescription(contactForm.getDescription());
        con.setFavourite(contactForm.isFavorite());
        con.setWebsiteLink(contactForm.getWebsiteLink());
        con.setLinkedInLink(contactForm.getLinkedInLink());
        

        // image process
        if(contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty() ){
            String fileName=UUID.randomUUID().toString();
            String imageUrl=imageService.uploadImage(contactForm.getContactImage(), fileName);
            con.setCloudinaryImagePublicId(fileName);
            con.setPicture(imageUrl);
            contactForm.setPicture(imageUrl);
        }else{
            logger.info("file is empty");
        }


        var updatedContact=contactService.upadate(con);
        logger.info("updated contact {}",updatedContact);
        model.addAttribute("message",Message.builder()
        .content("Contact Updated")
        .type(MessageType.green)
        .build());

        return "redirect:/user/contacts/view/" + contactId;
    }
}
