package com.example.oauthpratice.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author daecheol song
 * @since 1.0
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoProfileResponse {
    private Long id;
    private LocalDateTime connectedAt;
    private Map<Object, Object> properties;
    private Map<Object, Object> kakaoAccount;
}
