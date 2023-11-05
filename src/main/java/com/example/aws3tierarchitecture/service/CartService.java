package com.example.aws3tierarchitecture.service;

import com.example.aws3tierarchitecture.domain.entity.CartEntity;
import com.example.aws3tierarchitecture.domain.entity.ProdEntity;
import com.example.aws3tierarchitecture.domain.entity.UserEntity;
import com.example.aws3tierarchitecture.domain.repository.CartRepository;
import com.example.aws3tierarchitecture.domain.repository.ProdRepository;
import com.example.aws3tierarchitecture.domain.repository.UserRepository;
import com.example.aws3tierarchitecture.dto.CartDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProdRepository prodRepository;

    @Autowired
    public CartService(CartRepository cartRepository,UserRepository userRepository,ProdRepository prodRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.prodRepository = prodRepository;
    }

    public List<CartDto> getAllCartsByUserId(Long userId) {
        List<CartDto> cartDtos = new ArrayList<>();

        List<CartEntity> cartEntities = cartRepository.findByUser_Id(userId);

        for (CartEntity cartEntity : cartEntities) {
            cartDtos.add(cartEntity.toDto());
        }

        return cartDtos;
    }

    @Transactional
    public CartDto addToCart(CartDto cartDto) {
        UserEntity user = userRepository.findById(cartDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        ProdEntity product = prodRepository.findById(cartDto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));

        // 이미 장바구니에 있는 상품 확인
        CartEntity existingCartItem = cartRepository.findByUser_IdAndProduct_Id(user.getId(), product.getId());

        if (existingCartItem != null) {
            // 이미 있는 상품인 경우 count를 증가시킴
            existingCartItem.setCount(existingCartItem.getCount() + 1);
            cartRepository.save(existingCartItem);
            return CartDto.fromEntity(existingCartItem);
        } else {
            // 장바구니에 없는 상품인 경우 새로 추가
            CartEntity cartEntity = CartEntity.builder()
                    .user(user)
                    .product(product)
                    .count(1) // 수량 1로 설정
                    .build();
            CartEntity savedCartEntity = cartRepository.save(cartEntity);
            return CartDto.fromEntity(savedCartEntity);
        }
    }

    public void removeFromCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }
}