package com.example.oauthpratice.controller;

import com.example.oauthpratice.dto.SessionUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;



/**
 * @author daecheol song
 * @since 1.0
 */
@Controller
@Slf4j
public class LoginController {
    @GetMapping("/auth/login")
    public String login() {
        return "login";
    }


    @GetMapping("/")
    public String result(@SessionAttribute("user") SessionUser sessionUser, Model model) {
        model.addAttribute("user", sessionUser);
        return "index";
    }
}
