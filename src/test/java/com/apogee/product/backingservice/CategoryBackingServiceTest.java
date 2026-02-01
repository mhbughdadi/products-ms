package com.apogee.product.backingservice;

import com.apogee.product.dtos.inputs.CategoryDto;
import com.apogee.product.dtos.output.AllCategoriesResponseDto;
import com.apogee.product.dtos.output.AllTagsResponseDto;
import com.apogee.product.dtos.output.CategoryResponseDto;
import com.apogee.product.dtos.output.SuccessfulResponse;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.exceptions.MapperException;
import com.apogee.product.exceptions.DBException;
import com.apogee.product.models.Category;
import com.apogee.product.models.Tag;
import com.apogee.product.services.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryBackingServiceTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryBackingService backingService;

    @Test
    void addCategory_mapsDtoAndReturnsDto() throws MapperException, RecordNotFoundException {
        CategoryDto input = new CategoryDto();
        input.setCode("C1");

        Category model = new Category();
        model.setId(5L);
        model.setCode("C1");

        when(categoryService.addCategory(any(Category.class))).thenReturn(model);

        CategoryResponseDto resp = backingService.addCategory(input);

        assertNotNull(resp);
        assertEquals(5L, resp.getCategory().getId());
        assertEquals("C1", resp.getCategory().getCode());
    }

    @Test
    void getAllCategories_transformsModelsToDtos() throws MapperException {
        Category c = new Category();
        c.setId(1L);
        c.setCode("ROOT");

        Category child = new Category();
        child.setId(2L);
        child.setParentId(1L);
        c.setSubCategories(List.of(child));

        when(categoryService.findAllCategories()).thenReturn(List.of(c));

        AllCategoriesResponseDto resp = backingService.getAllCategories();

        assertNotNull(resp);
        assertEquals(1, resp.getCategories().size());
        assertEquals(1L, resp.getCategories().getFirst().getId());
        assertEquals(1, resp.getCategories().getFirst().getSubCategories().size());
    }

    @Test
    void getCategoryById_mapsModelToDto() throws MapperException, RecordNotFoundException {
        Category model = new Category();
        model.setId(7L);
        model.setCode("C7");

        when(categoryService.findCategoryByID(7L)).thenReturn(model);

        CategoryResponseDto resp = backingService.getCategoryById(7L);

        assertNotNull(resp);
        assertEquals(7L, resp.getCategory().getId());
    }

    @Test
    void assignTag_and_fetchTags_and_removeTag_behaveAsExpected() throws MapperException, RecordNotFoundException, DBException {
        Tag tag = new Tag();
        tag.setId(11L);
        tag.setName("T1");

        Category withTag = new Category();
        withTag.setId(1L);
        withTag.setTags(List.of(tag));

        when(categoryService.assignTagToCategory(1L, 11L)).thenReturn(withTag);
        when(categoryService.getTagsForCategory(1L)).thenReturn(List.of(tag));
        doNothing().when(categoryService).removeTagFromCategory(1L, 11L);

        CategoryResponseDto assigned = backingService.assignTag(1L, 11L);
        assertNotNull(assigned.getCategory().getTags());
        assertEquals(1, assigned.getCategory().getTags().size());

        AllTagsResponseDto tagsResp = backingService.fetchCategoryTags(1L);
        assertNotNull(tagsResp.getTags());
        assertEquals(1, tagsResp.getTags().size());
        assertEquals(11L, tagsResp.getTags().getFirst().getId());

        SuccessfulResponse removed = backingService.removeTag(1L, 11L);
        assertNotNull(removed);
    }

    @Test
    void deleteCategoryById_mapsReturnedModel() throws MapperException, RecordNotFoundException {
        Category deleted = new Category();
        deleted.setId(3L);
        deleted.setCode("DEL");

        when(categoryService.deleteCategoryById(3L)).thenReturn(deleted);

        CategoryResponseDto resp = backingService.deleteCategoryById(3L);
        assertEquals(3L, resp.getCategory().getId());
    }

    @Test
    void deleteCategoryById_propagatesException() throws MapperException, RecordNotFoundException {
        when(categoryService.deleteCategoryById(99L)).thenThrow(new RecordNotFoundException("db gone", 99L));
        assertThrows(RecordNotFoundException.class, () -> backingService.deleteCategoryById(99L));
    }

    @Test
    void addCategory_propagatesException() throws MapperException, RecordNotFoundException {
        CategoryDto dto = new CategoryDto();
        dto.setCode("X");
        when(categoryService.addCategory(any(Category.class))).thenThrow(new RecordNotFoundException("save failed", 0L));
        assertThrows(RecordNotFoundException.class, () -> backingService.addCategory(dto));
    }

    @Test
    void getAllCategories_returnsEmptyList_whenServiceReturnsEmpty() throws MapperException {
        when(categoryService.findAllCategories()).thenReturn(List.of());
        AllCategoriesResponseDto resp = backingService.getAllCategories();
        assertNotNull(resp);
        assertTrue(resp.getCategories().isEmpty());
    }

    @Test
    void assignTag_propagatesRecordNotFound() throws MapperException, RecordNotFoundException, DBException {
        when(categoryService.assignTagToCategory(1L, 10L)).thenThrow(new com.apogee.product.exceptions.RecordNotFoundException("record.not.found", 1L));
        assertThrows(com.apogee.product.exceptions.RecordNotFoundException.class, () -> backingService.assignTag(1L, 10L));
    }

    @Test
    void fetchCategoryTags_returnsEmptyList_whenNoTags() throws MapperException {
        when(categoryService.getTagsForCategory(5L)).thenReturn(List.of());
        AllTagsResponseDto resp = backingService.fetchCategoryTags(5L);
        assertNotNull(resp);
        assertTrue(resp.getTags().isEmpty());
    }
}
