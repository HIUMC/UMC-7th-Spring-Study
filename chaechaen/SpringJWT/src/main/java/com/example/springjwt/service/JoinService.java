package com.example.springjwt.service;

import com.example.springjwt.dto.JoinDTO;
import com.example.springjwt.entity.UserEntity;
import com.example.springjwt.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) { // 생성자 주입

        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder; // 초기화
    }

    public void joinProcess(JoinDTO joinDTO){

        String username = joinDTO.getUsername(); // joinDTO에서 get 메서드로 username 꺼내기
        String password = joinDTO.getPassword(); // joinDTO에서 get 메서드로 password 꺼내기

        Boolean isExist = userRepository.existsByUsername(username); // userRepository에서 만든 메서드 -> 유저 이름이 존재하는지 true/false로 받기

        if (isExist) { // 존재하면 이 메서드 종료 시킴
            return;
        }

        UserEntity data = new UserEntity(); // 존재하지 않으면 회원가입 진행 (DTO에서 받은 데이터를 엔티티로 옮겨주기)
        data.setUsername(username);
        data.setPassword(bCryptPasswordEncoder.encode(password)); // 암호화 해야 함
        data.setRole("ROLE_ADMIN"); // 강제로 권한 만들기

        userRepository.save(data); // 엔티티값을 저장해주기
    }

}
