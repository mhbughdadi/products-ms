package com.apogee.product.repositories;

import com.apogee.product.entities.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {

    List<TagEntity> findByItemsId(Long parentItemId);

}
