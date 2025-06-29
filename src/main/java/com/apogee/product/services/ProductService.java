package com.apogee.product.services;

import com.apogee.product.models.Product;

import java.util.List;

public interface ProductService {

    List<Product> findAllProducts() throws Exception;

    Product addProduct(Product product) throws Exception;

    Product findProductById(Long productId) throws Exception;

    void deleteProductById(Long productId) throws Exception;
}
