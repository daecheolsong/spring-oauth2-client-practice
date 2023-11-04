package com.example.oauthpratice.service;

import com.example.oauthpratice.dto.OAuth2UserInfo;
import com.example.oauthpratice.dto.SessionUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author daecheol song
 * @since 1.0
 */
public class OAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuth2UserInfo attributes = OAuth2UserInfo.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(false);

        SessionUser sessionUser = new SessionUser(attributes.getName(), attributes.getEmail(), attributes.getPicture());
        session.setAttribute("user", sessionUser);

        return new DefaultOAuth2User(authorities, attributes.getAttributes(), attributes.getNameAttributeKey());
    }

}
