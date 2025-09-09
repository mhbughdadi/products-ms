package com.apogee.product.services.impl;

import com.apogee.product.models.Tag;
import com.apogee.product.services.CategoryTagService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryTagServiceImpl implements CategoryTagService {
    @Override
    public Tag assignTagToCategory(Long categoryId, Long tagId) throws Exception {
        return null;
    }

    @Override
    public List<Tag> getTagsForCategory(Long categoryId) throws Exception {
        return List.of();
    }

    @Override
    public void removeTagFromCategory(Long categoryId, Long tagId) throws Exception {

    }
}
