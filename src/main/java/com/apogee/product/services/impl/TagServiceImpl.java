package com.apogee.product.services.impl;

import com.apogee.product.entities.TagEntity;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.models.Tag;
import com.apogee.product.repositories.TagRepository;
import com.apogee.product.services.TagService;
import com.apogee.product.utilities.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.apogee.product.utilities.Utilities.transformCollection;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public Tag saveTag(Tag tag) throws Exception {

        TagEntity transientTag = Mapper.map(tag, TagEntity.class);
        TagEntity savedTag = tagRepository.save(transientTag);

        return Mapper.map(savedTag, Tag.class);
    }

    @Override
    public Tag findTag(Long tagId) throws Exception {
        Optional<TagEntity> tagEntityOptional = tagRepository.findById(tagId);

        if (tagEntityOptional.isPresent()) {
            return Mapper.map(tagEntityOptional.get(), Tag.class);
        } else {
            throw new RecordNotFoundException("record.not.found", tagId);
        }
    }

    @Override
    public List<Tag> findAllTags() throws Exception {

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
    public Tag updateTag(Tag tag) throws Exception {

        if (tag.getId() == null || !tagRepository.existsById(tag.getId())) {
            throw new RecordNotFoundException("record.not.found", tag.getId());
        }

        TagEntity updatedTag = tagRepository.save(Mapper.map(tag, TagEntity.class));

        return Mapper.map(updatedTag, Tag.class);
    }

    @Override
    public Tag deleteTag(Long tagId) throws Exception {

        Optional<TagEntity> tagEntity = tagRepository.findById(tagId);
        TagEntity toBeDeletedEntity = tagEntity.orElseThrow(() -> new RecordNotFoundException("record.not.found", tagId));

        tagRepository.deleteById(tagId);

        return Mapper.map(toBeDeletedEntity, Tag.class);
    }
}
