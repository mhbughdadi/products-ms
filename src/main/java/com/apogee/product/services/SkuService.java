package com.apogee.product.services;

import com.apogee.product.exceptions.DBException;
import com.apogee.product.exceptions.MapperException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.models.Benefit;
import com.apogee.product.models.Sku;
import com.apogee.product.models.Tag;
import java.util.List;

public interface SkuService {

    List<Sku> findAllSkus() throws MapperException;

    Sku addSku(Sku sku) throws MapperException, RecordNotFoundException;

    Sku updateSku(Sku sku) throws MapperException, RecordNotFoundException;

    Sku findSkuById(Long skuId) throws MapperException, RecordNotFoundException;

    void deleteSkuById(Long skuId) throws MapperException, RecordNotFoundException;

    Benefit addBenefitToSku(Long skuId, Benefit benefit) throws MapperException, RecordNotFoundException;

    List<Benefit> getSkuBenefits(Long skuId) throws MapperException;

    void removeBenefitFromSku(Long skuId, Long benefitId) throws MapperException, RecordNotFoundException;

    Sku assignTagToSku(Long skuId, Long tagId) throws MapperException, RecordNotFoundException, DBException;

    List<Tag> getTagsForSku(Long skuId) throws MapperException;

    void removeTagFromSku(Long skuId, Long tagId) throws MapperException, DBException;
}
