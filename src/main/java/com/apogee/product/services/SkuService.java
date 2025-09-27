package com.apogee.product.services;

import com.apogee.product.models.Benefit;
import com.apogee.product.models.Sku;
import com.apogee.product.models.Tag;

import java.util.List;

public interface SkuService {

    List<Sku> findAllSkus() throws Exception;

    Sku addSku(Sku sku) throws Exception;

    Sku updateSku(Sku sku) throws Exception;

    Sku findSkuById(Long skuId) throws Exception;

    void deleteSkuById(Long skuId) throws Exception;

    Benefit addBenefitToSku(Long skuId, Benefit benefit) throws Exception;

    List<Benefit> getSkuBenefits(Long skuId) throws Exception;

    void removeBenefitFromSku(Long skuId, Long benefitId) throws Exception;

    Sku assignTagToSku(Long skuId, Long tagId) throws Exception;

    List<Tag> getTagsForSku(Long skuId) throws Exception;

    void removeTagFromSku(Long skuId, Long tagId) throws Exception;
}
