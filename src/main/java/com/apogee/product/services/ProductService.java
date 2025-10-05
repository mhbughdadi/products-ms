package com.apogee.product.services;

import com.apogee.product.models.Product;
import com.apogee.product.models.Tag;
import java.util.List;

public interface ProductService {

    List<Product> findAllProducts() throws Exception;

    Product addProduct(Product product) throws Exception;

    Product updateProduct(Product product) throws Exception;

    Product findProductById(Long productId) throws Exception;

    void deleteProductById(Long productId) throws Exception;

    Product assignTagToProduct(Long productId, Long tagId) throws Exception;

    List<Tag> getTagsForProduct(Long productId) throws Exception;

    void removeTagFromProduct(Long productId, Long tagId) throws Exception;
}
