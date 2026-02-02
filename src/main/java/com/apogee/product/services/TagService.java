package com.apogee.product.services;

import com.apogee.product.exceptions.MapperException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.models.Tag;
import java.util.List;

public interface TagService {

    /**
     * Persist a new Tag
     * @param tag input model
     * @return saved Tag model
     * @throws MapperException if mapping between model and entity fails
     */
    Tag saveTag(Tag tag) throws MapperException;

    /**
     * Find a tag by id
     * @param tagId target id
     * @return Tag model
     * @throws MapperException mapping failure
     * @throws RecordNotFoundException when the tag is not found
     */
    Tag findTag(Long tagId) throws MapperException, RecordNotFoundException;

    /**
     * Retrieve all tags
     * @return list of Tag models (may be empty)
     * @throws MapperException mapping failure
     */
    List<Tag> findAllTags() throws MapperException;

    /**
     * Update an existing tag
     * @param tag updated model (must contain id)
     * @return updated Tag model
     * @throws MapperException mapping failure
     * @throws RecordNotFoundException when the tag to update does not exist
     */
    Tag updateTag(Tag tag) throws MapperException, RecordNotFoundException;

    /**
     * Delete a tag
     * @param tagId id to delete
     * @return the deleted Tag model
     * @throws MapperException mapping failure
     * @throws RecordNotFoundException when the tag to delete does not exist
     */
    Tag deleteTag(Long tagId) throws MapperException, RecordNotFoundException;

}
