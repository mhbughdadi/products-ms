package com.apogee.product.services;

import com.apogee.product.exceptions.DBException;
import com.apogee.product.exceptions.MapperException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.models.Product;
import com.apogee.product.models.Tag;
import java.util.List;

public interface ProductService {

    List<Product> findAllProducts() throws MapperException;

    Product addProduct(Product product) throws MapperException;

    Product updateProduct(Product product) throws MapperException, RecordNotFoundException;

    Product findProductById(Long productId) throws MapperException, RecordNotFoundException;

    void deleteProductById(Long productId) throws MapperException, RecordNotFoundException;

    Product assignTagToProduct(Long productId, Long tagId) throws MapperException, RecordNotFoundException, DBException;

    List<Tag> getTagsForProduct(Long productId) throws MapperException;

    void removeTagFromProduct(Long productId, Long tagId) throws MapperException, RecordNotFoundException;
}
