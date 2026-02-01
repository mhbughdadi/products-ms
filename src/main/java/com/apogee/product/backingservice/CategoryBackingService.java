package com.apogee.product.backingservice;

import com.apogee.product.dtos.inputs.CategoryDto;
import com.apogee.product.dtos.inputs.TagDto;
import com.apogee.product.dtos.output.AllCategoriesResponseDto;
import com.apogee.product.dtos.output.AllTagsResponseDto;
import com.apogee.product.dtos.output.CategoryResponseDto;
import com.apogee.product.dtos.output.SuccessfulResponse;
import com.apogee.product.models.Category;
import com.apogee.product.models.Tag;
import com.apogee.product.services.CategoryService;
import com.apogee.product.utilities.Mapper;
import com.apogee.product.exceptions.MapperException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.exceptions.DBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.apogee.product.utilities.Utilities.transform;
import static com.apogee.product.utilities.Utilities.transformCollection;

@Service
public class CategoryBackingService {

    @Autowired
    private CategoryService categoryService;

    public CategoryResponseDto addCategory(CategoryDto categoryDto) throws MapperException, RecordNotFoundException {

        CategoryResponseDto response = new CategoryResponseDto();

        Category savedCurrency = this.categoryService.addCategory(transform(categoryDto, Category.class));

        response.setCategory(transform(savedCurrency, CategoryDto.class));

        return response;
    }

    public CategoryResponseDto updateCategory(CategoryDto categoryDto) throws MapperException {

        CategoryResponseDto response = new CategoryResponseDto();

        Category savedCurrency = this.categoryService.updateCategory(transform(categoryDto, Category.class));

        response.setCategory(transform(savedCurrency, CategoryDto.class));

        return response;
    }

    public CategoryResponseDto deleteCategoryById(Long categoryId) throws MapperException, RecordNotFoundException {

        CategoryResponseDto response = new CategoryResponseDto();

        Category deletedCategory = this.categoryService.deleteCategoryById(categoryId);

        response.setCategory(transform(deletedCategory, CategoryDto.class));

        return response;
    }

    public AllCategoriesResponseDto getAllCategories() throws MapperException {

        AllCategoriesResponseDto response = new AllCategoriesResponseDto();

        List<Category> allCategories = this.categoryService.findAllCategories();
        List<CategoryDto> categoryDtoList = transformCollection(allCategories, CategoryDto.class);

        response.setCategories(categoryDtoList);

        return response;
    }

    public CategoryResponseDto getCategoryById(Long categoryId) throws MapperException, RecordNotFoundException {

        CategoryResponseDto response = new CategoryResponseDto();

        Category category = this.categoryService.findCategoryByID(categoryId);

        response.setCategory(transform(category, CategoryDto.class));

        return response;

    }


    public CategoryResponseDto assignTag(Long categoryId, Long tagId) throws MapperException, RecordNotFoundException, DBException {

        CategoryResponseDto response = new CategoryResponseDto();

        Category category = this.categoryService.assignTagToCategory(categoryId, tagId);

        response.setCategory( transform(category, CategoryDto.class));

        return response;
    }

    public SuccessfulResponse removeTag(Long categoryId, Long tagId) throws MapperException, DBException {

        SuccessfulResponse response = new SuccessfulResponse();

        this.categoryService.removeTagFromCategory(categoryId, tagId);

        return response;
    }

    public AllTagsResponseDto fetchCategoryTags(Long categoryId) throws MapperException {

        AllTagsResponseDto response = new AllTagsResponseDto();

        List<Tag> categoryTags = this.categoryService.getTagsForCategory(categoryId);

        response.setTags(transformCollection(categoryTags, TagDto.class));

        return response;
    }
}
