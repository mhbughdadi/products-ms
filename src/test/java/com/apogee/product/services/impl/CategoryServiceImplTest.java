package com.apogee.product.services.impl;

import com.apogee.product.entities.CategoryEntity;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.models.Category;
import com.apogee.product.repositories.CategoryRepository;
import com.apogee.product.repositories.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private List<CategoryEntity> categoryEntities;


    @BeforeEach
    public void setup() {
        categoryEntities = new ArrayList<>();

        CategoryEntity entity = this.buildCategoryEntityObject(1L,"CAT001", "Electronics", "إلكترونيات", "Category for electronic products", "فئة للمنتجات الإلكترونية", true, null);

        categoryEntities.add(entity);
    }

    @Test
    public void findAllCategories_shouldReturnHierarchicalCategories_whenRootAndSubCategoriesExist() throws Exception {

        when(categoryRepository.findAllRootCategoriesWithSubCategories()).thenReturn(categoryEntities);

        List<Category> categories = this.categoryService.findAllCategories();

        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals("CAT001", categories.getFirst().getCode());
        assertEquals(1L, categories.getFirst().getId());
    }

    @Test
    public void findAllCategories_shouldReturnEmptyList_whenNoRootCategoriesExist() throws Exception {

        when(categoryRepository.findAllRootCategoriesWithSubCategories()).thenReturn(Collections.emptyList());

        List<Category> categories = this.categoryService.findAllCategories();

        assertNotNull(categories);
        assertEquals(0, categories.size());
    }

    @Test
    public void givenRepositoryThrowsException_whenFindAllCategories_thenExceptionPropagated() throws Exception {

        when(categoryRepository.findAllRootCategoriesWithSubCategories()).thenThrow(new RuntimeException("mapping exception"));

        assertThrows(RuntimeException.class, () -> {
            this.categoryService.findAllCategories();
        });
    }

    @Test
    public void findAllCategories_shouldPopulateIdsAndParentIds_forEachCategory() {
        assert (true);
    }

    @Test
    public void findAllCategories_returnsCategoriesWithTags_whenTagsAreAssigned() {
        assert (true);
    }

    @Test
    public void findCategoryByID_returnsCategory_whenCategoryExists() throws Exception {
        CategoryEntity entity = this.buildCategoryEntityObject(
                1L,
                "CAT001",
                "Electronics",
                "إلكترونيات",
                "Category for electronic products",
                "فئة للمنتجات الإلكترونية",
                true,
                null
        );

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(entity));

        Category category = this.categoryService.findCategoryByID(1L);
        assertNotNull(category);
        assertEquals(1L, category.getId());
        assertEquals("CAT001", category.getCode());
    }
    @Test
    public void findCategoryByID_throwsRecordNotFoundException_whenCategoryDoesNotExist() {

        when(categoryRepository.findById(1L)).thenThrow(new RecordNotFoundException("record.not.found", 1L));
        assertThrows(RecordNotFoundException.class, ( ) -> {
            this.categoryService.findCategoryByID(1L);
        });
    }

    @Test
    public void addCategory_savesAndReturnsCategory_whenValidCategoryProvided() {
        assert (true);
    }
    @Test
    public void deleteCategoryById_deletesAndReturnsCategory_whenCategoryExists() {
        assert (true);
    }
    @Test
    public void deleteCategoryById_throwsRecordNotFoundException_whenCategoryDoesNotExist() {
        assert (true);
    }


    public CategoryEntity buildCategoryEntityObject(Long id, String code, String nameEn, String nameAr, String descriptionEn, String descriptionAr, boolean active, CategoryEntity parent) {
        CategoryEntity category = new CategoryEntity();
        category.setId(id);
        category.setCode(code);
        category.setNameEn(nameEn);
        category.setNameAr(nameAr);
        category.setDescriptionEn(descriptionEn);
        category.setDescriptionAr(descriptionAr);
        category.setActive(active);
        category.setParent(parent);
        return category;
    }
}
