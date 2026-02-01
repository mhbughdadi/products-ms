package com.apogee.product.services;

import com.apogee.product.exceptions.DBException;
import com.apogee.product.exceptions.MapperException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.models.Category;
import com.apogee.product.models.Tag;

import java.util.List;

public interface CategoryService {

    List<Category> findAllCategories() throws MapperException;

    Category addCategory(Category product) throws MapperException, RecordNotFoundException;

    Category findCategoryByID(Long productId) throws MapperException, RecordNotFoundException;

    Category deleteCategoryById(Long productId) throws MapperException, RecordNotFoundException;

    Category updateCategory(Category map) throws MapperException;

    Category assignTagToCategory(Long categoryId, Long tagId) throws MapperException, RecordNotFoundException, DBException;

    List<Tag> getTagsForCategory(Long categoryId) throws MapperException;

    void removeTagFromCategory(Long categoryId, Long tagId) throws MapperException, DBException;
}
