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
        http
                .authorizeRequests()
                .antMatchers("/account/login-admin").permitAll()
                .antMatchers("/admin/employee/**").hasRole("ADMIN")
                .antMatchers("/admin/user/**").hasAnyRole("ADMIN", "MANAGER")
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
                .loginProcessingUrl("/j_spring_security_check_admin")
                .defaultSuccessUrl("/admin/home-page")
                .successHandler(customSuccessHandler)
                .failureUrl("/account/login-admin?incorrectAccount")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/accessDenied-admin")
                .and()
                .sessionManagement().invalidSessionUrl("/account/login-admin?sessionTimeout")
                .and()
                .logout().deleteCookies("JSESSIONID");

        // Cấu hình Remember Me.
        http.authorizeRequests().and() //
                .rememberMe().tokenRepository(this.persistentTokenRepository()).key("uniqueAndSecret") //
                .tokenValiditySeconds(1 * 24 * 60 * 60); // 24h
        http.csrf().disable();
    }

    @Autowired
    private DataSource dataSource;

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
