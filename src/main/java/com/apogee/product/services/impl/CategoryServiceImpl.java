package com.apogee.product.services.impl;

import com.apogee.product.entities.CategoryClosureEntity;
import com.apogee.product.entities.CategoryClosureId;
import com.apogee.product.entities.CategoryEntity;
import com.apogee.product.entities.TagEntity;
import com.apogee.product.exceptions.DBException;
import com.apogee.product.exceptions.MapperException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.models.Tag;
import com.apogee.product.repositories.CategoryClosureRepository;
import com.apogee.product.repositories.TagRepository;
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

    @Autowired
    private CategoryClosureRepository categoryClosureRepository;


    @Override
    public List<Category> findAllCategories() throws MapperException {

        List<Long> mainCategoriesIds = this.categoryClosureRepository.findMainCategories();
        List<CategoryEntity> categoryEntities = categoryRepository.findAllById(mainCategoriesIds);

        if (!categoryEntities.isEmpty()) {
            return transformCollection(categoryEntities, Category.class, ((categoryEntity, category) -> {
                category = this.addCategoryId(categoryEntity, category);

                category.setSubCategories(this.findDescendants(category.getId()));
                return category;
            }));
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Category> findDescendants(Long ancestorId) throws MapperException {
        List<Long> ancestorsIds = categoryClosureRepository.findDescendants(ancestorId);


        if (!ancestorsIds.isEmpty()) {
            List<CategoryEntity> categoryEntities = categoryRepository.findAllById(ancestorsIds);

            return transformCollection(categoryEntities, Category.class, this::getCategory);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Category> findAncestors() throws MapperException {
        List<Long> descendantsIds = categoryClosureRepository.findDescendants(1L);

        if (!descendantsIds.isEmpty()) {
            List<CategoryEntity> categoryEntities = categoryRepository.findAllById(descendantsIds);

            return transformCollection(categoryEntities, Category.class, this::getCategory);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Category addCategory(Category category) throws MapperException, RecordNotFoundException {

        CategoryEntity transientCategory = transform(category, CategoryEntity.class);

        if (category.getParentId() != null) {
            if (!this.categoryRepository.existsById(category.getParentId())) {
                throw new RecordNotFoundException(ERROR_RECORD_NOT_FOUND, category.getParentId());
            }
        }
        CategoryEntity savedCategory = categoryRepository.save(transientCategory);
        if(savedCategory.getParentId() != null) {
           Optional<Integer> parentDepthOptional = this.categoryClosureRepository.findParentDepth(savedCategory.getParentId());
            parentDepthOptional.ifPresent(parentDepth -> saveCategoryClosure(savedCategory.getParentId(), savedCategory.getId(), parentDepth + 1));
        }else{
            saveCategoryClosure(savedCategory.getId(), savedCategory.getId(), 0);
        }
        return transform(savedCategory, Category.class, this::getCategory);
    }

    @Override
    public Category findCategoryByID(Long categoryId) throws MapperException, RecordNotFoundException {

        Optional<CategoryEntity> categoryEntityOptional = this.categoryRepository.findById(categoryId);

        if (categoryEntityOptional.isPresent()) {

            Category category = transform(categoryEntityOptional.get(), Category.class, this::getCategory);
            category.setSubCategories(this.findDescendants(categoryId));

            return category;
        } else {

            throw new RecordNotFoundException(ERROR_RECORD_NOT_FOUND, categoryId);
        }
    }

    @Override
    public Category deleteCategoryById(Long categoryId) throws MapperException, RecordNotFoundException {

        Optional<CategoryEntity> categoryEntity = this.categoryRepository.findById(categoryId);

        CategoryEntity toBeDeletedEntity = categoryEntity.orElseThrow(() -> new RecordNotFoundException(ERROR_RECORD_NOT_FOUND, categoryId));

        this.categoryClosureRepository.deleteByIdDescendantId(toBeDeletedEntity.getId());
        this.categoryClosureRepository.deleteByIdAncestorId(toBeDeletedEntity.getId());
        this.categoryRepository.deleteById(categoryId);

        return transform(toBeDeletedEntity, Category.class, this::getCategory);
    }

    @Override
    public Category updateCategory(Category category) throws MapperException {

        Optional<CategoryEntity> existingCategoryEntityOptional = this.categoryRepository.findById(category.getId());
        existingCategoryEntityOptional.ifPresent( existingCategory -> {
            if(category.getParentId() == null ){
                saveCategoryClosure(category.getId(), category.getId(), 0);
            }else if(!category.getParentId().equals(existingCategory.getParentId())){
                this.categoryClosureRepository.deleteByIdDescendantId(category.getId());
                saveCategoryClosure(category.getParentId(), category.getId(), 1);
            }
        });
        CategoryEntity updatedCategory = this.categoryRepository.save(transform(category, CategoryEntity.class));

        return transform(updatedCategory, Category.class, this::getCategory);
    }

    private Category addCategoryId(CategoryEntity categoryEntity, Category category) throws MapperException {

        if (categoryEntity != null && category != null) {
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

//        model.setId(entity.getId());
        model.setTags(transformCollection(entity.getTags(), Tag.class, this::getTag));

        return model;
    }

    private Tag getTag(TagEntity tagEntity, Tag tag) {
        tag.setId(tagEntity.getId());
        tag.setName(tagEntity.getName());
        tag.setDescription(tagEntity.getDescription());
        tag.setDescriptionAr(tagEntity.getDescriptionAr());
        return tag;
    }

    private void saveCategoryClosure(Long ancestorId, Long descendantId, int depth) {

        CategoryClosureEntity closureEntity = new CategoryClosureEntity();
        CategoryClosureId closureId = new CategoryClosureId();

        closureId.setAncestorId(ancestorId);
        closureId.setDescendantId(descendantId);

        closureEntity.setId(closureId);
        closureEntity.setDepth(depth);

        categoryClosureRepository.save(closureEntity);
    }

}
