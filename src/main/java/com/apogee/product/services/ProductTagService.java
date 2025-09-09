package com.apogee.product.services;

import com.apogee.product.models.Tag;

import java.util.List;

public interface ProductTagService {

    Tag assignTagToProduct(Long productId, Long tagId) throws Exception;

    List<Tag> getTagsForProduct(Long productId) throws Exception;

    void removeTagFromProduct(Long productId, Long tagId) throws Exception;

}
