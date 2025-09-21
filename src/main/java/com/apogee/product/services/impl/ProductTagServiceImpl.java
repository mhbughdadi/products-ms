package com.apogee.product.services.impl;

import com.apogee.product.entities.CategoryTagEntity;
import com.apogee.product.entities.ProductEntity;
import com.apogee.product.entities.ProductTagEntity;
import com.apogee.product.entities.TagEntity;
import com.apogee.product.exceptions.DBException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.models.Tag;
import com.apogee.product.repositories.ProductRepository;
import com.apogee.product.repositories.ProductTagRepository;
import com.apogee.product.repositories.TagRepository;
import com.apogee.product.services.ProductTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.apogee.product.utilities.Utilities.transform;
import static com.apogee.product.utilities.Utilities.transformCollection;

@Service
public class ProductTagServiceImpl implements ProductTagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductTagRepository productTagRepository;

    @Override
    public Tag assignTagToProduct(Long productId, Long tagId) throws Exception {

        productTagRepository.findByProductIdAndTagId(productId, tagId).ifPresent(existingAssignment -> {
            throw new DBException("errors.duplicate.record", CategoryTagEntity.class, productId,tagId);
        });

        TagEntity tag = tagRepository.findById(tagId).orElseThrow(() -> new RecordNotFoundException("Tag not found", tagId));
        ProductEntity product = productRepository.findById(productId).orElseThrow(() -> new RecordNotFoundException("Product not found", productId));

        ProductTagEntity tagAssignment = new ProductTagEntity();
        tagAssignment.setTag(tag);
        tagAssignment.setProduct(product);

        ProductTagEntity savedProductTag = productTagRepository.save(tagAssignment);

        return transform(savedProductTag, Tag.class, this::getTag);
    }

    @Override
    public List<Tag> getTagsForProduct(Long productId) throws Exception {

        List<ProductTagEntity> assignments = productTagRepository.findByProductId(productId);

        return transformCollection(assignments, Tag.class, this::getTag);
    }

    @Override
    public void removeTagFromProduct(Long productId, Long tagId) throws Exception {

        List<ProductTagEntity> assignments = productTagRepository.findByProductId(productId);
        ProductTagEntity assignmentToRemove =
                assignments.stream()
                        .filter(a -> a.getTag().getId().equals(tagId))
                        .findFirst()
                        .orElseThrow(() -> new DBException("errors.products.tags.not.found", ProductTagEntity.class, productId, tagId));

        productTagRepository.delete(assignmentToRemove);
    }

    private Tag getTag(ProductTagEntity entity, Tag model) {

        model.setId(entity.getTag().getId());
        model.setName(entity.getTag().getName());
        model.setDescription(entity.getTag().getDescription());
        model.setDescriptionAr(entity.getTag().getDescriptionAr());
        return model;
    }
}
