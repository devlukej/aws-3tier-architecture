package com.example.aws3tierarchitecture.Controller;

import com.example.aws3tierarchitecture.domain.entity.UserEntity;
import com.example.aws3tierarchitecture.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class UserController {

    @GetMapping("/")
    public String dispMain() {

        return "index";
    }

    @GetMapping("/signup")
    public String dispSignup() {

        return "signup";
    }

    @GetMapping("/list")
    public String dispList() {

        return "list";
    }
}
