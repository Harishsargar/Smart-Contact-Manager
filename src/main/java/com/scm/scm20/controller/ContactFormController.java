package com.scm.scm20.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.scm20.entity.Contact;
import com.scm.scm20.entity.User;
import com.scm.scm20.forms.AddContactForm;
import com.scm.scm20.forms.SearchContactForm;
import com.scm.scm20.helper.AppConstant;
import com.scm.scm20.helper.Helpers;
import com.scm.scm20.helper.Message;
import com.scm.scm20.helper.MessageType;
import com.scm.scm20.services.ContactService;
import com.scm.scm20.services.ImageService;
import com.scm.scm20.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contacts")
public class ContactFormController {

    Logger logger = LoggerFactory.getLogger(ContactFormController.class);   

    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private ImageService imageService;
     

    // controller to view the add contact form
    @RequestMapping("/add")
    public String addContact(Model model){
        AddContactForm addContactForm = new AddContactForm();
        model.addAttribute("addContactForm", addContactForm); //passing empty object to Contact form  

        return "/user/addContact";
    }
    

    // controller to save the contact details
    @RequestMapping(value = "/add-contact", method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute AddContactForm addContactForm, BindingResult bindingResult, HttpSession session, Authentication authentication){

        // Validating the form
        if(bindingResult.hasErrors()){
            session.setAttribute("message", Message.builder().content("Please fill the form correctly").type(MessageType.red).build());
            return "/user/addContact";
        }

        logger.info("profile photo name :"+ addContactForm.getProFilePicture().getOriginalFilename());


        // fetching contact details from the form
        System.out.println(addContactForm);

        String userEmail = Helpers.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(userEmail);

        String filename = UUID.randomUUID().toString();
        String fileUrl = imageService.uploadImage(addContactForm.getProFilePicture(), filename);    

        logger.info(userEmail);

        // putting the data into contact object
        Contact contact = new Contact();
        contact.setName(addContactForm.getName());
        contact.setEmail(addContactForm.getEmail());
        contact.setPhoneNumber(addContactForm.getPhoneNumber()); 
        contact.setAddress(addContactForm.getAddress());
        // contact.setPicture(addContactForm.getProFilePicture());
        contact.setUser(user);
        contact.setDescription(addContactForm.getAbout());
        contact.setFavorite(addContactForm.isFavorite());
        contact.setWebsiteLink(addContactForm.getWebsiteLink());
        contact.setLinkedInLink(addContactForm.getLinkedInLink());
        contact.setPicture(fileUrl);
        contact.setCloudinaryImageId(filename);


        // saving contact details to the database
        contactService.saveContact(contact);

        System.out.println("Contact saved successfully");

        // give message
        Message message = Message.builder().content("Contact saved successfully").type(MessageType.green).build();
        session.setAttribute("message", message);    

        return "redirect:/user/contacts/add";
    }


    // controller to view the contacts
    @RequestMapping("/view")
    public String viewContact(
        @RequestParam(value="page",defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = AppConstant.PAGE_SIZE) int size,
        @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
        @RequestParam(value = "order", defaultValue = "asc") String order, Model model, Authentication authentication){

        // get the email / User object of the logged in user
        String userEmail = Helpers.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(userEmail);

        // load all the contacts from the database
        Page<Contact> pageContacts = contactService.getContactByUser(user, page, size, sortBy, order);

        model.addAttribute("pageContacts", pageContacts);
        model.addAttribute("pageSize", AppConstant.PAGE_SIZE);
        model.addAttribute("searchContactForm", new SearchContactForm());
        return "/user/viewContact";
    }

    // controller to search contact

