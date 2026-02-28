package com.apogee.product.repositories;

import com.apogee.product.entities.ParentImageEntity;
import com.apogee.product.entities.ParentImageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParentImageRepository extends JpaRepository<ParentImageEntity, ParentImageId> {

    List<ParentImageEntity> findAllByIdParentItemId(Long parentItemId);
    void deleteAllByIdParentItemId(Long itemId);
    boolean existsByIdParentItemId(Long parentItemId);
}
