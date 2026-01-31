package com.apogee.product.services.impl;

import com.apogee.product.entities.BenefitEntity;
import com.apogee.product.entities.ProductEntity;
import com.apogee.product.entities.SkuEntity;
import com.apogee.product.entities.TagEntity;
import com.apogee.product.exceptions.DBException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.models.Benefit;
import com.apogee.product.models.Sku;
import com.apogee.product.models.Tag;
import com.apogee.product.repositories.BenefitRepository;
import com.apogee.product.repositories.ProductRepository;
import com.apogee.product.repositories.SkuRepository;
import com.apogee.product.repositories.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SkuServiceImplTest {

    @Mock
    private SkuRepository skuRepository;

    @Mock
    private BenefitRepository benefitRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private SkuServiceImpl skuService;

    private SkuEntity buildSkuEntity(Long id) {
        SkuEntity e = new SkuEntity();
        e.setId(id);
        e.setSkuCode("S" + id);
        return e;
    }

    // Helper builders
    private TagEntity buildTagEntity(Long id, String name) {
        TagEntity t = new TagEntity();
        t.setId(id);
        if (name != null) t.setName(name);
        return t;
    }

    private BenefitEntity buildBenefitEntity(Long id, Long skuId) {
        BenefitEntity be = new BenefitEntity();
        be.setId(id);
        if (skuId != null) {
            SkuEntity sk = new SkuEntity();
            sk.setId(skuId);
            be.setSku(sk);
        }
        return be;
    }

    @BeforeEach
    public void setup() {
    }

    @Test
    public void findAllSkus_returnsMappedList_whenExists() throws Exception {
        List<SkuEntity> list = new ArrayList<>();
        list.add(buildSkuEntity(1L));
        when(skuRepository.findAll()).thenReturn(list);

        List<Sku> out = skuService.findAllSkus();
        assertNotNull(out);
        assertEquals(1, out.size());
    }

    @Test
    public void addSku_requiresProductExists() {
        Sku s = new Sku();
        s.setProductId(9L);

        when(productRepository.findById(9L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> skuService.addSku(s));
    }

    @Test
    public void addSku_savesWhenProductExists() throws Exception {
        Sku s = new Sku();
        s.setProductId(1L);
        SkuEntity saved = buildSkuEntity(10L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(new ProductEntity()));
        when(skuRepository.save(any(SkuEntity.class))).thenReturn(saved);

        Sku out = skuService.addSku(s);
        assertNotNull(out);
        assertEquals(10L, out.getId());
    }

    @Test
    public void updateSku_throwsWhenNotExistsOrNoProduct() {
        Sku s = new Sku();
        s.setId(5L);
        s.setProductId(null);

        when(skuRepository.existsById(5L)).thenReturn(false);

        assertThrows(RecordNotFoundException.class, () -> skuService.updateSku(s));
    }

    @Test
    public void findSkuById_returnsWhenFound() throws Exception {
        SkuEntity e = buildSkuEntity(7L);
        when(skuRepository.findById(7L)).thenReturn(Optional.of(e));

        Sku out = skuService.findSkuById(7L);
        assertNotNull(out);
        assertEquals(7L, out.getId());
    }

    @Test
    public void deleteSkuById_deletesWhenExists() throws Exception {
        when(skuRepository.existsById(3L)).thenReturn(true);

        skuService.deleteSkuById(3L);

        verify(benefitRepository).deleteBySkuId(3L);
        verify(skuRepository).deleteById(3L);
    }

    @Test
    public void deleteSkuById_throwsWhenNotExists() {
        when(skuRepository.existsById(33L)).thenReturn(false);
        assertThrows(RecordNotFoundException.class, () -> skuService.deleteSkuById(33L));
    }

    @Test
    public void addBenefitToSku_requiresSku() {
        Benefit b = new Benefit();
        when(skuRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> skuService.addBenefitToSku(1L, b));
    }

    @Test
    public void addBenefitToSku_savesBenefit() throws Exception {
        Benefit b = new Benefit();
        b.setKeyEn("B");
        SkuEntity sku = buildSkuEntity(1L);
        when(skuRepository.findById(1L)).thenReturn(Optional.of(sku));
        when(benefitRepository.save(any(BenefitEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        com.apogee.product.models.Benefit out = skuService.addBenefitToSku(1L, b);
        assertNotNull(out);
    }

    @Test
    public void getSkuBenefits_returnsList() throws Exception {
        BenefitEntity be = buildBenefitEntity(8L, 2L);
        when(benefitRepository.findBySkuId(2L)).thenReturn(List.of(be));

        List<com.apogee.product.models.Benefit> out = skuService.getSkuBenefits(2L);
        assertEquals(1, out.size());
    }

    @Test
    public void removeBenefitFromSku_throwsWhenNotFound() {
        when(skuRepository.findByIdAndBenefitsId(1L, 99L)).thenReturn(Optional.empty());
        assertThrows(DBException.class, () -> skuService.removeBenefitFromSku(1L, 99L));
    }

    @Test
    public void assignTagToSku_throwsWhenDuplicate() {
        when(skuRepository.findByIdAndTagsId(1L, 10L)).thenReturn(Optional.of(new SkuEntity()));
        assertThrows(DBException.class, () -> skuService.assignTagToSku(1L, 10L));
    }

    @Test
    public void assignTagToSku_successAssigns() throws Exception {
        TagEntity t = buildTagEntity(10L, null);
        SkuEntity sku = buildSkuEntity(1L);
        sku.setTags(new ArrayList<>());

        when(tagRepository.findById(10L)).thenReturn(Optional.of(t));
        when(skuRepository.findById(1L)).thenReturn(Optional.of(sku));
        when(skuRepository.save(any(SkuEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        Sku out = skuService.assignTagToSku(1L, 10L);
        assertNotNull(out);
        assertEquals(1, out.getTags().size());
        assertEquals(10L, out.getTags().get(0).getId());
    }

    @Test
    public void getTagsForSku_returnsList() throws Exception {
        TagEntity t = buildTagEntity(100L, null);
        when(tagRepository.findByItemsId(5L)).thenReturn(List.of(t));

        List<Tag> out = skuService.getTagsForSku(5L);
        assertEquals(1, out.size());
    }

    @Test
    public void removeTagFromSku_throwsWhenNotFound() {
        when(skuRepository.findByIdAndTagsId(1L, 90L)).thenReturn(Optional.empty());
        assertThrows(DBException.class, () -> skuService.removeTagFromSku(1L, 90L));
    }
}
