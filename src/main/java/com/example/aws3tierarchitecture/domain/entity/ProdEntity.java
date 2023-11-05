package com.example.aws3tierarchitecture.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Data
@Table(name = "prod")
public class ProdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String manual;
    private int price;
    private String image;

    @Builder
    public ProdEntity(Long id, String name, String manual, int price, String image) {
        this.id = id;
        this.name = name;
        this.manual = manual;
        this.price = price;
        this.image = image;
    }

}