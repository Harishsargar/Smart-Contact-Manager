package com.scm.scm20.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.scm20.entity.User;
import com.scm.scm20.forms.UserForm;
import com.scm.scm20.helper.Helpers;
import com.scm.scm20.services.UserService;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @ModelAttribute
    public void addLoggedInUserInfo(Model model, Authentication authentication){
        String username = Helpers.getEmailOfLoggedInUser(authentication);
        logger.info("Logged user:"+ username);
        // fetching user from DB
        User user = userService.getUserByEmail(username);
        System.out.println(user.getName());
        System.out.println(user.getPassword());
        model.addAttribute("loggedInUser", user);
    }

    // user dashboard page
    @RequestMapping("/dashboard")
    public String userDashboard() {
        return "user/dashboard";
    }
    
    // user profile page
    @RequestMapping("/profile")
    public String userProfile(Model model,Authentication authentication) {
        // infor is added by addLoggedInUserInfo
        return "user/profile";
    }

    @RequestMapping("/updateUser/{userId}")
    public String userUpdate(@PathVariable String userId, Model model){
        

        User oldUser = userService.getUserById(userId);
        UserForm loadUserFormData = new UserForm();
        loadUserFormData.setName(oldUser.getName());
        loadUserFormData.setEmail(oldUser.getEmail());
        loadUserFormData.setAbout(oldUser.getAbout());
        loadUserFormData.setPhoneNumber(oldUser.getPhoneNumber());

        model.addAttribute("loadUserFormData", loadUserFormData);
        
        return "/user/updateUser";
    }


    @RequestMapping("/do-updateUser")
    public String doUpdae(){

        return "user/profile";
    }

}
