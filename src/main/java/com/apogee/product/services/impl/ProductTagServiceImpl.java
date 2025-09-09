package com.apogee.product.services.impl;

import com.apogee.product.models.Tag;
import com.apogee.product.services.ProductTagService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductTagServiceImpl implements ProductTagService {
    @Override
    public Tag assignTagToProduct(Long productId, Long tagId) throws Exception {
        return null;
    }

    @Override
    public List<Tag> getTagsForProduct(Long productId) throws Exception {
        return List.of();
    }

    @Override
    public void removeTagFromProduct(Long productId, Long tagId) throws Exception {

    }
}
