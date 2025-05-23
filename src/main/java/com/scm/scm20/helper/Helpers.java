package com.scm.scm20.helper;


import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;



public class Helpers {

    


    public static String getEmailOfLoggedInUser(Authentication authentication){

        if(authentication instanceof OAuth2AuthenticationToken){
           var OAuth2AuthenticationToken =(OAuth2AuthenticationToken)authentication;
           var clientId = OAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
           var oAuthUser = (OAuth2User)authentication.getPrincipal();
           if(clientId.equalsIgnoreCase("google")){
                System.out.println("logged by google");
                String email = oAuthUser.getAttribute("email").toString();
                return email;
            
            }
            // else{
            //     // if more providers are their for example GITHUB
                
            // }
             
        }else{
            System.out.println("logged by  username password");
            return authentication.getName();
            // return "";
        }
                return null;

    }



    public static String getLinkForEmailVerification(String emailToken ){
        String verificationLink =  "http://localhost:8080/auth/verify-email?token=" + emailToken;
        return verificationLink;
    }

}
