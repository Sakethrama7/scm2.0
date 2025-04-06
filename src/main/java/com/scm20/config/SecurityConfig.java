package com.scm20.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.scm20.services.Impl.SecurityCustomUserDetailService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@Configuration
public class SecurityConfig {
    // user create and login using java code with in memory service

    // These are the two users that we are created them progametically.
//     @Bean
//     public UserDetailsService userDetailsService(){
//
//      UserDetails user= User.withDefaultPasswordEncoder().username("admin123").password("admin123").roles("ADMIN,"USER").build();
//      UserDetails user1= User.withDefaultPasswordEncoder().username("admin123").password("admin123").build();
//
//        var inMemoryUserDetailsManager= new InMemoryUserDetailsManager(user,user1);
//        return inMemoryUserDetailsManager;
//     }

// Now in the below we are doing that which ever User get's registered that user can login.
// Here we are doing that How can we login the users , who are registered.
@Autowired
private SecurityCustomUserDetailService userDetailService;

// configuration of authentication provider for spring security.
@Bean
public DaoAuthenticationProvider authenticationProvider(){
    DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
    // object of user detail service 
    daoAuthenticationProvider.setUserDetailsService(userDetailService);
    // object of password encoder
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

    return daoAuthenticationProvider;

}

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

    
    // configuration
    httpSecurity.authorizeHttpRequests(authorize -> {
       // authorize.requestMatchers("/home","/register","/services","/login","/authenticate").permitAll();

        // configured urls so that only specific urls are private and remaining are public.
        authorize.requestMatchers("/user/**").authenticated();
        authorize.anyRequest().permitAll();
    });

   

    // form default login , if we want to change any form login related things then we can come here. 
    // httpSecurity.formLogin(Customizer.withDefaults());

    httpSecurity.formLogin(formLogin -> {
          
         // customising our login page.
         formLogin.loginPage("/login");
         // when you submit the login form that will submitted at authenticate page.
         formLogin.loginProcessingUrl("/authenticate");
         // if you login successfully then it will forward to this user/dashboard page
         formLogin.defaultSuccessUrl("/user/dashboard");
         //
         // formLogin.failureForwardUrl("/login?error=true");
         //
         formLogin.usernameParameter("email");
         formLogin.passwordParameter("password");


        //  formLogin.failureHandler(new AuthenticationFailureHandler() {

        //     @Override
        //     public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        //             AuthenticationException exception) throws IOException, ServletException {
        //         // TODO Auto-generated method stub
        //         throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationFailure'");
        //     }
            
        //  });

        //  formLogin.successHandler(new AuthenticationSuccessHandler() {

        //     @Override
        //     public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        //             Authentication authentication) throws IOException, ServletException {
        //         // TODO Auto-generated method stub
        //         throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationSuccess'");
        //     }
            
        //  });

    });

    httpSecurity.csrf(AbstractHttpConfigurer :: disable);

    httpSecurity.logout(logoutForm -> {
        logoutForm.logoutUrl("/logout");
        logoutForm.logoutSuccessUrl("/login?logout=true");
    });

    return httpSecurity.build();
}

@Bean
public PasswordEncoder passwordEncoder(){

    return new BCryptPasswordEncoder();
}

}
