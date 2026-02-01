package com.apogee.product.services;

import com.apogee.product.exceptions.DBException;
import com.apogee.product.exceptions.MapperException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.models.Category;
import com.apogee.product.models.Tag;

import java.util.List;

public interface CategoryService {

    /**
     * Return hierarchical root categories with their sub-categories
     * @return list of root categories
     * @throws MapperException mapping failure
     */
    List<Category> findAllCategories() throws MapperException;

    /**
     * Add a category (optionally with parent)
     * @param product category model
     * @return saved category
     * @throws MapperException mapping failure
     * @throws RecordNotFoundException when specified parent not found
     */
    Category addCategory(Category product) throws MapperException, RecordNotFoundException;

    /**
     * Find a category by id
     * @param productId id
     * @return category
     * @throws MapperException mapping failure
     * @throws RecordNotFoundException when not found
     */
    Category findCategoryByID(Long productId) throws MapperException, RecordNotFoundException;

    /**
     * Delete a category
     * @param productId id
     * @return deleted category
     * @throws MapperException mapping failure
     * @throws RecordNotFoundException when not found
     */
    Category deleteCategoryById(Long productId) throws MapperException, RecordNotFoundException;

    /**
     * Update a category
     * @param map category model
     * @return updated category
     * @throws MapperException mapping failure
     */
    Category updateCategory(Category map) throws MapperException;

    /**
     * Assign a tag to a category
     * @param categoryId id
     * @param tagId id
     * @return updated category
     * @throws MapperException mapping failure
     * @throws RecordNotFoundException when category or tag not found
     * @throws DBException when assignment already exists
     */
    Category assignTagToCategory(Long categoryId, Long tagId) throws MapperException, RecordNotFoundException, DBException;

    /**
     * Get tags for category
     * @param categoryId id
     * @return list of tags
     * @throws MapperException mapping failure
     */
    List<Tag> getTagsForCategory(Long categoryId) throws MapperException;

    /**
     * Remove a tag from a category
     * @param categoryId id
     * @param tagId id
     * @throws MapperException mapping failure
     * @throws DBException when assignment not found
     */
    void removeTagFromCategory(Long categoryId, Long tagId) throws MapperException, DBException;
}
