package com.movie.core.config;

import com.movie.core.service.impl.CustomEmployeeDetailsService;
import com.movie.web.security.CustomSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@Order(1)
public class AdminSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomSuccessHandler customSuccessHandler;

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomEmployeeDetailsService();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/admin/**")
                .authorizeRequests()
                .antMatchers("/admin/employee/**").hasRole("ADMIN")
                .antMatchers("/admin/user/**").hasAnyRole("ADMIN", "MANAGER")
                .antMatchers("/admin/video/**").hasAnyRole("ADMIN", "MANAGER")
                .antMatchers("/api/admin/film**").hasAnyRole("ADMIN", "POSTER")
                .antMatchers("/api/admin/employee**").hasRole("ADMIN")
                .antMatchers("/api/admin/user**").hasAnyRole("ADMIN", "MANAGER")
                .antMatchers("/admin/film/**").hasAnyRole("ADMIN", "POSTER")
                .antMatchers("/admin/**").hasAnyRole("ADMIN", "POSTER", "MANAGER")
                .and()
                .formLogin()
                .loginPage("/account/login-admin")
                .usernameParameter("j_username")
                .passwordParameter("j_password")
                .loginProcessingUrl("/admin/login")
                .defaultSuccessUrl("/admin/home-page")
                .successHandler(customSuccessHandler)
                .failureUrl("/account/login-admin?incorrectAccount")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/accessDenied-admin")
                .and()
                .sessionManagement().invalidSessionUrl("/trang-chu")
                .and()
                .logout().deleteCookies("JSESSIONID","remember-me")
                .and()
                .rememberMe().rememberMeParameter("remember-me").key("remember-key").tokenValiditySeconds(1 * 24 * 60 * 60);
        http.csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
