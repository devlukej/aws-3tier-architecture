package com.example.aws3tierarchitecture.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Data
@Table(name = "user")
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private int money;
    private LocalDateTime createdAt;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<CartEntity> carts;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now(); // 엔티티가 저장되기 전에 생성 날짜 설정
    }

    @Builder
    public UserEntity(Long id, String username, String password, String nickname, LocalDateTime createdAt, int money) {
        this.id = id;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.money = money;
        this.createdAt = createdAt;
    }

}