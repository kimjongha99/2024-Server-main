package com.example.demo.common.oauth.domain;

import com.example.demo.common.enums.OAuthProvider;

public interface OAuthApiClient {

    OAuthProvider oAuthProvider();
    String requestAccessToken(OAuthLoginParams params);
    OAuthInfoResponse requestOauthInfo(String accessToken);

}
