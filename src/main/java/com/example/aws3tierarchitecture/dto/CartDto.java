package com.example.aws3tierarchitecture.dto;

import com.example.aws3tierarchitecture.domain.entity.CartEntity;
import com.example.aws3tierarchitecture.domain.entity.ProdEntity;
import com.example.aws3tierarchitecture.domain.entity.UserEntity;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CartDto {

    private Long id;
    private Long userId;
    private Long productId;
    private String productName; // 상품명을 포함한 필드

    private String productImage;

    private String productManual;
    private int price; // 상품 가격을 포함한 필드

    private int count;

    @Builder
    public CartDto(Long id, Long userId, Long productId, String productName, String productImage, String productManual, int price, int count) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.productManual = productManual;
        this.price = price;
        this.count = count;
    }

    public static CartDto fromEntity(CartEntity cartEntity) {
        return CartDto.builder()
                .id(cartEntity.getId())
                .userId(cartEntity.getUser().getId())
                .productId(cartEntity.getProduct().getId())
                .productName(cartEntity.getProduct().getName())
                .productImage(cartEntity.getProduct().getImage())
                .productManual(cartEntity.getProduct().getManual())
                .price(cartEntity.getProduct().getPrice())
                .count(cartEntity.getCount())
                .build();
    }

}