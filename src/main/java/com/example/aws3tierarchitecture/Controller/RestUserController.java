package com.example.aws3tierarchitecture.Controller;

import com.example.aws3tierarchitecture.domain.entity.UserEntity;
import com.example.aws3tierarchitecture.domain.repository.UserRepository;
import com.example.aws3tierarchitecture.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;


@RestController
@RequestMapping("/api")
public class RestUserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public RestUserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserEntity user) {
        // 사용자 이름(username) 및 닉네임(nickname) 중복 검사
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return new ResponseEntity<>("존재하는 아이디입니다.", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.findByNickname(user.getNickname()).isPresent()) {
            return new ResponseEntity<>("존재하는 닉네임입니다.", HttpStatus.BAD_REQUEST);
        }

        // 사용자 저장
        userRepository.save(user);

        return new ResponseEntity<>("회원가입 성공!", HttpStatus.OK);
    }

    @GetMapping("/users")
    public List<UserEntity> getAllUsers() {

        return userService.getAllUsers();
    }

}
