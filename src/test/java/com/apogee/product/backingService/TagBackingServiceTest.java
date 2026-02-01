package com.apogee.product.backingService;

import com.apogee.product.dtos.inputs.TagDto;
import com.apogee.product.dtos.output.AllTagsResponseDto;
import com.apogee.product.dtos.output.TagResponseDto;
import com.apogee.product.models.Tag;
import com.apogee.product.services.TagService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagBackingServiceTest {

    @Mock
    private TagService tagService;

    @InjectMocks
    private TagBackingService backingService;

    @Test
    void add_update_delete_and_getAll_getById() throws Exception {
        TagDto dto = new TagDto();
        dto.setName("N");

        Tag saved = new Tag();
        saved.setId(2L);
        saved.setName("N");

        when(tagService.saveTag(any(Tag.class))).thenReturn(saved);
        when(tagService.updateTag(any(Tag.class))).thenReturn(saved);
        when(tagService.deleteTag(2L)).thenReturn(saved);
        when(tagService.findAllTags()).thenReturn(List.of(saved));
        when(tagService.findTag(2L)).thenReturn(saved);

        TagResponseDto add = backingService.addTag(dto);
        assertEquals(2L, add.getTag().getId());

        TagResponseDto update = backingService.updateTag(dto);
        assertEquals(2L, update.getTag().getId());

        TagResponseDto delete = backingService.deleteTagById(2L);
        assertEquals(2L, delete.getTag().getId());

        AllTagsResponseDto all = backingService.getAllTags();
        assertEquals(1, all.getTags().size());

        TagResponseDto byId = backingService.getTagById(2L);
        assertEquals(2L, byId.getTag().getId());
    }

    @Test
    void getAllTags_returnsEmpty_whenNoTags() throws Exception {
        when(tagService.findAllTags()).thenReturn(List.of());
        AllTagsResponseDto all = backingService.getAllTags();
        assertNotNull(all);
        assertTrue(all.getTags().isEmpty());
    }

    @Test
    void getTagById_propagatesException() throws Exception {
        when(tagService.findTag(99L)).thenThrow(new RuntimeException("not found"));
        assertThrows(RuntimeException.class, () -> backingService.getTagById(99L));
    }
}
