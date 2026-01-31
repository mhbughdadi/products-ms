package com.apogee.product.repositories;

import com.apogee.product.entities.BenefitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BenefitRepository extends JpaRepository<BenefitEntity, Long> {

    Optional<BenefitEntity> findByIdAndSkuId(Long productId, Long tagId);

    List<BenefitEntity> findBySkuId(Long productId);

    void deleteBySkuId(Long skuId);
}
