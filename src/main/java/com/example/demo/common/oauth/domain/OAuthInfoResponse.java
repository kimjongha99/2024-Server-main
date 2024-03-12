package com.example.demo.common.oauth.domain;

import com.example.demo.common.Constant;
import com.example.demo.common.enums.OAuthProvider;

public interface OAuthInfoResponse {
    String getEmail();
    String getNickname();
    OAuthProvider getOAuthProvider();
}
