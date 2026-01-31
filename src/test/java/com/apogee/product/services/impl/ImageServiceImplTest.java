package com.apogee.product.services.impl;

import com.apogee.product.entities.ImageEntity;
import com.apogee.product.entities.ParentImageEntity;
import com.apogee.product.entities.ProductEntity;
import com.apogee.product.models.Image;
import com.apogee.product.repositories.ImageRepository;
import com.apogee.product.repositories.ParentImageRepository;
import com.apogee.product.repositories.ParentItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceImplTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ParentImageRepository parentImageRepository;

    @Mock
    private ParentItemRepository parentItemRepository;

    @InjectMocks
    private ImageServiceImpl imageService;

    private ImageEntity buildImageEntity(Long id) {
        ImageEntity e = new ImageEntity();
        e.setId(id);
        return e;
    }

    private ParentImageEntity buildParentImageEntity(ImageEntity image, int sortOrder, boolean isActive) {
        ParentImageEntity p = new ParentImageEntity();
        p.setImage(image);
        p.setSortOrder(sortOrder);
        p.setIsActive(isActive);
        return p;
    }

    private ProductEntity buildParentItemEntity(Long id) {
        ProductEntity p = new ProductEntity();
        p.setId(id);
        return p;
    }

    @Test
    public void findAllImages_returnsListOrEmpty() throws Exception {
        ImageEntity e = buildImageEntity(1L);
        when(imageRepository.findAll()).thenReturn(List.of(e));

        List<Image> out = imageService.findAllImages();
        assertEquals(1, out.size());

        when(imageRepository.findAll()).thenReturn(List.of());
        List<Image> none = imageService.findAllImages();
        assertNotNull(none);
        assertEquals(0, none.size());
    }

    @Test
    public void saveImages_savesAndReturns() throws Exception {
        Image img = new Image();
        img.setId(5L);
        List<Image> imgs = List.of(img);

        ImageEntity saved = new ImageEntity();
        saved.setId(5L);
        when(imageRepository.saveAll(anyList())).thenReturn(List.of(saved));

        List<Image> out = imageService.saveImages(imgs);
        assertEquals(1, out.size());
    }

    @Test
    public void findImageById_returnsWhenFound() throws Exception {
        ImageEntity e = new ImageEntity();
        e.setId(9L);
        when(imageRepository.findById(9L)).thenReturn(Optional.of(e));

        Image out = imageService.findImageById(9L);
        assertNotNull(out);
        assertEquals(9L, out.getId());
    }

    @Test
    public void findImageById_throwsWhenNotFound() {
        when(imageRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> imageService.findImageById(99L));
    }

    @Test
    public void updateImage_throwsWhenNotExists() {
        Image i = new Image();
        i.setId(4L);
        when(imageRepository.existsById(4L)).thenReturn(false);
        assertThrows(Exception.class, () -> imageService.updateImage(i));
    }

    @Test
    public void deleteImageById_throwsWhenNotExists() {
        when(imageRepository.existsById(20L)).thenReturn(false);
        assertThrows(Exception.class, () -> imageService.deleteImageById(20L));
    }

    @Test
    public void saveImage_throwsWhenExists() {
        Image i = new Image();
        i.setId(6L);
        when(imageRepository.existsById(6L)).thenReturn(true);
        assertThrows(Exception.class, () -> imageService.saveImage(i));
    }

    @Test
    public void findImagesByParentItemId_returnsWhenParentExists() throws Exception {
        ProductEntity parent = new ProductEntity();
        parent.setId(2L);
        ParentImageEntity pi = new ParentImageEntity();
        ImageEntity img = new ImageEntity();
        img.setId(7L);
        pi.setImage(img);
        pi.setSortOrder(0);
        pi.setIsActive(true);
        parent.setParentImages(new ArrayList<>());
        parent.getParentImages().add(pi);

        when(parentItemRepository.findById(2L)).thenReturn(Optional.of(parent));

        List<Image> out = imageService.findImagesByParentItemId(2L);
        assertEquals(1, out.size());
        assertEquals(7L, out.get(0).getId());
    }

    @Test
    public void deleteImagesByParentItemId_throwsWhenParentHasNoImages() {
        ProductEntity parent = new ProductEntity();
        parent.setId(3L);
        parent.setParentImages(new ArrayList<>());
        when(parentItemRepository.findById(3L)).thenReturn(Optional.of(parent));

        assertThrows(Exception.class, () -> imageService.deleteImagesByParentItemId(3L));
    }

    @Test
    public void addImageToParentItem_savesAssociation() throws Exception {
        ProductEntity parent = new ProductEntity();
        parent.setId(4L);
        when(parentItemRepository.findById(4L)).thenReturn(Optional.of(parent));

        ImageEntity img = new ImageEntity();
        img.setId(8L);
        when(imageRepository.findById(8L)).thenReturn(Optional.of(img));

        ParentImageEntity saved = new ParentImageEntity();
        saved.setImage(img);
        saved.setSortOrder(0);
        saved.setIsActive(true);
        when(parentImageRepository.save(any(ParentImageEntity.class))).thenReturn(saved);

        Image out = imageService.addImageToParentItem(4L, new Image() {
            {
                setId(8L);
            }
        });
        assertNotNull(out);
        assertEquals(8L, out.getId());
    }

    @Test
    public void removeImageFromParentItem_throwsWhenNotExists() {
        when(parentImageRepository.existsById(any())).thenReturn(false);
        assertThrows(Exception.class, () -> imageService.removeImageFromParentItem(5L, 6L));
    }
}
