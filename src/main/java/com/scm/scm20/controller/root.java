package com.scm.scm20.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.scm20.entity.User;
import com.scm.scm20.helper.Helpers;
import com.scm.scm20.services.UserService;

@ControllerAdvice
public class root {
        @Autowired
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @ModelAttribute
    public void addLoggedInUserInfo(Model model, Authentication authentication){
        if(authentication==null){
            return;
        }
        String username = Helpers.getEmailOfLoggedInUser(authentication);
        logger.info("Logged user:"+ username);
        // fetching user from DB
        User user = userService.getUserByEmail(username);
        System.out.println(user.getName());
        System.out.println(user.getPassword());
        model.addAttribute("loggedInUser", user);
    }

}
