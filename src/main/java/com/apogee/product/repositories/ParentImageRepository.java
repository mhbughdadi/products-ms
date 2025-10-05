package com.apogee.product.repositories;

import com.apogee.product.entities.ParentImageEntity;
import com.apogee.product.entities.ParentImageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParentImageRepository extends JpaRepository<ParentImageEntity, ParentImageId> {

    List<ParentImageEntity> findAllById_ParentItemId(Long parentItemId);
    void deleteAllById_ParentItemId(Long itemId);
    boolean existsById_ParentItemId(Long parentItemId);
}
