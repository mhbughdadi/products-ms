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

import static com.apogee.product.constants.ProductsConstant.RECORD_NOT_FOUND;
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
    public List<Sku> findAllSkus() throws Exception {

        List<SkuEntity> skuEntities = skuRepository.findAll();

        if (!skuEntities.isEmpty()) {

            return transformCollection(skuEntities, Sku.class, this::getSku);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Sku addSku(Sku sku) throws Exception {

        SkuEntity transientSku = Mapper.map(sku, SkuEntity.class);

        ProductEntity productEntity = productRepository.findById(sku.getProductId())
                .orElseThrow(() -> new RecordNotFoundException("Product not found", sku.getProductId()));
        transientSku.setProduct(productEntity);

        SkuEntity savedEntity = skuRepository.save(transientSku);

        return transform(savedEntity, Sku.class, this::getSku);
    }

    @Override
    public Sku updateSku(Sku sku) throws Exception {

        if (!this.skuRepository.existsById(sku.getId()) || sku.getProductId() == null) {
            throw new RecordNotFoundException(RECORD_NOT_FOUND, sku.getId());
        }

        SkuEntity skuEntity = transform(sku, SkuEntity.class, this::setSkuId);

        return transform(this.skuRepository.save(skuEntity), Sku.class, this::getSku);
    }

    @Override
    public Sku findSkuById(Long skuId) throws Exception {

        Optional<SkuEntity> skuEntityOptional = this.skuRepository.findById(skuId);

        return transform(skuEntityOptional.orElseThrow(() -> new RecordNotFoundException(RECORD_NOT_FOUND, skuId)), Sku.class, this::getSku);
    }

    @Override
    public void deleteSkuById(Long skuId) throws Exception {

        if (this.skuRepository.existsById(skuId)) {

            this.benefitRepository.deleteBySkuId(skuId);
            this.skuRepository.deleteById(skuId);
        } else {
            throw new RecordNotFoundException(RECORD_NOT_FOUND, skuId);
        }
    }

    @Override
    public Benefit addBenefitToSku(Long skuId, Benefit benefit) throws Exception {

        SkuEntity sku = skuRepository.findById(skuId).orElseThrow(() -> new RecordNotFoundException("Sku not found", skuId));
        BenefitEntity benefitEntity = Mapper.map(benefit, BenefitEntity.class);
        benefitEntity.setSku(sku);

        BenefitEntity savedBenefit = benefitRepository.save(benefitEntity);

        return transform(savedBenefit, Benefit.class, this::getBenefit);
    }

    @Override
    public List<Benefit> getSkuBenefits(Long skuId) throws Exception {

        List<BenefitEntity> benefits = benefitRepository.findBySkuId(skuId);

        return transformCollection(benefits, Benefit.class, this::getBenefit);
    }

    @Override
    public void removeBenefitFromSku(Long skuId, Long benefitId) throws Exception {

        SkuEntity found = skuRepository.findByIdAndBenefitsId(skuId, benefitId)
                .orElseThrow(
                        () -> new DBException("errors.not.found", SkuEntity.class, skuId, benefitId)
                );

        found.setBenefits(found.getBenefits().stream().filter(t -> !t.getId().equals(benefitId)).collect(Collectors.toCollection(ArrayList::new)));

        benefitRepository.deleteById(benefitId);
        skuRepository.save(found);
    }

    @Override
    public Sku assignTagToSku(Long skuId, Long tagId) throws Exception {

        skuRepository.findByIdAndTagsId(skuId, tagId).ifPresent(existingAssignment -> {
            throw new DBException("errors.duplicate.record", SkuEntity.class, skuId, tagId);
        });

        TagEntity tag = tagRepository.findById(tagId).orElseThrow(() -> new RecordNotFoundException("Tag not found", tagId));
        SkuEntity product = skuRepository.findById(skuId).orElseThrow(() -> new RecordNotFoundException("Sku not found", skuId));
        product.getTags().add(tag);

        SkuEntity savedSku = skuRepository.save(product);

        return transform(savedSku, Sku.class, this::getSku);
    }

    @Override
    public List<Tag> getTagsForSku(Long skuId) throws Exception {

        List<TagEntity> assignments = tagRepository.findByItemsId(skuId);

        return transformCollection(assignments, Tag.class, this::getTag);
    }

    @Override
    public void removeTagFromSku(Long skuId, Long tagId) throws Exception {

        SkuEntity found = skuRepository.findByIdAndTagsId(skuId, tagId)
                .orElseThrow(
                        () -> new DBException("errors.not.found", SkuEntity.class, skuId, tagId)
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

    private Sku getSku(SkuEntity skuEntity, Sku model) throws Exception {

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
