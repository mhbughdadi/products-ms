package com.apogee.product.services.impl;

import com.apogee.product.entities.ProductEntity;
import com.apogee.product.entities.TagEntity;
import com.apogee.product.exceptions.DBException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.exceptions.MapperException;
import com.apogee.product.models.Product;
import com.apogee.product.models.Tag;
import com.apogee.product.repositories.ProductRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Captor
    private ArgumentCaptor<ProductEntity> productEntityCaptor;

    private ProductEntity buildProductEntity(Long id) {
        ProductEntity e = new ProductEntity();
        e.setId(id);
        e.setNameEn("P" + id);
        return e;
    }

    // Helper builders reused across tests
    private TagEntity buildTagEntity(Long id, String name) {
        TagEntity t = new TagEntity();
        t.setId(id);
        if (name != null) t.setName(name);
        return t;
    }

    private Tag buildTagModel(Long id, String name) {
        Tag t = new Tag();
        t.setId(id);
        t.setName(name);
        return t;
    }

    @BeforeEach
    void setup() {
    }

    @Test
    void findAllProducts_returnsMappedList_whenProductsExist() throws MapperException, RecordNotFoundException, DBException {
        List<ProductEntity> list = new ArrayList<>();
        list.add(buildProductEntity(1L));
        when(productRepository.findAll()).thenReturn(list);

        List<Product> products = productService.findAllProducts();

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals(1L, products.getFirst().getId());
    }

    @Test
    void findAllProducts_returnsEmpty_whenNoProducts() throws MapperException, RecordNotFoundException, DBException {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        List<Product> products = productService.findAllProducts();

        assertNotNull(products);
        assertEquals(0, products.size());
    }

    @Test
    void addProduct_savesAndReturnsProduct() throws MapperException, RecordNotFoundException, DBException {
        Product input = new Product();
        input.setNameEn("New");

        ProductEntity saved = buildProductEntity(10L);
        when(productRepository.save(any(ProductEntity.class))).thenReturn(saved);

        Product result = productService.addProduct(input);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        verify(productRepository).save(any(ProductEntity.class));
    }

    @Test
    void updateProduct_updatesWhenExists() throws MapperException, RecordNotFoundException, DBException {
        Product p = new Product();
        p.setId(2L);
        p.setNameEn("U");

        when(productRepository.existsById(2L)).thenReturn(true);
        when(productRepository.save(any(ProductEntity.class))).thenAnswer(inv -> {
            ProductEntity arg = inv.getArgument(0);
            arg.setId(2L);
            return arg;
        });

        Product out = productService.updateProduct(p);

        assertNotNull(out);
        assertEquals(2L, out.getId());
        verify(productRepository).save(any(ProductEntity.class));
    }

    @Test
    void updateProduct_throwsWhenNotExists() {
        Product p = new Product();
        p.setId(99L);

        when(productRepository.existsById(99L)).thenReturn(false);

        assertThrows(RecordNotFoundException.class, () -> productService.updateProduct(p));
    }

    @Test
    void findProductById_returnsWhenFound() throws MapperException, RecordNotFoundException, DBException {
        ProductEntity e = buildProductEntity(5L);
        when(productRepository.findById(5L)).thenReturn(Optional.of(e));

        Product res = productService.findProductById(5L);

        assertNotNull(res);
        assertEquals(5L, res.getId());
    }

    @Test
    void findProductById_throwsWhenNotFound() {
        when(productRepository.findById(7L)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> productService.findProductById(7L));
    }

    @Test
    void deleteProductById_deletesWhenExists() throws MapperException, RecordNotFoundException, DBException {
        when(productRepository.existsById(3L)).thenReturn(true);

        productService.deleteProductById(3L);

        verify(productRepository).deleteById(3L);
    }

    @Test
    void deleteProductById_throwsWhenNotExists() {
        when(productRepository.existsById(33L)).thenReturn(false);
        assertThrows(RecordNotFoundException.class, () -> productService.deleteProductById(33L));
    }

    @Test
    void assignTagToProduct_throwsWhenDuplicate() {
        when(productRepository.findByIdAndTagsId(1L, 10L)).thenReturn(Optional.of(new ProductEntity()));
        assertThrows(DBException.class, () -> productService.assignTagToProduct(1L, 10L));
    }

    @Test
    void assignTagToProduct_successAssignsTag() throws MapperException, RecordNotFoundException, DBException {
        TagEntity t = buildTagEntity(10L, "T");

        ProductEntity p = buildProductEntity(1L);
        p.setTags(new ArrayList<>());

        when(tagRepository.findById(10L)).thenReturn(Optional.of(t));
        when(productRepository.findById(1L)).thenReturn(Optional.of(p));
        when(productRepository.save(any(ProductEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        Product out = productService.assignTagToProduct(1L, 10L);

        assertNotNull(out);
        assertNotNull(out.getTags());
        assertEquals(1, out.getTags().size());
        assertEquals(10L, out.getTags().getFirst().getId());
    }

    @Test
    void getTagsForProduct_returnsTags() throws MapperException, RecordNotFoundException, DBException {
        TagEntity t = buildTagEntity(100L, "Tag100");

        when(tagRepository.findByItemsId(5L)).thenReturn(List.of(t));

        List<Tag> tags = productService.getTagsForProduct(5L);

        assertNotNull(tags);
        assertEquals(1, tags.size());
        assertEquals(100L, tags.getFirst().getId());
    }

    @Test
    void removeTagFromProduct_successRemovesTag() throws MapperException, RecordNotFoundException, DBException {
        TagEntity t1 = buildTagEntity(200L, null);
        ProductEntity p = buildProductEntity(1L);
        List<TagEntity> tags = new ArrayList<>();
        tags.add(t1);
        p.setTags(tags);

        when(productRepository.findByIdAndTagsId(1L, 200L)).thenReturn(Optional.of(p));
        when(productRepository.save(any(ProductEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        productService.removeTagFromProduct(1L, 200L);

        verify(productRepository).save(productEntityCaptor.capture());
        ProductEntity saved = productEntityCaptor.getValue();
        assertNotNull(saved.getTags());
        assertTrue(saved.getTags().stream().noneMatch(tag -> tag.getId().equals(200L)));
    }

    @Test
    void removeTagFromProduct_throwsWhenNotFound() {
        when(productRepository.findByIdAndTagsId(1L, 90L)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> productService.removeTagFromProduct(1L, 90L));
    }
}
