package com.apogee.product.backingService;

import com.apogee.product.dtos.inputs.BenefitDto;
import com.apogee.product.dtos.inputs.SkuDto;
import com.apogee.product.dtos.output.AllBenefitResponseDto;
import com.apogee.product.dtos.output.AllSkusResponseDto;
import com.apogee.product.dtos.output.BenefitResponseDto;
import com.apogee.product.dtos.output.SkuResponseDto;
import com.apogee.product.dtos.output.SuccessfulResponse;
import com.apogee.product.models.Benefit;
import com.apogee.product.models.Sku;
import com.apogee.product.models.Tag;
import com.apogee.product.services.SkuService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkuBackingServiceTest {

    @Mock
    private SkuService skuService;

    @InjectMocks
    private SkuBackingService backingService;

    @Test
    void getAllSkus_and_add_and_get_and_update_and_delete() throws Exception {
        Sku s = new Sku();
        s.setId(1L);

        when(skuService.findAllSkus()).thenReturn(List.of(s));

        AllSkusResponseDto all = backingService.getAllSkus();
        assertEquals(1, all.getSkus().size());

        SkuDto dto = new SkuDto();
        dto.setTitleEn("n");

        Sku saved = new Sku();
        saved.setId(9L);
        saved.setTitleEn("n");

        when(skuService.addSku(any(Sku.class))).thenReturn(saved);
        when(skuService.findSkuById(9L)).thenReturn(saved);
        when(skuService.updateSku(any(Sku.class))).thenReturn(saved);
        doNothing().when(skuService).deleteSkuById(9L);

        SkuResponseDto added = backingService.addSku(dto);
        assertEquals(9L, added.getSku().getId());

        SkuResponseDto got = backingService.getSkuById(9L);
        assertEquals(9L, got.getSku().getId());

        SkuResponseDto upd = backingService.updateSku(dto);
        assertEquals("n", upd.getSku().getTitleEn());

        backingService.deleteSku(9L);
    }

    @Test
    void assign_remove_and_fetchTags_and_benefits() throws Exception {
        Tag t = new Tag();
        t.setId(77L);

        Sku s = new Sku();
        s.setId(5L);
        s.setTags(List.of(t));

        when(skuService.assignTagToSku(5L, 77L)).thenReturn(s);
        when(skuService.getTagsForSku(5L)).thenReturn(List.of(t));
        doNothing().when(skuService).removeTagFromSku(5L, 77L);

        SkuResponseDto assigned = backingService.assignTag(5L, 77L);
        assertEquals(5L, assigned.getSku().getId());
        assertEquals(1, assigned.getSku().getTags().size());

        var tags = backingService.fetchSkuTags(5L);
        assertEquals(1, tags.getTags().size());

        var rem = backingService.removeTag(5L, 77L);
        assertNotNull(rem);

        // benefits
        Benefit b = new Benefit();
        b.setId(55L);
        when(skuService.addBenefitToSku(eq(5L), any(Benefit.class))).thenReturn(b);
        when(skuService.getSkuBenefits(5L)).thenReturn(List.of(b));
        doNothing().when(skuService).removeBenefitFromSku(5L, 55L);

        BenefitResponseDto added = backingService.addSkuBenefit(5L, new BenefitDto());
        assertEquals(55L, added.getBenefit().getId());

        AllBenefitResponseDto benefits = backingService.getSkuBenefits(5L);
        assertEquals(1, benefits.getBenefits().size());

        SuccessfulResponse removed = backingService.removeSkuBenefit(5L, 55L);
        assertNotNull(removed);
    }

    @Test
    void getAllSkus_returnsEmpty_whenNone() throws Exception {
        when(skuService.findAllSkus()).thenReturn(List.of());
        var resp = backingService.getAllSkus();
        assertNotNull(resp);
        assertTrue(resp.getSkus().isEmpty());
    }

    @Test
    void assignTag_propagatesException() throws Exception {
        when(skuService.assignTagToSku(5L, 77L)).thenThrow(new RuntimeException("assign fail"));
        assertThrows(RuntimeException.class, () -> backingService.assignTag(5L, 77L));
    }

    @Test
    void getSkuBenefits_returnsEmpty_whenNone() throws Exception {
        when(skuService.getSkuBenefits(5L)).thenReturn(List.of());
        var resp = backingService.getSkuBenefits(5L);
        assertNotNull(resp);
        assertTrue(resp.getBenefits().isEmpty());
    }
}
