package com.example.demo.user;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo._core.handler.ex.Exception400;

@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void join(UserRequest.JoinDTO joinDTO) {
        // 1. 아이디 중복 체크 (안전 장치)
        userRepository.findByUsername(joinDTO.getUsername()).ifPresent(user -> {
            throw new Exception400("중복된 아이디입니다.");
        });

        // 2. 비밀번호 암호화 (BCrypt)
        String encPassword = BCrypt.hashpw(joinDTO.getPassword(), BCrypt.gensalt());
        joinDTO.setPassword(encPassword);

        // 3. 저장
        userRepository.save(joinDTO.toEntity());
    }

    public boolean usernameSameCheck(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
