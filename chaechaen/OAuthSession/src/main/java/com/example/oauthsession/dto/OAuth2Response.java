package com.example.oauthsession.dto;

public interface OAuth2Response {

    String getProvider(); // 네이버, 구글 등 제공자 이름

    String getProviderId(); // 제공자에서 각각의 유저에게 발금해준 아이디(번호)

    String getEmail(); // 이메일

    String getName(); // 사용자 실명 (설정한 이름)
}
