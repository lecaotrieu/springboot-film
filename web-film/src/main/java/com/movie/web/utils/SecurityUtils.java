package com.movie.web.utils;

import com.movie.core.dto.MyEmployee;
import com.movie.core.dto.MyUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SecurityUtils {
    public static MyUser getUserPrincipal() {
        MyUser myUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return myUser;
    }

    public static MyEmployee getPrincipal() {
        MyEmployee myEmployee = (MyEmployee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return myEmployee;
    }

    @SuppressWarnings("unchecked")
    public static List<String> getUserAuthorities() {
        List<String> result = new ArrayList<String>();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof MyUser) {
            List<GrantedAuthority> authorities = (List<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
            for (GrantedAuthority authority : authorities) {
                result.add(authority.getAuthority());
            }
        }
        return result;
    }
    @SuppressWarnings("unchecked")
    public static List<String> getEmployeeAuthorities() {
        List<String> result = new ArrayList<String>();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof MyEmployee) {
            List<GrantedAuthority> authorities = (List<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
            for (GrantedAuthority authority : authorities) {
                result.add(authority.getAuthority());
            }
        }
        return result;
    }

    public static String toString(User user) {
        StringBuilder sb = new StringBuilder();

        sb.append("UserName:").append(user.getUsername());

        Collection<GrantedAuthority> authorities = user.getAuthorities();
        if (authorities != null && !authorities.isEmpty()) {
            sb.append(" (");
            boolean first = true;
            for (GrantedAuthority a : authorities) {
                if (first) {
                    sb.append(a.getAuthority());
                    first = false;
                } else {
                    sb.append(", ").append(a.getAuthority());
                }
            }
            sb.append(")");
        }
        return sb.toString();
    }
}
