package com.apogee.product.services.impl;

import com.apogee.product.entities.ProductEntity;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.utilities.Mapper;
import com.apogee.product.models.Product;
import com.apogee.product.repositories.ProductRepository;
import com.apogee.product.services.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.apogee.product.utilities.Utilities.transform;
import static com.apogee.product.utilities.Utilities.transformCollection;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> findAllProducts() throws Exception {

        List<ProductEntity> productEntities = productRepository.findAll();

        if (!productEntities.isEmpty()) {

            return transformCollection(productEntities, Product.class, this::setProductId);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Product addProduct(Product product) throws Exception {

        ProductEntity transientProduct = Mapper.map(product, ProductEntity.class);

        ProductEntity savedEntity = productRepository.save(transientProduct);

        return transform(savedEntity, Product.class, this::setProductId);
    }

    @Override
    public Product updateProduct(Product product) throws Exception {

        if (this.productRepository.existsById(product.getId())) {
            
            ProductEntity productEntity = Mapper.map(product, ProductEntity.class);

            return transform(this.productRepository.save(productEntity), Product.class, this::setProductId);
        } else {
            throw new RecordNotFoundException("record.not.found", product.getId());
        }
    }

    @Override
    public Product findProductById(Long productId) throws Exception {

        Optional<ProductEntity> productEntityOptional = this.productRepository.findById(productId);

        return transform(productEntityOptional.orElseThrow(() -> new RecordNotFoundException("record.not.found", productId)), Product.class, this::setProductId);
    }

    @Override
    public void deleteProductById(Long productId) throws Exception {

        if (this.productRepository.existsById(productId)) {

            this.productRepository.deleteById(productId);
        } else {
            throw new RecordNotFoundException("record.not.found", productId);
        }
    }

    private Product setProductId(ProductEntity categoryEntity, Product product) {
        product.setId(categoryEntity.getId());
        return product;
    }


}
