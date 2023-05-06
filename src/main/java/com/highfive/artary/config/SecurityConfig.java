package com.highfive.artary.config;

import com.highfive.artary.service.UserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .authorizeRequests(request->
                        request.antMatchers("/", "/users/signup/**",
                                        "/users/login/**", "/users/email", "/users/password").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(login->
                        login.loginPage("/users/login")// url 다시 생각해보기
                                .loginProcessingUrl("/users/loginprocess")
                                .permitAll()
                                .defaultSuccessUrl("/", false)
                                .failureUrl("/users/login-error")
                )
                .logout(logout->
                        logout.logoutSuccessUrl("/"))
                .exceptionHandling(error->
                        error.accessDeniedPage("/users/access-denied")
                )
        ;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()
                )
        ;
    }

}
