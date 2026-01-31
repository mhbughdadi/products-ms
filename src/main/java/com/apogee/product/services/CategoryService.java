package com.apogee.product.services;

import com.apogee.product.models.Category;
import com.apogee.product.models.Tag;

import java.util.List;

public interface CategoryService {

    List<Category> findAllCategories() throws Exception;

    Category addCategory(Category product) throws Exception;

    Category findCategoryByID(Long productId) throws Exception;

    Category deleteCategoryById(Long productId) throws Exception;

    Category updateCategory(Category map) throws Exception;

    Category assignTagToCategory(Long categoryId, Long tagId) throws Exception;

    List<Tag> getTagsForCategory(Long categoryId) throws Exception;

    void removeTagFromCategory(Long categoryId, Long tagId) throws Exception;
}
