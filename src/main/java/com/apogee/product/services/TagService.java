package com.apogee.product.services;

import com.apogee.product.exceptions.MapperException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.models.Tag;
import java.util.List;

public interface TagService {

    Tag saveTag(Tag tag) throws MapperException;

    Tag findTag(Long tagId) throws MapperException, RecordNotFoundException;

    List<Tag> findAllTags() throws MapperException;

    Tag updateTag(Tag tag) throws MapperException, RecordNotFoundException;

    Tag deleteTag(Long tagId) throws MapperException, RecordNotFoundException;

}
