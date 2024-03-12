package com.example.demo.common.oauth.domain;

import com.example.demo.common.Constant;
import com.example.demo.common.enums.OAuthProvider;
import org.springframework.util.MultiValueMap;

public interface OAuthLoginParams {
    OAuthProvider oAuthProvider();
    MultiValueMap<String, String> makeBody();
}