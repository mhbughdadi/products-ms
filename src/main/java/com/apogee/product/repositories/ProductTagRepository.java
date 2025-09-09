package com.apogee.product.repositories;

import com.apogee.product.entities.ProductTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTagRepository extends JpaRepository<ProductTagEntity, Long> {
}
