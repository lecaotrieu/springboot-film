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

    @Autowired
    private PersistentTokenRepository persistentTokenRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/user**")
                .authorizeRequests().anyRequest().hasRole("USER")
                .and()
                .formLogin()
                .loginPage("/dang-nhap")
                .usernameParameter("j_username_user")
                .passwordParameter("j_password_user")
                .loginProcessingUrl("/user_login")
                .defaultSuccessUrl("/trang-chu")
                .successHandler(customUserSuccessHandler)
                .failureUrl("/trang-chu?incorrectAccount")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/accessDenied-user")
                .and()
                .sessionManagement().invalidSessionUrl("/dang-nhap?sessionTimeout")
                .and()
                .logout().deleteCookies("JSESSIONID").invalidateHttpSession(true);
        http.csrf().disable();
        // Cấu hình Remember Me.
        http.authorizeRequests().and() //
                .rememberMe().tokenRepository(persistentTokenRepository).key("uniqueAndSecret") //
                .tokenValiditySeconds(1 * 24 * 60 * 60); // 24h
    }
}
