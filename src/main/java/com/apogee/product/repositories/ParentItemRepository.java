package com.apogee.product.repositories;

import com.apogee.product.entities.ParentItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentItemRepository extends JpaRepository<ParentItemEntity, Long> {
}