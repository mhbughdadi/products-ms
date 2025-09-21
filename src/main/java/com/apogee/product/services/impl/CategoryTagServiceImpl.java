package com.apogee.product.services.impl;

import com.apogee.product.entities.CategoryEntity;
import com.apogee.product.entities.CategoryTagEntity;
import com.apogee.product.entities.TagEntity;
import com.apogee.product.exceptions.DBException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.models.Tag;
import com.apogee.product.repositories.CategoryRepository;
import com.apogee.product.repositories.CategoryTagRepository;
import com.apogee.product.repositories.TagRepository;
import com.apogee.product.services.CategoryTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.apogee.product.utilities.Utilities.transform;
import static com.apogee.product.utilities.Utilities.transformCollection;

@Service
public class CategoryTagServiceImpl implements CategoryTagService {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryTagRepository categoryTagRepository;


    @Override
    public Tag assignTagToCategory(Long categoryId, Long tagId) throws Exception {

        categoryTagRepository.findByCategoryIdAndTagId(categoryId, tagId).ifPresent(existingAssignment -> {
            throw new DBException("errors.duplicate.record",CategoryTagEntity.class, categoryId,tagId);
        });

        TagEntity tag = tagRepository.findById(tagId).orElseThrow(() -> new RecordNotFoundException("Tag not found", tagId));
        CategoryEntity category = categoryRepository.findById(categoryId).orElseThrow(() -> new RecordNotFoundException("Product not found", categoryId));

        CategoryTagEntity tagAssignment = new CategoryTagEntity();
        tagAssignment.setTag(tag);
        tagAssignment.setCategory(category);
        CategoryTagEntity savedCategoryTag = categoryTagRepository.save(tagAssignment);

        return transform(savedCategoryTag, Tag.class, this::getTag);
    }

    @Override
    public List<Tag> getTagsForCategory(Long categoryId) throws Exception {

        List<CategoryTagEntity> assignments = categoryTagRepository.findByCategoryId(categoryId);

        return transformCollection(assignments, Tag.class, this::getTag);
    }



    @Override
    public void removeTagFromCategory(Long categoryId, Long tagId) throws Exception {
        List<CategoryTagEntity> assignments = categoryTagRepository.findByCategoryId(categoryId);
        CategoryTagEntity assignmentToRemove = assignments.stream()
                .filter(a -> a.getTag().getId().equals(tagId))
                .findFirst()
                .orElseThrow(() -> new RecordNotFoundException(
                        "Tag assignment not found for categoryId: " + categoryId + ", tagId: " + tagId, tagId));
        categoryTagRepository.delete(assignmentToRemove);
    }

    private Tag getTag(CategoryTagEntity entity, Tag model) {

        model.setId(entity.getTag().getId());
        model.setName(entity.getTag().getName());
        model.setDescription(entity.getTag().getDescription());
        model.setDescriptionAr(entity.getTag().getDescriptionAr());
        return model;
    }
}
