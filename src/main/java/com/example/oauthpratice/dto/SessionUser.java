package com.example.oauthpratice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author daecheol song
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public class SessionUser {
    private String name;
    private String email;
    private String picture;
}
