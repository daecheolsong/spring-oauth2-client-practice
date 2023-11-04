package com.example.oauthpratice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author daecheol song
 * @since 1.0
 */
@Controller
public class LogoutController {

    @GetMapping("/auth/logout/result")
    public String logoutRedirect() {
        return "logout_result";
    }
}
