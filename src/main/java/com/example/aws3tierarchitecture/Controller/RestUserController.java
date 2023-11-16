package com.example.aws3tierarchitecture.Controller;

import com.example.aws3tierarchitecture.domain.entity.UserEntity;
import com.example.aws3tierarchitecture.domain.repository.UserRepository;
import com.example.aws3tierarchitecture.dto.CartDto;
import com.example.aws3tierarchitecture.dto.ProdDto;
import com.example.aws3tierarchitecture.service.CartService;
import com.example.aws3tierarchitecture.service.ProdService;
import com.example.aws3tierarchitecture.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@RestController
public class RestUserController {

    private final UserService userService;

    private final ProdService prodService;
    private final CartService cartService;
    private final UserRepository userRepository;

    @Autowired
    public RestUserController(UserService userService, UserRepository userRepository, ProdService prodService,CartService cartService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.prodService = prodService;
        this.cartService = cartService;
    }

    @PostMapping("/api/signup")
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

    @PostMapping("/api/login")
    public ResponseEntity<UserEntity> login(@RequestBody UserEntity user, HttpSession session) {
        // 사용자 이름(username) 및 비밀번호(password) 확인
        UserEntity existingUser = userRepository.findByUsername(user.getUsername()).orElse(null);

        if (existingUser == null || !existingUser.getPassword().equals(user.getPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        existingUser = userRepository.save(existingUser);

        session.setAttribute("user", existingUser);

        return new ResponseEntity<>(existingUser, HttpStatus.OK);
    }


    @PostMapping("/api/logout") // 로그아웃 엔드포인트 추가
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate(); // 세션을 비워서 로그아웃
        return new ResponseEntity<>("로그아웃 성공!", HttpStatus.OK);
    }

    @GetMapping("/api/statusLogin")
    public ResponseEntity<Map<String, Object>> getStatusLogin(HttpSession session) {
        Map<String, Object> status = new HashMap<>();

        UserEntity user = (UserEntity) session.getAttribute("user");

        if (user != null) {
            status.put("authenticated", true);
            status.put("nickname", user.getNickname()); // 현재 사용자의 닉네임을 가져오도록 수정
            status.put("money", user.getMoney());
        } else {
            status.put("authenticated", false);
        }

        return ResponseEntity.ok(status);
    }

    @GetMapping("/api/prodList")
    public ResponseEntity<List<ProdDto>> getAllProducts() {
        List<ProdDto> prodDtoList = prodService.getAllProds();
        return ResponseEntity.ok(prodDtoList);
    }

    @GetMapping("/api/cart/all")
    public List<CartDto> getAllCarts(HttpSession session) {

        UserEntity user = (UserEntity) session.getAttribute("user");

        return cartService.getAllCartsByUserId(user.getId());
    }

    @PostMapping("/api/cart/add")
    public CartDto addToCart(@RequestBody CartDto cartDto, HttpSession session) {

        UserEntity user = (UserEntity) session.getAttribute("user");
        cartDto.setUserId(user.getId());

        return cartService.addToCart(cartDto);
    }

    @DeleteMapping("/api/cart/remove/{cartId}")
    public void removeFromCart(@PathVariable Long cartId) {
        cartService.removeFromCart(cartId);
    }

    @PostMapping("/api/purchase")
    public ResponseEntity<String> purchaseItems(@RequestBody List<Long> itemIds, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated.");
        }

        int totalAmount = cartService.calculateTotalAmount(itemIds); // 상품 가격의 합계 계산

        if (user.getMoney() < totalAmount) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient funds.");
        }

        // 상품 가격을 사용자의 돈에서 차감
        int newMoney = user.getMoney() - totalAmount;
        userService.updateUserMoney(user.getId(), newMoney);

        // 여기에서 상품을 실제로 제거하는 로직을 추가

        return ResponseEntity.ok("Purchase successful.");
    }

    @GetMapping("/api/search")
    public ResponseEntity<List<ProdDto>> getAllProducts(@RequestParam("keyword") String keyword) {
        if (Objects.equals(keyword, "")) {
            List<ProdDto> prodDtoList = prodService.getAllProds();
            return ResponseEntity.ok(prodDtoList);
        } else {
            List<ProdDto> prodList = prodService.searchName(keyword);
            return ResponseEntity.ok(prodList);
        }
    }

}