package com.example.oauthpratice.config;

import com.example.oauthpratice.response.KakaoProfileResponse;
import com.example.oauthpratice.service.OAuth2UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.session.DisableEncodeUrlFilter;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author daecheol song
 * @since 1.0
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.
                csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(authorizeRequestsCustomizer -> {
                    authorizeRequestsCustomizer.antMatchers("/auth/login", "/image/**", "/js/**", "/auth/logout/result").permitAll();
                    authorizeRequestsCustomizer.anyRequest().authenticated();
                })
                .oauth2Login(httpSecurityOAuth2LoginConfigurer -> {
                    httpSecurityOAuth2LoginConfigurer.loginPage("/auth/login");
                    httpSecurityOAuth2LoginConfigurer.userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(oAuth2UserService()));
                    httpSecurityOAuth2LoginConfigurer.successHandler(authenticationSuccessHandler());
                })
                .logout(httpSecurityLogoutConfigurer -> {
                    httpSecurityLogoutConfigurer.logoutUrl("/auth/logout");
                    httpSecurityLogoutConfigurer.logoutSuccessUrl("/auth/logout/result");
                    httpSecurityLogoutConfigurer.clearAuthentication(true);
                    httpSecurityLogoutConfigurer.invalidateHttpSession(true);
                })
                .build();
    }

    @Bean
    public DefaultOAuth2UserService oAuth2UserService() {
        return new OAuth2UserService();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return ((request, response, authentication) -> {
            DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

            Map<String, Object> attributes = defaultOAuth2User.getAttributes();

            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            for (String s : attributes.keySet()) {
                log.info("{} : {}", s, attributes.get(s));
            }

            KakaoProfileResponse kakaoProfileResponse = objectMapper.convertValue(attributes, KakaoProfileResponse.class);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.TEXT_HTML_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            request.setAttribute("profileResponse", kakaoProfileResponse);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/auth/result");
            requestDispatcher.forward(request, response);

        });
    }
}
