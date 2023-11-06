package com.example.aws3tierarchitecture.dto;

import com.example.aws3tierarchitecture.domain.entity.ProdEntity;
import lombok.Builder;
import lombok.Data;


@Data
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

    @Builder
    public ProdDto(Long id, String name, String manual, String image, int price) {
        this.id = id;
        this.name = name;
        this.manual = manual;
        this.image = image;
        this.price = price;
    }
}