package com.example.aws3tierarchitecture.domain.repository;

import com.example.aws3tierarchitecture.domain.entity.CartEntity;
import com.example.aws3tierarchitecture.domain.entity.ProdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {

    List<CartEntity> findByUser_Id(Long userId);

    CartEntity findByUser_IdAndProduct_Id(Long userId, Long productId);
}