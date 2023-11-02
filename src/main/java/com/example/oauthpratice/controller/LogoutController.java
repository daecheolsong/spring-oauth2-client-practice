package com.example.oauthpratice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author daecheol song
 * @since 1.0
 */
@Controller
public class LogoutController {

    @GetMapping("/auth/kakao/logout")
    public String logout() {
        return "logout";
    }

}
