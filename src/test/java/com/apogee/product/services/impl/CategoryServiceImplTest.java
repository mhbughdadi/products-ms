package com.apogee.product.services.impl;

import com.apogee.product.entities.CategoryEntity;
import com.apogee.product.entities.TagEntity;
import com.apogee.product.exceptions.DBException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.exceptions.MapperException;
import com.apogee.product.models.Category;
import com.apogee.product.models.Tag;
import com.apogee.product.repositories.CategoryRepository;
import com.apogee.product.repositories.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Captor
    private ArgumentCaptor<CategoryEntity> categoryEntityCaptor;

    private List<CategoryEntity> categoryEntities;

    private TagEntity buildTagEntity(Long id, String name) {
        TagEntity t = new TagEntity();
        t.setId(id);
        if (name != null) t.setName(name);
        return t;
    }

    @BeforeEach
    void setup() {
        categoryEntities = new ArrayList<>();

        // root category with one child and a tag to exercise mappings
        CategoryEntity root = this.buildCategoryEntityObject(1L, "CAT001", "Electronics", "إلكترونيات", "Category for electronic products", "فئة للمنتجات الإلكترونية", true, null);
        CategoryEntity child = this.buildCategoryEntityObject(2L, "CAT001-1", "Mobile", "موبايل", "Mobile phones", "هواتف محمولة", true, root);

        // wire parent/child
        List<CategoryEntity> subs = new ArrayList<>();
        subs.add(child);
        root.setSubCategories(subs);

        // add a tag to the child to validate tags mapping
        TagEntity tag = buildTagEntity(10L, "NewTag");
        child.setTags(new ArrayList<>());
        child.getTags().add(tag);

        categoryEntities.add(root);
    }

    @Test
    void findAllCategories_shouldReturnHierarchicalCategories_whenRootAndSubCategoriesExist() throws MapperException, RecordNotFoundException, DBException {

        when(categoryRepository.findAllRootCategoriesWithSubCategories()).thenReturn(categoryEntities);

        List<Category> categories = this.categoryService.findAllCategories();

        assertNotNull(categories);
        assertEquals(1, categories.size());

        Category root = categories.getFirst();
        assertEquals("CAT001", root.getCode());
        assertEquals(1L, root.getId());
        assertNull(root.getParentId());
        assertNotNull(root.getSubCategories());
        assertEquals(1, root.getSubCategories().size());

        Category child = root.getSubCategories().getFirst();
        assertEquals(2L, child.getId());
        assertEquals(1L, child.getParentId());

        assertEquals(1, child.getTags().size());
        assertEquals(10L, child.getTags().getFirst().getId());
    }

    @Test
    void findAllCategories_shouldReturnEmptyList_whenNoRootCategoriesExist() throws MapperException, RecordNotFoundException, DBException {

        when(categoryRepository.findAllRootCategoriesWithSubCategories()).thenReturn(Collections.emptyList());

        List<Category> categories = this.categoryService.findAllCategories();

        assertNotNull(categories);
        assertEquals(0, categories.size());
    }

    @Test
    void givenRepositoryThrowsException_whenFindAllCategories_thenExceptionPropagated() {

        when(categoryRepository.findAllRootCategoriesWithSubCategories()).thenThrow(new MapperException("mapping exception"));

        assertThrows(MapperException.class, () -> this.categoryService.findAllCategories());
    }

    @Test
    void findAllCategories_returnsCategoriesWithTags_whenTagsAreAssigned() throws MapperException, RecordNotFoundException, DBException {
        when(categoryRepository.findAllRootCategoriesWithSubCategories()).thenReturn(categoryEntities);

        List<Category> categories = this.categoryService.findAllCategories();

        Category child = categories.getFirst().getSubCategories().getFirst();
        assertEquals(1, child.getTags().size());
        Tag tag = child.getTags().getFirst();
        assertEquals(10L, tag.getId());
        assertEquals("NewTag", tag.getName());
    }

    @Test
    void findCategoryByID_returnsCategory_whenCategoryExists() throws MapperException, RecordNotFoundException, DBException {
        CategoryEntity entity = this.buildCategoryEntityObject(1L, "CAT001", "Electronics", "إلكترونيات", "Category for electronic products", "فئة للمنتجات الإلكترونية", true, null);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(entity));

        Category category = this.categoryService.findCategoryByID(1L);
        assertNotNull(category);
        assertEquals(1L, category.getId());
        assertEquals("CAT001", category.getCode());
    }

    @Test
    void findCategoryByID_throwsRecordNotFoundException_whenCategoryDoesNotExist() {

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> this.categoryService.findCategoryByID(1L));
    }

    @Test
    void addCategory_savesAndReturnsCategory_whenValidCategoryProvided_withoutParent() throws MapperException, RecordNotFoundException, DBException {
        // input model
        Category toAdd = new Category();
        toAdd.setCode("NEW");
        toAdd.setNameEn("New Cat");

        CategoryEntity saved = this.buildCategoryEntityObject(100L, "NEW", "New Cat", "جديد", "desc", "descAr", true, null);

        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(saved);

        Category result = this.categoryService.addCategory(toAdd);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        //explain the  next line

        verify(categoryRepository, times(1)).save(any(CategoryEntity.class));
    }

    @Test
    void addCategory_savesAndReturnsCategory_whenValidCategoryProvided_withParent() throws MapperException, RecordNotFoundException, DBException {
        // input model with parent
        Category toAdd = new Category();
        toAdd.setCode("NEW");
        toAdd.setNameEn("New Cat");
        toAdd.setParentId(1L);

        CategoryEntity parent = this.buildCategoryEntityObject(1L, "CAT001", "Electronics", "إلكترونيات", "Category for electronic products", "فئة", true, null);
        CategoryEntity savedFinal = this.buildCategoryEntityObject(101L, "NEW", "New Cat", "جديد", "desc", "descAr", true, parent);

        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(savedFinal);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(parent));

        Category result = this.categoryService.addCategory(toAdd);

        assertNotNull(result);
        assertEquals(101L, result.getId());
        assertEquals(1L, result.getParentId());
        verify(categoryRepository, times(1)).save(any(CategoryEntity.class));
    }

    @Test
    void deleteCategoryById_deletesAndReturnsCategory_whenCategoryExists() throws MapperException, RecordNotFoundException, DBException {
        CategoryEntity entity = this.buildCategoryEntityObject(5L, "DEL", "ToDelete", "", "desc", "", true, null);

        when(categoryRepository.findById(5L)).thenReturn(Optional.of(entity));

        Category result = this.categoryService.deleteCategoryById(5L);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        verify(categoryRepository, times(1)).deleteById(5L);
    }

    @Test
    void deleteCategoryById_throwsRecordNotFoundException_whenCategoryDoesNotExist() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> this.categoryService.deleteCategoryById(99L));
    }

    @Test
    void updateCategory_savesAndReturnsUpdatedCategory() throws MapperException, RecordNotFoundException, DBException {
        Category toUpdate = new Category();
        toUpdate.setId(2L);
        toUpdate.setCode("UPD");

        CategoryEntity saved = this.buildCategoryEntityObject(2L, "UPD", "Updated", "", "desc", "", true, null);

        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(saved);

        Category result = this.categoryService.updateCategory(toUpdate);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("UPD", result.getCode());
        verify(categoryRepository, times(1)).save(any(CategoryEntity.class));
    }

    @Test
    void assignTagToCategory_whenAssignmentExists_shouldThrowDBException() {
        when(categoryRepository.findByIdAndTagsId(1L, 10L)).thenReturn(Optional.of(new CategoryEntity()));

        assertThrows(DBException.class, () -> this.categoryService.assignTagToCategory(1L, 10L));
    }

    @Test
    void assignTagToCategory_successfulAssignment() throws MapperException, RecordNotFoundException, DBException {
        TagEntity tagEntity = new TagEntity();
        tagEntity.setId(10L);
        tagEntity.setName("T1");

        CategoryEntity category = this.buildCategoryEntityObject(1L, "CAT001", "Electronics", "إلكترونيات", "desc", "descAr", true, null);
        category.setTags(new ArrayList<>());

        when(tagRepository.findById(10L)).thenReturn(Optional.of(tagEntity));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // simulate save returning entity with tag added
        CategoryEntity saved = this.buildCategoryEntityObject(1L, "CAT001", "Electronics", "", "desc", "", true, null);
        saved.setTags(new ArrayList<>());
        saved.getTags().add(buildTagEntity(10L, "T1"));

        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(saved);

        Category result = this.categoryService.assignTagToCategory(1L, 10L);

        assertNotNull(result);
        assertNotNull(result.getTags());
        assertEquals(1, result.getTags().size());
        assertEquals(10L, result.getTags().getFirst().getId());
    }

    @Test
    void getTagsForCategory_returnsTags() throws MapperException, RecordNotFoundException, DBException {
        TagEntity tag1 = buildTagEntity(100L, "Tag100");

        when(tagRepository.findByItemsId(5L)).thenReturn(List.of(tag1));

        List<Tag> tags = this.categoryService.getTagsForCategory(5L);

        assertNotNull(tags);
        assertEquals(1, tags.size());
        assertEquals(100L, tags.getFirst().getId());
    }

    @Test
    void removeTagFromCategory_whenNotFound_throwsDBException() {
        when(categoryRepository.findByIdAndTagsId(1L, 90L)).thenReturn(Optional.empty());

        assertThrows(DBException.class, () -> this.categoryService.removeTagFromCategory(1L, 90L));
    }

    @Test
    void removeTagFromCategory_success_removesTagAndSaves() throws MapperException, RecordNotFoundException, DBException {
        TagEntity t1 = buildTagEntity(200L, null);
        TagEntity t2 = buildTagEntity(201L, null);

        CategoryEntity cat = this.buildCategoryEntityObject(1L, "CAT", "Cat", "", "desc", "", true, null);
        List<TagEntity> tags = new ArrayList<>();
        tags.add(t1);
        tags.add(t2);
        cat.setTags(tags);

        when(categoryRepository.findByIdAndTagsId(1L, 200L)).thenReturn(Optional.of(cat));
        when(categoryRepository.save(any(CategoryEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        this.categoryService.removeTagFromCategory(1L, 200L);

        verify(categoryRepository).save(categoryEntityCaptor.capture());
        CategoryEntity saved = categoryEntityCaptor.getValue();
        assertNotNull(saved.getTags());
        assertEquals(1, saved.getTags().size());
        assertTrue(saved.getTags().stream().noneMatch(tag -> tag.getId().equals(200L)));
    }

    CategoryEntity buildCategoryEntityObject(Long id, String code, String nameEn, String nameAr, String descriptionEn, String descriptionAr, boolean active, CategoryEntity parent) {
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
