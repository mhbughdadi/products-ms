package com.apogee.product.services.impl;

import com.apogee.product.entities.CategoryEntity;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.mappings.Mapper;
import com.apogee.product.models.Category;
import com.apogee.product.repositories.CategoryRepository;
import com.apogee.product.services.CategoryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static com.apogee.product.utilities.Utilities.transformCollection;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    Mapper mapper;

    @Override
    public List<Category> findAllCategories() throws Exception {

        List<CategoryEntity> categoryEntities = categoryRepository.findAll();

        if (!categoryEntities.isEmpty()) {
            return transformCollection(categoryEntities, entity -> mapper.map(entity, Category.class));
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Category addCategory(Category category) throws Exception {

        CategoryEntity transientCategory = mapper.map(category, CategoryEntity.class);
        CategoryEntity savedEntity = categoryRepository.save(transientCategory);

        return mapper.map(savedEntity, Category.class);
    }

    @Override
    public Category findCategoryByID(Long categoryId) throws Exception {

        AtomicReference<Category> categoryAtomicReference = new AtomicReference<>();
        Optional<CategoryEntity> categoryEntityOptional = this.categoryRepository.findById(categoryId);

        categoryEntityOptional.ifPresent(productEntity -> {
            try {
                categoryAtomicReference.set(mapper.map(productEntity, Category.class));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return categoryAtomicReference.get();
    }

    @Override
    public Category deleteCategoryById(Long categoryId) throws Exception {

        Optional<CategoryEntity> categoryEntity = this.categoryRepository.findById(categoryId);

        CategoryEntity toBeDeletedEntity = categoryEntity.orElseThrow(() -> new RecordNotFoundException("record.not.found", categoryId));

        this.categoryRepository.deleteById(categoryId);

        return this.mapper.map(toBeDeletedEntity, Category.class);
    }

    @Override
    public Category updateCategory(Category category) throws Exception {

        CategoryEntity updatedCurrency = this.categoryRepository.save(mapper.map(category, CategoryEntity.class));

        return mapper.map(updatedCurrency, Category.class);
    }

}
