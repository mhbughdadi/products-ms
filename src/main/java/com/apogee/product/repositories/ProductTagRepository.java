package com.apogee.product.repositories;

import com.apogee.product.entities.ProductTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductTagRepository extends JpaRepository<ProductTagEntity, Long> {

    List<ProductTagEntity> findByProductId(Long productId);

    Optional<ProductTagEntity> findByProductIdAndTagId(Long productId, Long tagId);
}
