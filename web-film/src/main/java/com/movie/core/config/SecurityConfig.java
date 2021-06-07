package com.movie.core.config;

import com.movie.core.service.impl.CustomUserDetailsService;
import com.movie.web.security.CustomUserSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
@Order(2)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserSuccessHandler customUserSuccessHandler;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/trang-ca-nhan/**").hasRole("USER")
                .and()
                .formLogin()
                .loginPage("/trang-chu")
                .usernameParameter("j_username_user")
                .passwordParameter("j_password_user")
                .loginProcessingUrl("/trang-ca-nhan/login")
                .defaultSuccessUrl("/ajax-login-success")
                .failureUrl("/ajax-login-failure")
                .and()
                .sessionManagement().invalidSessionUrl("/trang-chu")
                .and()
                .logout().deleteCookies("JSESSIONID", "remember-me")
                .and()
                .rememberMe().rememberMeParameter("remember-me").key("remember-key").tokenValiditySeconds(1 * 24 * 60 * 60);
        http.csrf().disable();

    }
}