    @RequestMapping("/search")
    public String searchHandler(
        // @RequestParam("field") String field,
        // @RequestParam("keyword") String keyword,
        @ModelAttribute SearchContactForm searchContactForm,
        @RequestParam(value="page",defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = AppConstant.PAGE_SIZE) int size,
        @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
        @RequestParam(value = "order", defaultValue = "asc") String order , Model model, Authentication authentication){

        logger.info("field {} keyword {}",searchContactForm.getField(), searchContactForm.getValue());
            
        // fetching logged user
        User user=userService.getUserByEmail(Helpers.getEmailOfLoggedInUser(authentication));


        // fetching contacts from the database for keyword
        
        if(searchContactForm.getField().equals("name")){
            Page<Contact> pageContacts = contactService.searchContactByUserAndName(user,searchContactForm.getValue(),page, size,sortBy,order);
            model.addAttribute("pageContacts", pageContacts);
        }else if(searchContactForm.getField().equals("email")){
            Page<Contact> pageContacts = contactService.searchContactByUserAndEmail(user,searchContactForm.getValue(),page, size,sortBy,order);
            model.addAttribute("pageContacts", pageContacts);
        }else if(searchContactForm.getField().equals("phoneNumber")){
            Page<Contact> pageContacts = contactService.searchContactByUserAndPhoneNumber(user,searchContactForm.getValue(),page, size,sortBy,order);
            model.addAttribute("pageContacts", pageContacts);
        }



        
        model.addAttribute("pageSize", AppConstant.PAGE_SIZE);
        model.addAttribute("searchContactForm", searchContactForm);
        return "/user/searchContact";

    }

    @RequestMapping("/loadUpdateContact/{contactId}")
    public String loadUpdateContact(@PathVariable("contactId") String contactId,  Model model, Authentication authentication){
        System.out.println("Contact id : "+contactId);
        AddContactForm contactUpdate = new AddContactForm();
        Contact contactById = contactService.getContactById(contactId);
        
        contactUpdate.setName(contactById.getName());
        contactUpdate.setEmail(contactById.getEmail());
        contactUpdate.setPhoneNumber(contactById.getPhoneNumber());
        contactUpdate.setAddress(contactById.getAddress());
        contactUpdate.setAbout(contactById.getDescription());
        contactUpdate.setFavorite(contactById.isFavorite());
        contactUpdate.setWebsiteLink(contactById.getWebsiteLink());
        contactUpdate.setLinkedInLink(contactById.getLinkedInLink());
        contactUpdate.setPictureUrl(contactById.getPicture());

        model.addAttribute("contactUpdate", contactUpdate);
        model.addAttribute("contactId", contactId);
        
        return "user/updateContact";
    }


    @RequestMapping(value = "/update/{contactId}", method = RequestMethod.POST)
    public String updateContact(@PathVariable("contactId") String contactId, @Valid @ModelAttribute AddContactForm addContactForm, BindingResult bindingResult, HttpSession session){
        // Validating the form
        if(bindingResult.hasErrors()){
            session.setAttribute("message", Message.builder().content("Please fill the form correctly").type(MessageType.red).build());
            return "redirect:/user/contacts/loadUpdateContact/"+contactId;
        }

            // fetching contact details from the form
            System.out.println("updating contact form.......... ");

             

            // putting the data into contact object
            // Contact oldContact = new Contact();
            Contact oldContact = contactService.getContactById(contactId);
            System.out.println(oldContact.getName());
            oldContact.setId(contactId);
            oldContact.setName(addContactForm.getName());
            oldContact.setEmail(addContactForm.getEmail());
            oldContact.setPhoneNumber(addContactForm.getPhoneNumber()); 
            oldContact.setAddress(addContactForm.getAddress());
            oldContact.setDescription(addContactForm.getAbout());
            oldContact.setFavorite(addContactForm.isFavorite());
            oldContact.setWebsiteLink(addContactForm.getWebsiteLink());
            oldContact.setLinkedInLink(addContactForm.getLinkedInLink());
            

            if(addContactForm.getProFilePicture() != null && !addContactForm.getProFilePicture().isEmpty()){
                String filename = UUID.randomUUID().toString();
                String fileUrl = imageService.uploadImage(addContactForm.getProFilePicture(), filename);  
                imageService.deleteImage(oldContact.getCloudinaryImageId());
                oldContact.setPicture(fileUrl);
                oldContact.setCloudinaryImageId(filename);
            }

            contactService.updateContact(oldContact);
            System.out.println("Contact updated successfully");

        // give message
        Message message = Message.builder().content("Contact updated successfully").type(MessageType.green).build();
        session.setAttribute("message", message);    
        

        String url = "redirect:/user/contacts/loadUpdateContact/"+contactId;
        return url;
    }

}                               
