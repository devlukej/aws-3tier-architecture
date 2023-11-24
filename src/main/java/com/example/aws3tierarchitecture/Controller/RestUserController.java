package com.example.aws3tierarchitecture.Controller;

import com.example.aws3tierarchitecture.domain.entity.UserEntity;
import com.example.aws3tierarchitecture.domain.repository.UserRepository;
import com.example.aws3tierarchitecture.dto.CartDto;
import com.example.aws3tierarchitecture.dto.ProdDto;
import com.example.aws3tierarchitecture.service.CartService;
import com.example.aws3tierarchitecture.service.ProdService;
import com.example.aws3tierarchitecture.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
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

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RestUserController(UserService userService, UserRepository userRepository, ProdService prodService, CartService cartService, RedisTemplate<String, Object> redisTemplate) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.prodService = prodService;
        this.cartService = cartService;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/api/signup")
    public ResponseEntity<String> signup(@RequestBody UserEntity user) {

        return userService.saveUser(user);
    }

    @PostMapping("/api/login")
    public ResponseEntity<UserEntity> login(@RequestBody UserEntity user, HttpSession session) {

        return userService.loginUser(user, session);
    }


    @PostMapping("/api/logout") // 로그아웃 엔드포인트 추가
    public ResponseEntity<String> logout(HttpSession session) {

        String sessionId = session.getId();
        redisTemplate.delete(sessionId);
        return new ResponseEntity<>("로그아웃 성공!", HttpStatus.OK);
    }

    @GetMapping("/api/statusLogin")
    public ResponseEntity<Map<String, Object>> getStatusLogin(HttpSession session) {
        Map<String, Object> status = new HashMap<>();

        try {
            String sessionId = session.getId();
            UserEntity user = (UserEntity) redisTemplate.opsForValue().get(sessionId);

            if (user != null) {
                status.put("authenticated", true);
                status.put("nickname", user.getNickname());
                status.put("money", user.getMoney());
                status.put("state", user.getState());
            } else {
                status.put("authenticated", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

        String sessionId = session.getId();
        UserEntity user = (UserEntity) redisTemplate.opsForValue().get(sessionId);

        return cartService.getAllCartsByUserId(user.getId());
    }

    @PostMapping("/api/cart/add")
    public CartDto addToCart(@RequestBody CartDto cartDto, HttpSession session) {

        String sessionId = session.getId();
        UserEntity user = (UserEntity) redisTemplate.opsForValue().get(sessionId);

        cartDto.setUserId(user.getId());

        return cartService.addToCart(cartDto);
    }

    @DeleteMapping("/api/cart/remove/{cartId}")
    public void removeFromCart(@PathVariable Long cartId) {
        cartService.removeFromCart(cartId);
    }

    @Transactional
    @PostMapping("/api/purchase")
    public ResponseEntity<String> purchaseItems(@RequestBody List<Long> itemIds, HttpSession session) {
        try {
            String sessionId = session.getId();
            UserEntity user = (UserEntity) redisTemplate.opsForValue().get(sessionId);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated.");
            }

            int totalAmount = cartService.calculateTotalAmount(itemIds); // 상품 가격의 합계 계산

            if (user.getMoney() < totalAmount) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient funds.");
            }

            int newMoney = user.getMoney() - totalAmount;


            // Redis 업데이트
            redisTemplate.opsForValue().set(sessionId, new UserEntity(user.getId(), user.getUsername(), user.getPassword(), user.getNickname(), user.getState(), newMoney));

            // 데이터베이스 업데이트 시작
            UserEntity updatedUser = userService.getUserById(user.getId());
            userService.updateUserMoney(user.getId(), newMoney);

            for (Long itemId : itemIds) {
                cartService.removeFromCart(itemId);
            }

            return ResponseEntity.ok("Purchase successful.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during purchase.");
        }
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