package com.apogee.product.repositories;

import com.apogee.product.entities.CategoryClosureEntity;
import com.apogee.product.entities.CategoryClosureId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryClosureRepository extends JpaRepository<CategoryClosureEntity, CategoryClosureId> {

    @Query(name = "CategoryClosureEntity.findAllDescendants")
    List<Long> findDescendants(Long ancestorId);

    @Query(name = "CategoryClosureEntity.findAllAncestors")
    List<Long> findAncestors(Long descendantId);

    @Query(name = "CategoryClosureEntity.findMainCategories")
    List<Long> findMainCategories();

    @Query(name = "CategoryClosureEntity.findParentDepth")
    Optional<Integer> findParentDepth(Long descendantId);

    void deleteByIdAncestorId(Long ancestorId);
    void deleteByIdDescendantId(Long descendantId);
}
