package com.scm.scm20.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.scm20.entity.User;
import com.scm.scm20.forms.UserForm;
import com.scm.scm20.helper.Message;
import com.scm.scm20.helper.MessageType;
import com.scm.scm20.services.impl.UserServiceImpl;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class pagecontroller {


    @Autowired
    private UserServiceImpl userServiceImpl;


    @GetMapping("/")
    public String index(){
        return "redirect:/home";
    }

    // homepage
    @RequestMapping("/home")
    public String home(Model model) {
        // sending data to html templates
        model.addAttribute("name", "Harish Sargar");
        model.addAttribute("link", "https://www.google.co.in/");
        return "home";
    }

    // about page
    @RequestMapping("/about")
    public String aboutPage(){
        return "about";
    }

    //services
    @RequestMapping("/services")
    public String services(){
        return "services";
    }
    
    @RequestMapping("/contact")
    public String contact(){
        return "contact";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }
    @RequestMapping("/register")
    public String register(Model model){
        UserForm userForm = new UserForm();
        // userForm.setName("harish");
        model.addAttribute("userForm", userForm);   // we are sending empty object to register page(view) 

        // we can also send object with data
            // userForm.setName("harish");
            // model.addAttribute("userForm", userForm);

        return "register";
    }

    //processing register
    @RequestMapping(value = "/do-register", method=RequestMethod.POST)
    public String processingRegister(@Valid @ModelAttribute UserForm userForm, BindingResult bindingResult, HttpSession session) {

        // fetch form data
        

        // from UserForm --> User
        // User user = User.builder()
        // .name(userForm.getName())
        // .email(userForm.getEmail())
        // .password(userForm.getPassword())
        // .about(userForm.getAbout())
        // .phoneNumber(userForm.getPhoneNumber())
        // .profiePic("https://learncodewithdurgesh.com/_next/image?url=%2F_next%2Fstatic%2Fmedia%2Flcwd_logo.45da3818.png&w=1080&q=75")
        // .build();
        // we are not using builder because default values are not getting stored (eg. Provider ka SELF in DB)
        System.out.println(userForm);
        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setEnabled(false);
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setProfilePic("https://learncodewithdurgesh.com/_next/image?url=%2F_next%2Fstatic%2Fmedia%2Flcwd_logo.45da3818.png&w=1080&q=75");
        

        // validate form data
        if(bindingResult.hasErrors()){
            return "/register";
        }

        // save to database
        User savedUser = userServiceImpl.saveUser(user);
        System.out.println("user saved: "+ savedUser );
        
        // give message
        Message message = Message.builder().content("Registration Successful").type(MessageType.green).build();
        session.setAttribute("message", message);

        // redirect to login form

        return "redirect:/register";
    }
    
}
