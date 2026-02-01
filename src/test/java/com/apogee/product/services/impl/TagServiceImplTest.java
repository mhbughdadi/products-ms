package com.apogee.product.services.impl;

import com.apogee.product.entities.TagEntity;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.models.Tag;
import com.apogee.product.repositories.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    // Helper builders
    private TagEntity buildTagEntity(Long id, String name) {
        TagEntity e = new TagEntity();
        e.setId(id);
        if (name != null) e.setName(name);
        return e;
    }

    @Test
    void saveTag_savesAndReturns() throws Exception {
        Tag input = new Tag();
        input.setName("T1");

        TagEntity saved = buildTagEntity(10L, "T1");

        when(tagRepository.save(any(TagEntity.class))).thenReturn(saved);

        Tag out = tagService.saveTag(input);

        assertNotNull(out);
        assertEquals(10L, out.getId());
    }

    @Test
    void findTag_returnsWhenFound() throws Exception {
        TagEntity e = buildTagEntity(5L, "Tag5");

        when(tagRepository.findById(5L)).thenReturn(Optional.of(e));

        Tag t = tagService.findTag(5L);
        assertNotNull(t);
        assertEquals(5L, t.getId());
    }

    @Test
    void findTag_throwsWhenNotFound() {
        when(tagRepository.findById(6L)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> tagService.findTag(6L));
    }

    @Test
    void findAllTags_returnsListOrEmpty() throws Exception {
        TagEntity e = buildTagEntity(7L, null);
        when(tagRepository.findAll()).thenReturn(List.of(e));

        List<Tag> all = tagService.findAllTags();
        assertEquals(1, all.size());

        when(tagRepository.findAll()).thenReturn(Collections.emptyList());
        List<Tag> none = tagService.findAllTags();
        assertNotNull(none);
        assertEquals(0, none.size());
    }

    @Test
    void updateTag_updatesWhenExists() throws Exception {
        Tag t = new Tag();
        t.setId(2L);
        t.setName("U");

        when(tagRepository.existsById(2L)).thenReturn(true);
        when(tagRepository.save(any(TagEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        Tag out = tagService.updateTag(t);
        assertNotNull(out);
        assertEquals(2L, out.getId());
    }

    @Test
    void updateTag_throwsWhenNotExists() {
        Tag t = new Tag();
        t.setId(99L);
        when(tagRepository.existsById(99L)).thenReturn(false);
        assertThrows(RecordNotFoundException.class, () -> tagService.updateTag(t));
    }

    @Test
    void deleteTag_deletesWhenFound() throws Exception {
        TagEntity e = buildTagEntity(11L, null);
        when(tagRepository.findById(11L)).thenReturn(Optional.of(e));

        Tag out = tagService.deleteTag(11L);
        assertNotNull(out);
        assertEquals(11L, out.getId());
        verify(tagRepository).deleteById(11L);
    }

    @Test
    void deleteTag_throwsWhenNotFound() {
        when(tagRepository.findById(12L)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> tagService.deleteTag(12L));
    }
}
