package com.apogee.product.services.impl;

import com.apogee.product.entities.CategoryEntity;
import com.apogee.product.entities.TagEntity;
import com.apogee.product.exceptions.DBException;
import com.apogee.product.exceptions.MapperException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.models.Tag;
import com.apogee.product.repositories.TagRepository;
import com.apogee.product.utilities.Mapper;
import com.apogee.product.models.Category;
import com.apogee.product.repositories.CategoryRepository;
import com.apogee.product.services.CategoryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.apogee.product.constants.ProductsConstant.ERROR_CATEGORY_TAG_ALREADY_EXISTS;
import static com.apogee.product.constants.ProductsConstant.ERROR_CATEGORY_TAG_NOT_FOUND;
import static com.apogee.product.constants.ProductsConstant.ERROR_RECORD_NOT_FOUND;
import static com.apogee.product.utilities.Utilities.transform;
import static com.apogee.product.utilities.Utilities.transformCollection;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TagRepository tagRepository;


    @Override
    public List<Category> findAllCategories() throws MapperException {

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
    public Category addCategory(Category category) throws MapperException, RecordNotFoundException {

        CategoryEntity transientCategory = transform(category, CategoryEntity.class);

        if (category.getParentId() != null) {
            CategoryEntity parentCategory = this.categoryRepository.findById(category.getParentId()).orElseThrow(() -> new RecordNotFoundException(ERROR_RECORD_NOT_FOUND, category.getParentId()));
            transientCategory.setParent(parentCategory);
        }
        CategoryEntity savedCategory = categoryRepository.save(transientCategory);

        return transform(savedCategory, Category.class, this::getCategory);
    }

    @Override
    public Category findCategoryByID(Long categoryId) throws MapperException, RecordNotFoundException {

        Optional<CategoryEntity> categoryEntityOptional = this.categoryRepository.findById(categoryId);
        if (categoryEntityOptional.isPresent()) {
            return transform(categoryEntityOptional.get(), Category.class, this::getCategory);
        } else {
            throw new RecordNotFoundException(ERROR_RECORD_NOT_FOUND, categoryId);
        }
    }

    @Override
    public Category deleteCategoryById(Long categoryId) throws MapperException, RecordNotFoundException {

        Optional<CategoryEntity> categoryEntity = this.categoryRepository.findById(categoryId);

        CategoryEntity toBeDeletedEntity = categoryEntity.orElseThrow(() -> new RecordNotFoundException(ERROR_RECORD_NOT_FOUND, categoryId));

        this.categoryRepository.deleteById(categoryId);

        return transform(toBeDeletedEntity, Category.class, this::getCategory);
    }

    @Override
    public Category updateCategory(Category category) throws MapperException {

        CategoryEntity updatedCurrency = this.categoryRepository.save(transform(category, CategoryEntity.class));

        return transform(updatedCurrency, Category.class, this::getCategory);
    }

    private Category addCategoryIdAndParentId(CategoryEntity categoryEntity, Category category) throws MapperException {

        if (categoryEntity != null && category != null) {
            category.setId(categoryEntity.getId());
            category.setParentId(categoryEntity.getParent() != null ? categoryEntity.getParent().getId() : null);
            category.setTags(transformCollection(categoryEntity.getTags(), Tag.class, this::getTag));
        }

        return category;
    }


    @Override
    public Category assignTagToCategory(Long categoryId, Long tagId) throws MapperException, RecordNotFoundException, DBException {

        categoryRepository.findByIdAndTagsId(categoryId, tagId).ifPresent(existingAssignment -> {
            throw new DBException(ERROR_CATEGORY_TAG_ALREADY_EXISTS, CategoryEntity.class, categoryId, tagId);
        });

        TagEntity tag = tagRepository.findById(tagId).orElseThrow(() -> new RecordNotFoundException(ERROR_RECORD_NOT_FOUND, tagId));
        CategoryEntity category = categoryRepository.findById(categoryId).orElseThrow(() -> new RecordNotFoundException(ERROR_RECORD_NOT_FOUND, categoryId));

        category.getTags().add(tag);
        category = categoryRepository.save(category);

        return transform(category, Category.class, this::getCategory);
    }

    @Override
    public List<Tag> getTagsForCategory(Long categoryId) throws MapperException {

        List<TagEntity> assignments = tagRepository.findByItemsId(categoryId);

        return transformCollection(assignments, Tag.class, this::getTag);
    }


    @Override
    public void removeTagFromCategory(Long categoryId, Long tagId) throws MapperException, DBException {

        CategoryEntity found = categoryRepository.findByIdAndTagsId(categoryId, tagId)
                .orElseThrow(
                        () -> new DBException(ERROR_CATEGORY_TAG_NOT_FOUND, CategoryEntity.class, categoryId, tagId)
                );

        found.setTags(found.getTags().stream()
                .filter(t -> !t.getId().equals(tagId))
                .collect(Collectors.toCollection(ArrayList::new)));

        categoryRepository.save(found);
    }

    private Category getCategory(CategoryEntity entity, Category model) throws MapperException {

        model.setId(entity.getId());
        model.setTags(transformCollection(entity.getTags(), Tag.class, this::getTag));
        if (entity.getParent() != null) {
            model.setParentId(entity.getParent().getId());
        }
        return model;
    }

    private Tag getTag(TagEntity tagEntity, Tag tag) {
        tag.setId(tagEntity.getId());
        tag.setName(tagEntity.getName());
        tag.setDescription(tagEntity.getDescription());
        tag.setDescriptionAr(tagEntity.getDescriptionAr());
        return tag;
    }

}
