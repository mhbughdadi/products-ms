package com.apogee.product.services.impl;

import com.apogee.product.entities.BenefitEntity;
import com.apogee.product.entities.ProductEntity;
import com.apogee.product.entities.SkuEntity;
import com.apogee.product.entities.TagEntity;
import com.apogee.product.exceptions.DBException;
import com.apogee.product.exceptions.MapperException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.models.Benefit;
import com.apogee.product.models.Sku;
import com.apogee.product.models.Tag;
import com.apogee.product.repositories.BenefitRepository;
import com.apogee.product.repositories.ProductRepository;
import com.apogee.product.repositories.SkuRepository;
import com.apogee.product.repositories.TagRepository;
import com.apogee.product.services.SkuService;
import com.apogee.product.utilities.Mapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.apogee.product.constants.ProductsConstant.ERROR_CATEGORY_TAG_ALREADY_EXISTS;
import static com.apogee.product.constants.ProductsConstant.ERROR_RECORD_NOT_FOUND;
import static com.apogee.product.constants.ProductsConstant.ERROR_SKU_TAG_NOT_FOUND;
import static com.apogee.product.utilities.Utilities.transform;
import static com.apogee.product.utilities.Utilities.transformCollection;

@Service
@Transactional
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuRepository skuRepository;

    @Autowired
    private BenefitRepository benefitRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Sku> findAllSkus() throws MapperException {

        List<SkuEntity> skuEntities = skuRepository.findAll();

        if (!skuEntities.isEmpty()) {

            return transformCollection(skuEntities, Sku.class, this::getSku);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Sku addSku(Sku sku) throws MapperException, RecordNotFoundException {

        SkuEntity transientSku = transform(sku, SkuEntity.class);

        ProductEntity productEntity = productRepository.findById(sku.getProductId())
                .orElseThrow(() -> new RecordNotFoundException(ERROR_RECORD_NOT_FOUND, sku.getProductId()));
        transientSku.setProduct(productEntity);

        SkuEntity savedEntity = skuRepository.save(transientSku);

        return transform(savedEntity, Sku.class, this::getSku);
    }

    @Override
    public Sku updateSku(Sku sku) throws MapperException, RecordNotFoundException {

        if (!this.skuRepository.existsById(sku.getId()) || sku.getProductId() == null) {
            throw new RecordNotFoundException(ERROR_RECORD_NOT_FOUND, sku.getId());
        }

        SkuEntity skuEntity = transform(sku, SkuEntity.class, this::setSkuId);

        return transform(this.skuRepository.save(skuEntity), Sku.class, this::getSku);
    }

    @Override
    public Sku findSkuById(Long skuId) throws MapperException , RecordNotFoundException {

        Optional<SkuEntity> skuEntityOptional = this.skuRepository.findById(skuId);

        return transform(skuEntityOptional.orElseThrow(() -> new RecordNotFoundException(ERROR_RECORD_NOT_FOUND, skuId)), Sku.class, this::getSku);
    }

    @Override
    public void deleteSkuById(Long skuId) throws MapperException, RecordNotFoundException {

        if (this.skuRepository.existsById(skuId)) {

            this.benefitRepository.deleteBySkuId(skuId);
            this.skuRepository.deleteById(skuId);
        } else {
            throw new RecordNotFoundException(ERROR_RECORD_NOT_FOUND, skuId);
        }
    }

    @Override
    public Benefit addBenefitToSku(Long skuId, Benefit benefit) throws MapperException , RecordNotFoundException {

        SkuEntity sku = skuRepository.findById(skuId).orElseThrow(() -> new RecordNotFoundException(ERROR_RECORD_NOT_FOUND, skuId));
        BenefitEntity benefitEntity = transform(benefit, BenefitEntity.class);
        benefitEntity.setSku(sku);

        BenefitEntity savedBenefit = benefitRepository.save(benefitEntity);

        return transform(savedBenefit, Benefit.class, this::getBenefit);
    }

    @Override
    public List<Benefit> getSkuBenefits(Long skuId) throws MapperException {

        List<BenefitEntity> benefits = benefitRepository.findBySkuId(skuId);

        return transformCollection(benefits, Benefit.class, this::getBenefit);
    }

    @Override
    public void removeBenefitFromSku(Long skuId, Long benefitId) throws MapperException, RecordNotFoundException {

        SkuEntity found = skuRepository.findByIdAndBenefitsId(skuId, benefitId)
                .orElseThrow(
                        () -> new DBException(ERROR_RECORD_NOT_FOUND, SkuEntity.class, skuId, benefitId)
                );

        found.setBenefits(found.getBenefits().stream().filter(t -> !t.getId().equals(benefitId)).collect(Collectors.toCollection(ArrayList::new)));

        benefitRepository.deleteById(benefitId);
        skuRepository.save(found);
    }

    @Override
    public Sku assignTagToSku(Long skuId, Long tagId) throws MapperException, RecordNotFoundException, DBException {

        skuRepository.findByIdAndTagsId(skuId, tagId).ifPresent(existingAssignment -> {
            throw new DBException(ERROR_CATEGORY_TAG_ALREADY_EXISTS, SkuEntity.class, skuId, tagId);
        });

        TagEntity tag = tagRepository.findById(tagId).orElseThrow(() -> new RecordNotFoundException(ERROR_RECORD_NOT_FOUND, tagId));
        SkuEntity product = skuRepository.findById(skuId).orElseThrow(() -> new RecordNotFoundException(ERROR_RECORD_NOT_FOUND, skuId));
        product.getTags().add(tag);

        SkuEntity savedSku = skuRepository.save(product);

        return transform(savedSku, Sku.class, this::getSku);
    }

    @Override
    public List<Tag> getTagsForSku(Long skuId) throws MapperException {

        List<TagEntity> assignments = tagRepository.findByItemsId(skuId);

        return transformCollection(assignments, Tag.class, this::getTag);
    }

    @Override
    public void removeTagFromSku(Long skuId, Long tagId) throws MapperException, DBException {

        SkuEntity found = skuRepository.findByIdAndTagsId(skuId, tagId)
                .orElseThrow(
                        () -> new DBException(ERROR_SKU_TAG_NOT_FOUND, SkuEntity.class, skuId, tagId)
                );

        found.setTags(found.getTags().stream().filter(t -> !t.getId().equals(tagId)).collect(Collectors.toCollection(ArrayList::new)));

        skuRepository.save(found);
    }


    private Tag getTag(TagEntity entity, Tag model) {

        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setDescriptionAr(entity.getDescriptionAr());
        return model;
    }

    private Sku getSku(SkuEntity skuEntity, Sku model) throws MapperException {

        model.setId(skuEntity.getId());
        model.setTags(transformCollection(skuEntity.getTags(), Tag.class, this::getTag));
        model.setBenefits(transformCollection(skuEntity.getBenefits(), Benefit.class, this::getBenefit));
        return model;
    }

    private Benefit getBenefit(BenefitEntity entity, Benefit model) {

        model.setId(entity.getId());
        model.setSkuId(entity.getSku().getId());
        return model;
    }

    private SkuEntity setSkuId(Sku sku, SkuEntity skuEntity) {

        skuEntity.setId(sku.getId());
        this.productRepository.findById(sku.getProductId()).ifPresent(skuEntity::setProduct);

        return skuEntity;
    }
}
