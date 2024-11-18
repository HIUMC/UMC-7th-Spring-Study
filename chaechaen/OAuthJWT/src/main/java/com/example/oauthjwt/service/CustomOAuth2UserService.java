package com.example.oauthjwt.service;

import com.example.oauthjwt.dto.GoogleResponse;
import com.example.oauthjwt.dto.NaverResponse;
import com.example.oauthjwt.dto.OAuth2Response;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println(oAuth2User);

        // 서비스가 네이버에서 온 요청인지, 구글에서 온 요청인지 확인
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) { // 이 값이 네이버인 경우

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) { // 이 값이 구글인 경우

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else { // 아무 값도 아닌 경우

            return null;
        }

        return null; //추후 수정
    }
}
