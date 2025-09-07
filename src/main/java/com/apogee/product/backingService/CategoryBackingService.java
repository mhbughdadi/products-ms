package com.apogee.product.backingService;

import com.apogee.product.dtos.inputs.CategoryDto;
import com.apogee.product.dtos.output.AllCategoriesResponseDto;
import com.apogee.product.dtos.output.CategoryResponseDto;
import com.apogee.product.models.Category;
import com.apogee.product.services.CategoryService;
import com.apogee.product.utilities.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.apogee.product.utilities.Utilities.transformCollection;

@Service
public class CategoryBackingService {

    @Autowired
    private CategoryService categoryService;

    public CategoryResponseDto addCategory(CategoryDto categoryDto) throws Exception {

        CategoryResponseDto response = new CategoryResponseDto();

        Category savedCurrency = this.categoryService.addCategory(Mapper.map(categoryDto, Category.class));

        response.setCategory(Mapper.map(savedCurrency, CategoryDto.class));

        return response;
    }

    public CategoryResponseDto updateCategory(CategoryDto categoryDto) throws Exception {

        CategoryResponseDto response = new CategoryResponseDto();

        Category savedCurrency = this.categoryService.updateCategory(Mapper.map(categoryDto, Category.class));

        response.setCategory(Mapper.map(savedCurrency, CategoryDto.class));

        return response;
    }

    public CategoryResponseDto deleteCategoryById(Long categoryId) throws Exception {

        CategoryResponseDto response = new CategoryResponseDto();

        Category deletedCategory = this.categoryService.deleteCategoryById(categoryId);

        response.setCategory(Mapper.map(deletedCategory, CategoryDto.class));

        return response;
    }

    public AllCategoriesResponseDto getAllCategories() throws Exception {

        AllCategoriesResponseDto response = new AllCategoriesResponseDto();

        List<Category> allCategories = this.categoryService.findAllCategories();
        List<CategoryDto> categoryDtoList = transformCollection(allCategories, CategoryDto.class);

        response.setCategories(categoryDtoList);

        return response;
    }

    public CategoryResponseDto getCategoryById(Long categoryId) throws Exception {

        CategoryResponseDto response = new CategoryResponseDto();

        Category category = this.categoryService.findCategoryByID(categoryId);

        response.setCategory(Mapper.map(category, CategoryDto.class));

        return response;

    }


}
