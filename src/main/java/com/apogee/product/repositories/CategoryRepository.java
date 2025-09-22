package com.apogee.product.repositories;

import com.apogee.product.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    @Query("SELECT c FROM CategoryEntity c LEFT JOIN FETCH c.subCategories WHERE c.parent IS NULL")
    List<CategoryEntity> findAllRootCategoriesWithSubCategories();

    Optional<CategoryEntity> findByIdAndTagsId(Long categoryId, Long tagId);

}
