package com.apogee.product.repositories;

import com.apogee.product.entities.SkuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkuRepository extends JpaRepository<SkuEntity, Long> {

    Optional<SkuEntity> findByIdAndTagsId(Long skuId, Long tagId);

    Optional<SkuEntity> findByIdAndBenefitsId(Long skuId, Long benefitId);

    List<SkuEntity> findByProductId(Long skuId);
}
