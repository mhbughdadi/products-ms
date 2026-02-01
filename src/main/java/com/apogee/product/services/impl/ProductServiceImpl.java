package com.apogee.product.services.impl;

import com.apogee.product.entities.ProductEntity;
import com.apogee.product.entities.TagEntity;
import com.apogee.product.exceptions.DBException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.models.Tag;
import com.apogee.product.repositories.TagRepository;
import com.apogee.product.utilities.Mapper;
import com.apogee.product.models.Product;
import com.apogee.product.repositories.ProductRepository;
import com.apogee.product.services.ProductService;
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
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TagRepository tagRepository;

    @Override
    public List<Product> findAllProducts() throws Exception {

        List<ProductEntity> productEntities = productRepository.findAll();

        if (!productEntities.isEmpty()) {

            return transformCollection(productEntities, Product.class, this::getProduct);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Product addProduct(Product product) throws Exception {

        ProductEntity transientProduct = Mapper.map(product, ProductEntity.class);

        ProductEntity savedEntity = productRepository.save(transientProduct);

        return transform(savedEntity, Product.class, this::getProduct);
    }

    @Override
    public Product updateProduct(Product product) throws Exception {

        if (this.productRepository.existsById(product.getId())) {

            ProductEntity productEntity = Mapper.map(product, ProductEntity.class);

            return transform(this.productRepository.save(productEntity), Product.class, this::getProduct);
        } else {
            throw new RecordNotFoundException(RECORD_NOT_FOUND, product.getId());
        }
    }

    @Override
    public Product findProductById(Long productId) throws Exception {

        Optional<ProductEntity> productEntityOptional = this.productRepository.findById(productId);

        return transform(productEntityOptional.orElseThrow(() -> new RecordNotFoundException(RECORD_NOT_FOUND, productId)), Product.class, this::getProduct);
    }

    @Override
    public void deleteProductById(Long productId) throws Exception {

        if (this.productRepository.existsById(productId)) {

            this.productRepository.deleteById(productId);
        } else {
            throw new RecordNotFoundException(RECORD_NOT_FOUND, productId);
        }
    }

    @Override
    public Product assignTagToProduct(Long productId, Long tagId) throws Exception {

        productRepository.findByIdAndTagsId(productId, tagId).ifPresent(existingAssignment -> {
            throw new DBException("errors.duplicate.record", ProductEntity.class, productId, tagId);
        });

        TagEntity tag = tagRepository.findById(tagId).orElseThrow(() -> new RecordNotFoundException("Tag not found", tagId));
        ProductEntity product = productRepository.findById(productId).orElseThrow(() -> new RecordNotFoundException("Product not found", productId));
        product.getTags().add(tag);

        ProductEntity savedProduct = productRepository.save(product);

        return transform(savedProduct, Product.class, this::getProduct);
    }

    @Override
    public List<Tag> getTagsForProduct(Long productId) throws Exception {

        List<TagEntity> assignments = tagRepository.findByItemsId(productId);

        return transformCollection(assignments, Tag.class, this::getTag);
    }

    @Override
    public void removeTagFromProduct(Long productId, Long tagId) throws Exception {

        ProductEntity found = productRepository.findByIdAndTagsId(productId, tagId)
                .orElseThrow(
                        () -> new DBException("errors.not.found", ProductEntity.class, productId, tagId)
                );

        found.setTags(found.getTags().stream().filter(t -> !t.getId().equals(tagId)).collect(Collectors.toCollection(ArrayList::new)));

        productRepository.save(found);
    }

    private Product getProduct(ProductEntity productEntity, Product model) throws Exception {

        model.setId(productEntity.getId());
        model.setTags(transformCollection(productEntity.getTags(), Tag.class, this::getTag));
        return model;
    }

    private Tag getTag(TagEntity entity, Tag model) {

        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setDescriptionAr(entity.getDescriptionAr());
        return model;
    }
}
