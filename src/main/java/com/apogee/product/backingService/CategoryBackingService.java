package com.apogee.product.backingService;

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


    public CategoryResponseDto assignTag(Long categoryId, Long tagId) throws Exception {

        CategoryResponseDto response = new CategoryResponseDto();

        Category category = this.categoryService.assignTagToCategory(categoryId, tagId);

        response.setCategory( Mapper.map(category, CategoryDto.class));

        return response;
    }

    public SuccessfulResponse removeTag(Long categoryId, Long tagId) throws Exception {

        SuccessfulResponse response = new SuccessfulResponse();

        this.categoryService.removeTagFromCategory(categoryId, tagId);

        return response;
    }

    public AllTagsResponseDto fetchCategoryTags(Long categoryId) throws Exception {

        AllTagsResponseDto response = new AllTagsResponseDto();

        List<Tag> categoryTags = this.categoryService.getTagsForCategory(categoryId);

        response.setTags(transformCollection(categoryTags, TagDto.class));

        return response;
    }
}
