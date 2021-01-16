package com.udacity.jwdnd.course1.cloudstorage.config;

import com.udacity.jwdnd.course1.cloudstorage.services.AuthenticationService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/*
    the SecurityConfiguration class provides the following web security benefits:
        * it requires that the user needs to be authenticated prior to accessing any URL within our application
        * enables HTTP Basic and Form based authentication
        * Spring Security will automatically render a login page and logout success page for you
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    AuthenticationService authenticationService;

    public SecurityConfiguration (AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(this.authenticationService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/signup", "/css/**", "/js/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/home", true)
                .and()
            .logout(logout -> logout
                .permitAll()
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/login?logout")
            );
    }
}
