package com.movie.web.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AccountAPI {
    @GetMapping("/ajax-login-success")
    public String loginSuccess() {
        return "success";
    }

    @GetMapping("/ajax-login-failure")
    public String loginFailure() {
        return "failure";
    }

    @GetMapping("/ajax-register-success")
    public String registerSuccess() {
        return "success";
    }

    @GetMapping("/ajax-register-failure")
    public String registerFailure() {
        return "failure";
    }
}
