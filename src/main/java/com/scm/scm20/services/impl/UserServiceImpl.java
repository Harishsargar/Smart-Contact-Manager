package com.scm.scm20.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.scm20.entity.User;
import com.scm.scm20.helper.AppConstant;
import com.scm.scm20.helper.Helpers;
import com.scm.scm20.helper.ResourceNotFoundException;
import com.scm.scm20.repositories.UserRepo;
import com.scm.scm20.services.EmailService;
import com.scm.scm20.services.UserService;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public User saveUser(User user) {

        // user id : have to generate
        String userId = UUID.randomUUID().toString();

        user.setUserId(userId);

        // this will encode the password and then store to the DB
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // set the user role
        user.setRollList(List.of(AppConstant.ROLE_USER));

        logger.info(user.getProviders().toString());
       User savedUser= userRepo.save(user);

       String emailToken = UUID.randomUUID().toString();
       String verificationLink = Helpers.getLinkForEmailVerification(emailToken);
       emailService.sendEmail(savedUser.getEmail(), "account verification link", verificationLink);
       return savedUser;
    }

    @Override
    public User getUserById(String id) {
        return userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("user not found"));
    }

    @Override
    public Optional<User> updatUser(User user) {

        // in user2 we will get the user's data from DB
        User user2 = userRepo.findById(user.getUserId()).orElseThrow(()-> new ResourceNotFoundException("User Not Found"));
        //once we get data from DB will update user2 
        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        user2.setPassword(user.getPassword());
        user2.setAbout(user.getAbout());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setProfilePic(user.getProfilePic());
        user2.setEnabled(user.isEnabled());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setPhoneVerified(user.isPhoneVerified());
        user2.setProviders(user.getProviders());
        user2.setProviderUserId(user.getProviderUserId());
        // saving data to DB
        User save = userRepo.save(user2);
        return Optional.ofNullable(save);
    }

    @Override
    public void deleteUser(String id) {
        User user2 = userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("User Not Found"));
        userRepo.delete(user2);
    }

    @Override
    public boolean isUserExist(String userId) {
        User user2 = userRepo.findById(userId).orElse(null);
        return user2!=null ? true : false;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        User user2 = userRepo.findByEmail(email).orElse(null);
        return user2!=null ? true : false; 
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
        
    }

    
}
