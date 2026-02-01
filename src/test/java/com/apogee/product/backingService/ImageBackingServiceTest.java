package com.apogee.product.backingService;

import com.apogee.product.dtos.inputs.ImageDto;
import com.apogee.product.dtos.output.AllImagesResponseDto;
import com.apogee.product.dtos.output.ImageResponseDto;
import com.apogee.product.dtos.output.SuccessfulResponse;
import com.apogee.product.models.Image;
import com.apogee.product.services.ImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageBackingServiceTest {

    @Mock
    private ImageService imageService;

    @InjectMocks
    private ImageBackingService backingService;

    @Test
    void add_update_and_get_and_delete() throws Exception {
        ImageDto dto = new ImageDto();
        dto.setUrl("u");

        Image saved = new Image();
        saved.setId(77L);
        saved.setUrl("u");

        when(imageService.saveImage(any(Image.class))).thenReturn(saved);
        when(imageService.updateImage(any(Image.class))).thenReturn(saved);
        doNothing().when(imageService).deleteImageById(77L);

        ImageResponseDto add = backingService.addImage(dto);
        assertEquals(77L, add.getImage().getId());

        ImageResponseDto upd = backingService.updateImage(dto);
        assertEquals("u", upd.getImage().getUrl());

        SuccessfulResponse del = backingService.deleteImageById(77L);
        assertNotNull(del);
    }

    @Test
    void addImages_and_getAll_and_getByParent_and_deleteByParent_and_addToParent_removeFromParent() throws Exception {
        ImageDto dto = new ImageDto();
        dto.setUrl("i1");

        Image img = new Image();
        img.setId(8L);
        img.setUrl("i1");

        when(imageService.saveImages(anyList())).thenReturn(List.of(img));
        when(imageService.findAllImages()).thenReturn(List.of(img));
        when(imageService.findImageById(8L)).thenReturn(img);
        when(imageService.findImagesByParentItemId(2L)).thenReturn(List.of(img));
        doNothing().when(imageService).deleteImagesByParentItemId(2L);
        when(imageService.addImageToParentItem(eq(2L), any(Image.class))).thenReturn(img);
        doNothing().when(imageService).removeImageFromParentItem(2L, 8L);

        var addAll = backingService.addImages(List.of(dto));
        assertEquals(1, addAll.getImages().size());

        var all = backingService.getAllImages();
        assertEquals(1, all.getImages().size());

        var byId = backingService.getImageById(8L);
        assertEquals(8L, byId.getImage().getId());

        var parentImgs = backingService.getImagesByParentItemId(2L);
        assertEquals(1, parentImgs.getImages().size());

        var delByParent = backingService.deleteImagesByParentItemId(2L);
        assertNotNull(delByParent);

        var addedToParent = backingService.addImageToParentItem(2L, dto);
        assertEquals(8L, addedToParent.getImage().getId());

        var removed = backingService.removeImageFromParentItem(2L, 8L);
        assertNotNull(removed);
    }

    @Test
    void getImageById_propagatesException() throws Exception {
        when(imageService.findImageById(123L)).thenThrow(new RuntimeException("not found"));
        assertThrows(RuntimeException.class, () -> backingService.getImageById(123L));
    }

    @Test
    void addImages_handlesEmptyList() throws Exception {
        when(imageService.saveImages(anyList())).thenReturn(List.of());
        var resp = backingService.addImages(List.of());
        assertNotNull(resp);
        assertTrue(resp.getImages().isEmpty());
    }

    @Test
    void getImagesByParentItemId_returnsEmpty_whenNone() throws Exception {
        when(imageService.findImagesByParentItemId(7L)).thenReturn(List.of());
        var resp = backingService.getImagesByParentItemId(7L);
        assertNotNull(resp);
        assertTrue(resp.getImages().isEmpty());
    }
}