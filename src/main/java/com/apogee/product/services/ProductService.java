package com.apogee.product.services;

import com.apogee.product.exceptions.DBException;
import com.apogee.product.exceptions.MapperException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.models.Product;
import com.apogee.product.models.Tag;
import java.util.List;

public interface ProductService {

    /**
     * Return all products
     * @return list of products (may be empty)
     * @throws MapperException when mapping fails
     */
    List<Product> findAllProducts() throws MapperException;

    /**
     * Persist a product
     * @param product input model
     * @return saved product model
     * @throws MapperException mapping failure
     */
    Product addProduct(Product product) throws MapperException;

    /**
     * Update a product
     * @param product model containing id and changes
     * @return updated product
     * @throws MapperException mapping failure
     * @throws RecordNotFoundException when target product does not exist
     */
    Product updateProduct(Product product) throws MapperException, RecordNotFoundException;

    /**
     * Find a product by id
     * @param productId id
     * @return product model
     * @throws MapperException mapping failure
     * @throws RecordNotFoundException when not found
     */
    Product findProductById(Long productId) throws MapperException, RecordNotFoundException;

    /**
     * Delete a product by id
     * @param productId id
     * @throws MapperException mapping failure
     * @throws RecordNotFoundException when not found
     */
    void deleteProductById(Long productId) throws MapperException, RecordNotFoundException;

    /**
     * Assign a tag to a product
     * @param productId product id
     * @param tagId tag id
     * @return updated product model
     * @throws MapperException mapping failure
     * @throws RecordNotFoundException when product or tag not found
     * @throws DBException on duplicate assignment or DB-level issues
     */
    Product assignTagToProduct(Long productId, Long tagId) throws MapperException, RecordNotFoundException, DBException;

    /**
     * Get tags assigned to a product
     * @param productId id
     * @return list of Tag models
     * @throws MapperException mapping failure
     */
    List<Tag> getTagsForProduct(Long productId) throws MapperException;

    /**
     * Remove a tag from a product
     * @param productId id
     * @param tagId id
     * @throws MapperException mapping failure
     * @throws RecordNotFoundException when the tag assignment is not found
     */
    void removeTagFromProduct(Long productId, Long tagId) throws MapperException, RecordNotFoundException;
}
