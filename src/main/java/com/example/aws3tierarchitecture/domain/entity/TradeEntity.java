package com.example.aws3tierarchitecture.domain.entity;

import com.example.aws3tierarchitecture.dto.TradeDto;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Data
@Table(name = "trade")
public class TradeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "total_price")
    private int totalPrice;


    @Builder
    public TradeEntity(UserEntity user, int totalPrice) {
        this.user = user;
        this.totalPrice = totalPrice;
    }

    public TradeDto toDto() {
        return TradeDto.builder()
                .id(this.id)
                .userId(this.user.getId()) // UserEntity에서 사용자 ID 추출
                .totalPrice(this.totalPrice)
                .build();
    }


}