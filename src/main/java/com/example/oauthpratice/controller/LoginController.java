package com.example.oauthpratice.controller;

import com.example.oauthpratice.response.KakaoProfileResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;


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


    @GetMapping("/auth/result")
    public String result(@RequestAttribute("profileResponse") KakaoProfileResponse kakaoProfileResponse, Model model) {
        model.addAttribute("result", kakaoProfileResponse);
        return "result";
    }
}
