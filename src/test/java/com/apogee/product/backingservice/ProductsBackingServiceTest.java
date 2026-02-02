package com.apogee.product.backingservice;

import com.apogee.product.dtos.inputs.ProductDto;
import com.apogee.product.dtos.output.AddProductResponseDto;
import com.apogee.product.dtos.output.AllProductsResponseDto;
import com.apogee.product.dtos.output.AllTagsResponseDto;
import com.apogee.product.dtos.output.FindProductResponseDto;
import com.apogee.product.exceptions.MapperException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.models.Product;
import com.apogee.product.models.Tag;
import com.apogee.product.services.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductsBackingServiceTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductsBackingService backingService;

    @Test
    void getAllProducts_mapsList() throws MapperException {
        Product p = new Product();
        p.setId(1L);
        p.setCode("P1");

        when(productService.findAllProducts()).thenReturn(List.of(p));

        AllProductsResponseDto resp = backingService.getAllProducts();

        assertNotNull(resp);
        assertEquals(1, resp.getProducts().size());
        assertEquals(1L, resp.getProducts().getFirst().getId());
    }

    @Test
    void addProduct_and_getProductById_and_delete_and_update_flow() throws MapperException, RecordNotFoundException {
        ProductDto dto = new ProductDto();
        dto.setNameEn("X");

        Product saved = new Product();
        saved.setId(10L);
        saved.setNameEn("X");

        when(productService.addProduct(any(Product.class))).thenReturn(saved);
        when(productService.findProductById(10L)).thenReturn(saved);
        doNothing().when(productService).deleteProductById(10L);
        when(productService.updateProduct(any(Product.class))).thenReturn(saved);

        AddProductResponseDto addResp = backingService.addProduct(dto);
        assertEquals(10L, addResp.getProduct().getId());

        FindProductResponseDto findResp = backingService.getProductById(10L);
        assertEquals(10L, findResp.getProduct().getId());

        backingService.deleteProduct(10L);
        AddProductResponseDto updResp = backingService.updateProduct(dto);
        assertEquals(10L, updResp.getProduct().getId());
    }

    @Test
    void assign_remove_fetchTags() throws MapperException, RecordNotFoundException, com.apogee.product.exceptions.DBException {
        Tag t = new Tag();
        t.setId(99L);

        Product p = new Product();
        p.setId(20L);
        p.setTags(List.of(t));

        when(productService.assignTagToProduct(20L, 99L)).thenReturn(p);
        when(productService.getTagsForProduct(20L)).thenReturn(List.of(t));
        doNothing().when(productService).removeTagFromProduct(20L, 99L);

        var assignResp = backingService.assignTag(20L, 99L);
        assertEquals(20L, assignResp.getProduct().getId());
        assertEquals(1, assignResp.getProduct().getTags().size());

        var tags = backingService.fetchProductTags(20L);
        assertEquals(1, tags.getTags().size());

        var removed = backingService.removeTag(20L, 99L);
        assertNotNull(removed);
    }

    @Test
    void getAllProducts_returnsEmpty_whenNoProducts() throws MapperException {
        when(productService.findAllProducts()).thenReturn(List.of());
        AllProductsResponseDto resp = backingService.getAllProducts();
        assertNotNull(resp);
        assertTrue(resp.getProducts().isEmpty());
    }

    @Test
    void addProduct_propagatesException() throws MapperException {
        ProductDto dto = new ProductDto();
        dto.setNameEn("X");
        when(productService.addProduct(any(Product.class))).thenThrow(new MapperException("mapping fail"));

        assertThrows(MapperException.class, () -> backingService.addProduct(dto));
    }

    @Test
    void getProductById_propagatesException() throws MapperException, RecordNotFoundException {
        when(productService.findProductById(99L)).thenThrow(new MapperException("mapping fail")).thenThrow(new RecordNotFoundException("not found", 99L));

        assertThrows(MapperException.class, () -> backingService.getProductById(99L));
        assertThrows(RecordNotFoundException.class, () -> backingService.getProductById(99L));
    }

    @Test
    void fetchProductTags_returnsEmpty_whenNoTags() throws MapperException {
        when(productService.getTagsForProduct(50L)).thenReturn(List.of());
        AllTagsResponseDto resp = backingService.fetchProductTags(50L);
        assertNotNull(resp);
        assertTrue(resp.getTags().isEmpty());
    }
}
