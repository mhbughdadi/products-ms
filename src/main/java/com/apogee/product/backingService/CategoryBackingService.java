package com.apogee.product.backingService;

import com.apogee.product.dtos.inputs.CategoryDto;
import com.apogee.product.dtos.output.AllCategoriesResponseDto;
import com.apogee.product.dtos.output.CategoryResponseDto;
import com.apogee.product.mappings.Mapper;
import com.apogee.product.models.Category;
import com.apogee.product.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.apogee.product.utilities.Utilities.transformCollection;

@Service
public class CategoryBackingService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private Mapper mapper;

    public CategoryResponseDto addCategory(CategoryDto categoryDto) throws Exception {

        CategoryResponseDto response = new CategoryResponseDto();

        Category savedCurrency = this.categoryService.addCategory(this.mapper.map(categoryDto, Category.class));

        response.setCategory(this.mapper.map(savedCurrency, CategoryDto.class));

        return response;
    }

    public CategoryResponseDto updateCategory(CategoryDto categoryDto) throws Exception {

        CategoryResponseDto response = new CategoryResponseDto();

        Category savedCurrency = this.categoryService.updateCategory(this.mapper.map(categoryDto, Category.class));

        response.setCategory(this.mapper.map(savedCurrency, CategoryDto.class));

        return response;
    }

    public CategoryResponseDto deleteCategoryById(Long categoryId) throws Exception {

        CategoryResponseDto response = new CategoryResponseDto();

        Category deletedCategory = this.categoryService.deleteCategoryById(categoryId);

        response.setCategory(this.mapper.map(deletedCategory, CategoryDto.class));

        return response;
    }

    public AllCategoriesResponseDto getAllCategories() throws Exception {

        AllCategoriesResponseDto response = new AllCategoriesResponseDto();

        List<Category> allCategories = this.categoryService.findAllCategories();
        List<CategoryDto> allCategoriesDtos = transformCollection(allCategories, (currency) -> this.mapper.map(currency, CategoryDto.class));

        response.setCategories(allCategoriesDtos);

        return response;
    }

    public CategoryResponseDto getCategoryById(Long categoryId) throws Exception {

        CategoryResponseDto response = new CategoryResponseDto();

        Category category = this.categoryService.findCategoryByID(categoryId);

        response.setCategory(this.mapper.map(category, CategoryDto.class));

        return response;

    }


}
