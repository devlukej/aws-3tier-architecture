package com.example.aws3tierarchitecture.dto;

import com.example.aws3tierarchitecture.domain.entity.TradeEntity;
import com.example.aws3tierarchitecture.domain.entity.TradeEntity;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class TradeDto {

    private Long id;
    private Long userId;
    private int totalPrice;


    @Builder
    public TradeDto(Long id, Long userId, int totalPrice) {
        this.id = id;
        this.userId = userId;
        this.totalPrice = totalPrice;
    }

    public static TradeDtoBuilder builder() {
        return new TradeDtoBuilder();
    }

    public static class TradeDtoBuilder {
        private Long id;
        private Long userId;
        private int totalPrice; // totalPrice 필드를 추가합니다.

        TradeDtoBuilder() {
        }

        public TradeDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TradeDtoBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public TradeDtoBuilder totalPrice(int totalPrice) {
            this.totalPrice = totalPrice; // totalPrice 필드를 설정합니다.
            return this;
        }

        public TradeDto build() {
            return new TradeDto(id, userId, totalPrice); // totalPrice 필드를 사용하여 객체를 생성합니다.
        }
    }

}