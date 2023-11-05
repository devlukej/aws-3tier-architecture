package com.example.aws3tierarchitecture.dto;

import com.example.aws3tierarchitecture.domain.entity.ProdEntity;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ProdDto {

    private Long id;
    private String name;
    private String manual;
    private int price;
    private String image;


    public ProdEntity toEntity() {
        return ProdEntity.builder()
                .id(id)
                .name(name)
                .manual(manual)
                .price(price)
                .image(image)
                .build();
    }
}