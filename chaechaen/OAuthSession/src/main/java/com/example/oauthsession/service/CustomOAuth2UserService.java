package com.example.oauthsession.service;

import com.example.oauthsession.dto.CustomOAuth2User;
import com.example.oauthsession.dto.GoogleResponse;
import com.example.oauthsession.dto.NaverResponse;
import com.example.oauthsession.dto.OAuth2Response;
import com.example.oauthsession.entity.UserEntity;
import com.example.oauthsession.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService { // DefaultOAuth2UserService 상속 받기

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //DefaultOAuth2UserService OAuth2UserService의 구현체

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest); // 부모 클래스에 존재하는 loadUser 메서드에 userRequest 넣어 유저 정보 가져옴
        System.out.println(oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 어떤 인증 provider인지 변수를 담아옴

        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());

        }
        else if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());

        }
        else {

            return null;
        }

        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        UserEntity existData = userRepository.findByUsername(username);

        String role = null;
        if (existData == null) { // 존재하지 않으므로 회원가입

            UserEntity userEntity = new UserEntity(); // 새로운 엔티티 객체를 만듦

            // 유저 엔티티에 값들 집어 넣어주기
            userEntity.setUsername(username);
            userEntity.setEmail(oAuth2Response.getEmail());
            userEntity.setRole("ROLE_USER");

            userRepository.save(userEntity);
        }
        else { // 존재하므로 업데이트

            existData.setUsername(username); // 조회한 데이터에 대해 업데이트
            existData.setEmail(oAuth2Response.getEmail());

            role = existData.getRole();

            userRepository.save(existData);
        }

        return new CustomOAuth2User(oAuth2Response, role);
    }
}