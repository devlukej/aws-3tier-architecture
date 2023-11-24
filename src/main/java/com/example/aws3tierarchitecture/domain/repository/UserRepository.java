package com.example.aws3tierarchitecture.domain.repository;

import com.example.aws3tierarchitecture.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findById(Long id);
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByNickname(String nickname);

}