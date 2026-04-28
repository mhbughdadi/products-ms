package com.apogee.product.backingservice;

import com.apogee.product.dtos.inputs.TagDto;
import com.apogee.product.dtos.output.*;
import com.apogee.product.models.Tag;
import com.apogee.product.dtos.inputs.ProductDto;
import com.apogee.product.models.Product;
import com.apogee.product.services.ImageService;
import com.apogee.product.services.ProductService;
import com.apogee.product.exceptions.MapperException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.exceptions.DBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.apogee.common.mapper.ObjectMapper.transform;
import static com.apogee.common.mapper.ObjectMapper.transformCollection;

@Service
public class ProductsBackingService {

    @Autowired
    private ProductService productService;

    @Autowired
    private ImageService imageService;


    public AllProductsResponseDto getAllProducts() throws MapperException {

        AllProductsResponseDto response = new AllProductsResponseDto();

        List<ProductOutputDto> allProducts = transformCollection(productService.findAllProducts(), ProductOutputDto.class);
        response.setProducts(allProducts);

        return response;
    }

    public AddProductResponseDto addProduct(ProductDto productDto) throws MapperException {

        AddProductResponseDto response = new AddProductResponseDto();

        Product product = transform(productDto, Product.class);
        Product savedProduct = productService.addProduct(product);
        response.setProduct(transform(savedProduct, ProductOutputDto.class));

        return response;
    }

    public FindProductResponseDto getProductById(Long productId) throws MapperException, RecordNotFoundException {

        FindProductResponseDto response = new FindProductResponseDto();

        Product product = productService.findProductById(productId);
        response.setProduct(transform(product, ProductOutputDto.class));

        return response;
    }

    public void deleteProduct(Long productId) throws MapperException, RecordNotFoundException {

        this.productService.deleteProductById(productId);
    }

    public AddProductResponseDto updateProduct(ProductDto product) throws MapperException, RecordNotFoundException {

        AddProductResponseDto response = new AddProductResponseDto();

        Product updatedProduct = this.productService.updateProduct(transform(product, Product.class));
        response.setProduct(transform(updatedProduct, ProductOutputDto.class));

        return response;
    }

    public AddProductResponseDto assignTag(Long productId, Long tagId) throws MapperException, RecordNotFoundException, DBException {

        AddProductResponseDto response = new AddProductResponseDto();

        Product product = this.productService.assignTagToProduct(productId, tagId);

        response.setProduct(transform(product, ProductOutputDto.class));

        return response;
    }

    public SuccessfulResponse removeTag(Long productId, Long tagId) throws MapperException, RecordNotFoundException {

        SuccessfulResponse response = new SuccessfulResponse();

        this.productService.removeTagFromProduct(productId, tagId);

        return response;
    }

    public AllTagsResponseDto fetchProductTags(Long productId) throws MapperException {

        AllTagsResponseDto response = new AllTagsResponseDto();

        List<Tag> categoryTags = this.productService.getTagsForProduct(productId);

        response.setTags(transformCollection(categoryTags, TagDto.class));

        return response;
    }


}
