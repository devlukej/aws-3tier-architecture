package com.example.aws3tierarchitecture.service;

import com.example.aws3tierarchitecture.domain.entity.ProdEntity;
import com.example.aws3tierarchitecture.domain.repository.ProdRepository;
import com.example.aws3tierarchitecture.dto.ProdDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProdService {

    private final ProdRepository prodRepository;

    @Autowired
    public ProdService(ProdRepository prodRepository) {
        this.prodRepository = prodRepository;
    }

    public List<ProdDto> getAllProds() {
        List<ProdEntity> products = prodRepository.findAll();
        List<ProdDto> productDtos = new ArrayList<>();

        for (ProdEntity product : products) {
            ProdDto productDto = ProdDto.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .manual(product.getManual())
                    .price(product.getPrice())
                    .image(product.getImage())
                    .build();
            productDtos.add(productDto);
        }

        return productDtos;
    }

    @Transactional
    public List<ProdDto> searchName(String name) {

        List<ProdEntity> prodEntities = prodRepository.findByNameContaining(name);

        List<ProdDto> productDtos = new ArrayList<>();

        for (ProdEntity prodEntitie : prodEntities) {
            ProdDto productDto = ProdDto.builder()
                    .id(prodEntitie.getId())
                    .name(prodEntitie.getName())
                    .manual(prodEntitie.getManual())
                    .price(prodEntitie.getPrice())
                    .image(prodEntitie.getImage())
                    .build();
            productDtos.add(productDto);
        }

        return productDtos;
    }
}