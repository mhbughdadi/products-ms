package com.apogee.product.services;

import com.apogee.product.models.Category;

import java.util.List;

public interface CategoryService {

    List<Category> findAllCategories() throws Exception;

    Category addCategory(Category product) throws Exception;

    Category findCategoryByID(Long productId) throws Exception;

    Category deleteCategoryById(Long productId) throws Exception;

    Category updateCategory(Category map) throws Exception;
}
