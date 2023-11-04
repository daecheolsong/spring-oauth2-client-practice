package com.example.oauthpratice.config;

import com.example.oauthpratice.resolver.CustomAuthorizationResolver;
import com.example.oauthpratice.service.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author daecheol song
 * @since 1.0
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final ClientRegistrationRepository clientRegistrationRepository;
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
                    httpSecurityOAuth2LoginConfigurer.authorizationEndpoint(clientRegistrationRepository -> clientRegistrationRepository.authorizationRequestResolver(oAuth2AuthorizationRequestResolver()));
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


            for (String s : attributes.keySet()) {
                log.info("{} : {}", s, attributes.get(s));
            }


            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.TEXT_HTML_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.sendRedirect("/");

        });
    }


    public OAuth2AuthorizationRequestResolver oAuth2AuthorizationRequestResolver() {
        return new CustomAuthorizationResolver(clientRegistrationRepository);
    }
}
