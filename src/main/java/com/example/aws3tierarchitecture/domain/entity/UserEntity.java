package com.example.aws3tierarchitecture.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    private String state;
    private int money;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<CartEntity> carts;

    @Builder
    public UserEntity(Long id, String username, String password, String nickname, String state, int money) {
        this.id = id;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.username = username;
        this.nickname = nickname;
        this.state = state;
        this.money = money;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", money=" + money +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", state'" + state + '\'' +
                '}';
    }

}