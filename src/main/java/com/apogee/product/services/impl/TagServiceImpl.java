package com.apogee.product.services.impl;

import com.apogee.product.entities.TagEntity;
import com.apogee.product.exceptions.MapperException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.models.Tag;
import com.apogee.product.repositories.TagRepository;
import com.apogee.product.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.apogee.product.constants.ProductsConstant.ERROR_RECORD_NOT_FOUND;
import static com.apogee.common.mapper.ObjectMapper.transform;
import static com.apogee.common.mapper.ObjectMapper.transformCollection;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public Tag saveTag(Tag tag) throws MapperException {

        TagEntity transientTag = transform(tag, TagEntity.class);
        TagEntity savedTag = tagRepository.save(transientTag);

        return transform(savedTag, Tag.class);
    }

    @Override
    public Tag findTag(Long tagId) throws MapperException , RecordNotFoundException {
        Optional<TagEntity> tagEntityOptional = tagRepository.findById(tagId);

        if (tagEntityOptional.isPresent()) {
            return transform(tagEntityOptional.get(), Tag.class);
        } else {
            throw new RecordNotFoundException(ERROR_RECORD_NOT_FOUND, tagId);
        }
    }

    @Override
    public List<Tag> findAllTags() throws MapperException {

        List<TagEntity> tagEntities = tagRepository.findAll();
        if (!tagEntities.isEmpty()) {
            return transformCollection(tagEntities, Tag.class, (entity, model) -> {
                model.setId(entity.getId());
                return model;
            });
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Tag updateTag(Tag tag) throws MapperException , RecordNotFoundException {

        if (tag.getId() == null || !tagRepository.existsById(tag.getId())) {
            throw new RecordNotFoundException(ERROR_RECORD_NOT_FOUND, tag.getId());
        }

        TagEntity updatedTag = tagRepository.save(transform(tag, TagEntity.class));

        return transform(updatedTag, Tag.class);
    }

    @Override
    public Tag deleteTag(Long tagId) throws MapperException , RecordNotFoundException {

        Optional<TagEntity> tagEntity = tagRepository.findById(tagId);
        TagEntity toBeDeletedEntity = tagEntity.orElseThrow(() -> new RecordNotFoundException(ERROR_RECORD_NOT_FOUND, tagId));

        tagRepository.deleteById(tagId);

        return transform(toBeDeletedEntity, Tag.class);
    }
}
