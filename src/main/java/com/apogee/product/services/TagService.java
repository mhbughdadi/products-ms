package com.apogee.product.services;

import com.apogee.product.models.Tag;
import java.util.List;

public interface TagService {

    Tag saveTag(Tag tag) throws Exception;

    Tag findTag(Long tagId) throws Exception;

    List<Tag> findAllTags() throws Exception;

    Tag updateTag(Tag tag) throws Exception;

    Tag deleteTag(Long tagId) throws Exception;

}
