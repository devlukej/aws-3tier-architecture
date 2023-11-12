package com.example.aws3tierarchitecture.service;

import com.example.aws3tierarchitecture.domain.entity.UserEntity;
import com.example.aws3tierarchitecture.domain.repository.UserRepository;
import com.example.aws3tierarchitecture.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private static final String USER_SESSION_PREFIX = "user-session:";

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity updateUserMoney(Long userId, int newMoney) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setMoney(newMoney);
        return userRepository.save(user);
    }

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void loginUser(String userId) {
        // 로그인 성공 시, 세션 정보를 Redis에 저장
        String sessionKey = USER_SESSION_PREFIX + userId;
        redisTemplate.opsForValue().set(sessionKey, true);
    }

    public boolean isUserLoggedIn(String userId) {
        // 로그인 여부 확인
        String sessionKey = USER_SESSION_PREFIX + userId;
        return Boolean.TRUE.equals(redisTemplate.opsForValue().get(sessionKey));
    }

    public void logoutUser(String userId) {
        // 로그아웃 시, Redis에서 세션 정보 제거
        String sessionKey = USER_SESSION_PREFIX + userId;
        redisTemplate.delete(sessionKey);
    }
}