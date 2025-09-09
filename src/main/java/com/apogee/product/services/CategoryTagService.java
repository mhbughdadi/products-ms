package com.apogee.product.services;

import com.apogee.product.models.Tag;

import java.util.List;

public interface CategoryTagService {

    Tag assignTagToCategory(Long categoryId, Long tagId) throws Exception;

    List<Tag> getTagsForCategory(Long categoryId) throws Exception;

    void removeTagFromCategory(Long categoryId, Long tagId) throws Exception;

}
