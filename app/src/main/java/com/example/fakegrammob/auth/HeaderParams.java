package com.example.fakegrammob.auth;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class HeaderParams {
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String AUTH_HEADER = "Authorization";
}
