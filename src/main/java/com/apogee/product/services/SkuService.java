package com.apogee.product.services;

import com.apogee.product.exceptions.DBException;
import com.apogee.product.exceptions.MapperException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.models.Benefit;
import com.apogee.product.models.Sku;
import com.apogee.product.models.Tag;
import java.util.List;

public interface SkuService {

    /**
     * Return all SKUs
     * @return list of SKUs
     * @throws MapperException mapping failure
     */
    List<Sku> findAllSkus() throws MapperException;

    /**
     * Persist a SKU
     * @param sku input model
     * @return saved SKU
     * @throws MapperException mapping failure
     * @throws RecordNotFoundException when referenced product not found
     */
    Sku addSku(Sku sku) throws MapperException, RecordNotFoundException;

    /**
     * Update a SKU
     * @param sku model
     * @return updated SKU
     * @throws MapperException mapping failure
     * @throws RecordNotFoundException when SKU not found or product missing
     */
    Sku updateSku(Sku sku) throws MapperException, RecordNotFoundException;

    /**
     * Find a SKU by id
     * @param skuId id
     * @return SKU model
     * @throws MapperException mapping failure
     * @throws RecordNotFoundException when not found
     */
    Sku findSkuById(Long skuId) throws MapperException, RecordNotFoundException;

    /**
     * Delete a SKU
     * @param skuId id
     * @throws MapperException mapping failure
     * @throws RecordNotFoundException when not found
     */
    void deleteSkuById(Long skuId) throws MapperException, RecordNotFoundException;

    /**
     * Add a benefit to a SKU
     * @param skuId id
     * @param benefit benefit model
     * @return saved Benefit
     * @throws MapperException mapping failure
     * @throws RecordNotFoundException when SKU not found
     */
    Benefit addBenefitToSku(Long skuId, Benefit benefit) throws MapperException, RecordNotFoundException;

    /**
     * Get benefits for a SKU
     * @param skuId id
     * @return list of benefits
     * @throws MapperException mapping failure
     */
    List<Benefit> getSkuBenefits(Long skuId) throws MapperException;

    /**
     * Remove a benefit from a SKU
     * @param skuId id
     * @param benefitId id
     * @throws MapperException mapping failure
     * @throws RecordNotFoundException when benefit/sku association not found
     */
    void removeBenefitFromSku(Long skuId, Long benefitId) throws MapperException, RecordNotFoundException;

    /**
     * Assign a tag to a SKU
     * @param skuId id
     * @param tagId id
     * @return updated SKU
     * @throws MapperException mapping failure
     * @throws RecordNotFoundException when sku or tag not found
     * @throws DBException on duplicate assignment
     */
    Sku assignTagToSku(Long skuId, Long tagId) throws MapperException, RecordNotFoundException, DBException;

    /**
     * Get tags for a SKU
     * @param skuId id
     * @return list of Tag models
     * @throws MapperException mapping failure
     */
    List<Tag> getTagsForSku(Long skuId) throws MapperException;

    /**
     * Remove a tag from a SKU
     * @param skuId id
     * @param tagId id
     * @throws MapperException mapping failure
     * @throws DBException when tag assignment not found
     */
    void removeTagFromSku(Long skuId, Long tagId) throws MapperException, DBException;
}
