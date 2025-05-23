package com.scm.scm20.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.scm20.entity.Providers;
import com.scm.scm20.entity.User;
import com.scm.scm20.helper.AppConstant;
import com.scm.scm20.repositories.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepo userRepo;


    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
                logger.info("OAuthAuthenticationSuccessHandler");

                // identify the provider
                




                // save data in Database
                DefaultOAuth2User user = (DefaultOAuth2User)authentication.getPrincipal();
                //  logging (printing on terminal)
                // logger.info(user.getName());
                // logger.info(user.getAttributes().toString());
                // logger.info(user.getAuthorities().toString());


                String email = user.getAttribute("email").toString();
                String name = user.getAttribute("name").toString();
                String pictutr = user.getAttribute("picture").toString();

                // create an user
                User user1 = new User();
                user1.setEmail(email);
                user1.setName(name);
                user1.setProfilePic(pictutr);
                user1.setPassword("password");
                user1.setUserId(UUID.randomUUID().toString());
                user1.setProviders(Providers.GOOGLE);
                user1.setEnabled(true);
                user1.setEmailVerified(true);
                user1.setProviderUserId(user.getName());
                user1.setRollList(List.of(AppConstant.ROLE_USER));
                user1.setAbout("This account is created by Google");

                User user2 = userRepo.findByEmail(email).orElse(null);
                if(user2==null){
                    userRepo.save(user1);
                    logger.info("user saved: "+ email);
                }

                // response.sendRedirect("/home");
                new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }

}
