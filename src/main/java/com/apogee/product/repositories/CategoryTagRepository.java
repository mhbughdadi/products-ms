package com.apogee.product.repositories;

import com.apogee.product.entities.CategoryTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryTagRepository extends JpaRepository<CategoryTagEntity, Long> {

    List<CategoryTagEntity> findByCategoryId(Long categoryId);

    Optional<CategoryTagEntity> findByCategoryIdAndTagId(Long categoryId, Long tagId);
}
