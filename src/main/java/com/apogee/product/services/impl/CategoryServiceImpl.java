package com.apogee.product.services.impl;

import com.apogee.product.entities.CategoryEntity;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.utilities.Mapper;
import com.apogee.product.models.Category;
import com.apogee.product.repositories.CategoryRepository;
import com.apogee.product.services.CategoryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.apogee.product.utilities.Utilities.transformCollection;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public List<Category> findAllCategories() throws Exception {

        List<CategoryEntity> categoryEntities = categoryRepository.findAllRootCategoriesWithSubCategories();

        if (!categoryEntities.isEmpty()) {
            return transformCollection(categoryEntities, Category.class, ((categoryEntity, category) -> {
                category = this.addCategoryIdAndParentId(categoryEntity, category);

                category.setSubCategories(transformCollection(categoryEntity.getSubCategories(), Category.class, this::addCategoryIdAndParentId));

                return category;
            }));
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Category addCategory(Category category) throws Exception {

        CategoryEntity transientCategory = Mapper.map(category, CategoryEntity.class);

        CategoryEntity savedCategory = categoryRepository.save(transientCategory);

        if (category.getParentId() != null) {
            CategoryEntity parentCategory = this.categoryRepository.findById(category.getParentId()).orElseThrow(() -> new RecordNotFoundException("record.not.found", category.getParentId()));
            savedCategory.setParent(parentCategory);
        }
        this.categoryRepository.save(savedCategory);

        return Mapper.map(savedCategory, Category.class);
    }

    @Override
    public Category findCategoryByID(Long categoryId) throws Exception {

        Optional<CategoryEntity> categoryEntityOptional = this.categoryRepository.findById(categoryId);
        if (categoryEntityOptional.isPresent()) {
            return Mapper.map(categoryEntityOptional.get(), Category.class);
        } else {
            throw new RecordNotFoundException("record.not.found", categoryId);
        }
    }

    @Override
    public Category deleteCategoryById(Long categoryId) throws Exception {

        Optional<CategoryEntity> categoryEntity = this.categoryRepository.findById(categoryId);

        CategoryEntity toBeDeletedEntity = categoryEntity.orElseThrow(() -> new RecordNotFoundException("record.not.found", categoryId));

        this.categoryRepository.deleteById(categoryId);

        return Mapper.map(toBeDeletedEntity, Category.class);
    }

    @Override
    public Category updateCategory(Category category) throws Exception {

        CategoryEntity updatedCurrency = this.categoryRepository.save(Mapper.map(category, CategoryEntity.class));

        return Mapper.map(updatedCurrency, Category.class);
    }

    private Category addCategoryIdAndParentId(CategoryEntity categoryEntity, Category category) {

        if (categoryEntity != null && category != null) {
            category.setId(categoryEntity.getId());
            category.setParentId(categoryEntity.getParent() != null ? categoryEntity.getParent().getId() : null);
        }

        return category;
    }

}
