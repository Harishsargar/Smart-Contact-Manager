package com.scm.scm20.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.scm.scm20.services.impl.SecurityCustomUserDetailService;


@Configuration
public class SecurityConfig {
    
    // // create user and login in java using memory service
    // @Bean
    // public UserDetailsService userDetailsService(){


    //     // we are manually creating a user (username,password)
    //     UserDetails user1 = User
    //     .withDefaultPasswordEncoder()
    //     .username("user123")
    //     .password("123")
    //     .roles("USER")
    //     .build();

    //     UserDetails user2 = User
    //     .withDefaultPasswordEncoder()
    //     .username("admin123")
    //     .password("123")
    //     .roles("ADMIN")
    //     .build();

    //     // the created user is pass in inMemoryUserDetailsManager it will
    //     InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager(user1,user2);
    //     return inMemoryUserDetailsManager;
    // }


    // this will load the user from db
    @Autowired
    private SecurityCustomUserDetailService securityCustomUserDetailService;


    @Autowired
    private OAuthAuthenticationSuccessHandler handler;

    @Autowired
    private AuthFailureHandler authFailureHandler;

    // COnfiguration of authentication provider
    // user login using DataBase user
    // it will internally verify the username and password and do the login 
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        // want the userDateailService ka object to pass here
        daoAuthenticationProvider.setUserDetailsService(securityCustomUserDetailService);
        // want the passwordEncoder ka object to pass here
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    //Security chain configuration 
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        System.out.println("Securing all the api's");
        // securing routes 
        httpSecurity.authorizeHttpRequests(authorize->{
            // authorize.requestMatchers("/home","/register").permitAll();
            // it will secure all routes starting from user and rest will be not secure
            authorize.requestMatchers("/user/**").authenticated();
            // to secure all the api's

            authorize.requestMatchers("/api/**").authenticated();
            authorize.anyRequest().permitAll();  
        });

        

        // login form configuration
        // httpSecurity.formLogin(Customizer.withDefaults()); // will show default spring login form

        // to show custom login page
        httpSecurity.formLogin(formLogin->{
            formLogin.loginPage("/login");   // this login page is on "/login"
            formLogin.loginProcessingUrl("/authenticate");   // but it will submit on "/authenticate"
            formLogin.successForwardUrl("/user/profile");   // once the user is authenticated it will redirected to dashboard
            formLogin.failureForwardUrl("/login?error=true");
            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");
            formLogin.failureHandler(authFailureHandler);  //it id handling and sending the messages to the frontend

            
            // formLogin.failureHandler(new AuthenticationFailureHandler() {

            //     @Override
            //     public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            //             AuthenticationException exception) throws IOException, ServletException {
            //         
            //         throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationFailure'");
            //     }
                
            // });

            // formLogin.successHandler(new AuthenticationSuccessHandler() {

            //     @Override
            //     public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            //             Authentication authentication) throws IOException, ServletException {
            //         
            //         throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationSuccess'");
            //     }
                
            // });
        });


        // logout form configuration
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(logoutForm->{
            logoutForm.logoutUrl("/logout"); 
            logoutForm.logoutSuccessUrl("/login?logout=true");
        });



        // OAuth Configuration
        // httpSecurity.oauth2Login(Customizer.withDefaults());    //this is the default 
        httpSecurity.oauth2Login(oauth->{
            oauth.loginPage("/login");
            oauth.successHandler(handler);
        });


        return httpSecurity.build();
    }



    // this will create the object of passwordEncoder which we will pass in setPasswordEncoder in AuthenticationProvider method
    @Bean   
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
