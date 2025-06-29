package com.apogee.product.services.impl;

import com.apogee.product.entities.ProductEntity;
import com.apogee.product.mappings.Mapper;
import com.apogee.product.models.Product;
import com.apogee.product.repositories.ProductRepository;
import com.apogee.product.services.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static com.apogee.product.utilities.Utilities.transformCollection;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    Mapper mapper;

    @Override
    public List<Product> findAllProducts() throws Exception {

        List<ProductEntity> productEntities = productRepository.findAll();

        if (!productEntities.isEmpty()) {
            return transformCollection(productEntities, entity -> mapper.map(entity, Product.class));
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Product addProduct(Product product) throws Exception {

        ProductEntity transientProduct = mapper.map(product, ProductEntity.class);
        ProductEntity savedEntity = productRepository.save(transientProduct);

        return mapper.map(savedEntity, Product.class);
    }

    @Override
    public Product findProductById(Long productId) throws Exception {

        AtomicReference<Product> productReference = new AtomicReference<>();
        Optional<ProductEntity> productEntityOptional = this.productRepository.findById(productId);

        productEntityOptional.ifPresent(productEntity -> {
            try {
                productReference.set(mapper.map(productEntity, Product.class));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return productReference.get();
    }

    @Override
    public void deleteProductById(Long productId) throws Exception {

        this.productRepository.deleteById(productId);
    }


}
