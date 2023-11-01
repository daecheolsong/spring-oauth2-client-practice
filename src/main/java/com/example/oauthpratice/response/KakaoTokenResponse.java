package com.example.oauthpratice.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.ToString;

/**
 * @author daecheol song
 * @since 1.0
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ToString
public class KakaoTokenResponse {
    private String tokenType;
    private String accessToken;
    private String idToken;
    private Integer expiresIn;
    private String refreshToken;
    private Integer refreshTokenExpiresIn;
    private String scope;


}
