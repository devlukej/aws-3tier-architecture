package com.example.aws3tierarchitecture.service;

import com.example.aws3tierarchitecture.domain.entity.UserEntity;
import com.example.aws3tierarchitecture.domain.repository.UserRepository;
import com.example.aws3tierarchitecture.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, RedisTemplate<String, Object> redisTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.redisTemplate = redisTemplate;
    }

    public UserEntity updateUserMoney(Long userId, int newMoney) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setMoney(newMoney);
        return userRepository.save(user);
    }

    public UserEntity getUserById(Long userId) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        return optionalUser.orElse(null);
    }

    public ResponseEntity<String> saveUser(UserEntity user) {
        // 사용자 이름(username) 및 닉네임(nickname) 중복 검사
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return new ResponseEntity<>("존재하는 아이디입니다.", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.findByNickname(user.getNickname()).isPresent()) {
            return new ResponseEntity<>("존재하는 닉네임입니다.", HttpStatus.BAD_REQUEST);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // 사용자 저장
        userRepository.save(user);

        return new ResponseEntity<>("회원가입 성공!", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<UserEntity> loginUser(UserEntity user, HttpSession session) {
        // 사용자 이름(username) 확인
        UserEntity existingUser = userRepository.findByUsername(user.getUsername()).orElse(null);

        // 사용자가 존재하고 비밀번호가 일치하는지 확인
        if (existingUser != null && passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            String sessionId = session.getId();

            // Redis에 사용자 정보 저장
            redisTemplate.opsForValue().set(sessionId, existingUser);

            return new ResponseEntity<>(existingUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}