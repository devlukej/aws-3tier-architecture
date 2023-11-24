package com.example.aws3tierarchitecture.domain.entity;

import com.example.aws3tierarchitecture.dto.CartDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Data
@Table(name = "cart")
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "prod_id")
    private ProdEntity product;

    private int count;

    @Builder
    public CartEntity(Long id, UserEntity user, ProdEntity product,int count) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.count = count;
    }

    public CartDto toDto() {
        return CartDto.builder()
                .id(this.id)
                .userId(this.user.getId()) // UserEntity에서 사용자 ID 추출
                .productId(this.product.getId()) // ProdEntity에서 상품 ID 추출
                .productName(this.product.getName())
                .productImage(this.product.getImage())
                .productManual(this.product.getManual())
                .price(this.product.getPrice())
                .count(this.count)
                .build();
    }

    @Override
    public String toString() {
        return "CartEntity{" +
                "id=" + id +
                ", count=" + count +
                ", product=" + product + // Assuming there is a 'ProductEntity' field in CartEntity
                ", user=" + user +
                '}';
    }


}