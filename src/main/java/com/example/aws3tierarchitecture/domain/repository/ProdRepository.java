package com.example.aws3tierarchitecture.domain.repository;

import com.example.aws3tierarchitecture.domain.entity.ProdEntity;
import com.example.aws3tierarchitecture.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProdRepository extends JpaRepository<ProdEntity, Long> {

}