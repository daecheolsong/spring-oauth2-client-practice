package com.example.oauthpratice.controller;

import com.example.oauthpratice.response.KakaoTokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;


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

    @GetMapping("/auth/kakao/callback")
    public String responseCallback(@RequestParam("code") String code, Model model) {

        // 액세스 토큰 받기
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        // TODO : client_id, client_secret 수정
        params.add("client_id", "${CLIENT_ID}");
        params.add("client_secret", "${CLIENT_SECRET}");
        params.add("redirect_uri", "http://localhost:8080/auth/kakao/callback");
        params.add("code", code);
        params.add("scope", "profile_nickname profile_image account_email");
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, httpHeaders);

        ResponseEntity<KakaoTokenResponse> tokenResponseEntity = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                KakaoTokenResponse.class
        );

        // 엑세스 토큰으로 사용자 정보 가져오기
        KakaoTokenResponse kakaoTokenResponse = tokenResponseEntity.getBody();
        String accessToken = kakaoTokenResponse.getAccessToken();
        log.info("accessToken : {}", accessToken);
        log.info("idToken : {}", kakaoTokenResponse.getIdToken());
        log.info("refreshToken : {}", kakaoTokenResponse.getRefreshToken());
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=" + StandardCharsets.UTF_8);

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(httpHeaders);

        ResponseEntity<String> kakaoProfileResponseEntity = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST, // 요청할 방식
                kakaoProfileRequest, // 요청할 때 보낼 데이터
                String.class);// 요청 시 반환되는 데이터 타입

        model.addAttribute("result", kakaoProfileResponseEntity.getBody());
        model.addAttribute("token", code);

        return "result";
    }

}
