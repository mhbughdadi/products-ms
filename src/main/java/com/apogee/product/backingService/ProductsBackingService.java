package com.apogee.product.backingService;

import com.apogee.product.dtos.inputs.TagDto;
import com.apogee.product.dtos.output.*;
import com.apogee.product.models.Tag;
import com.apogee.product.utilities.Mapper;
import com.apogee.product.dtos.inputs.ProductDto;
import com.apogee.product.models.Product;
import com.apogee.product.services.CurrencyService;
import com.apogee.product.services.ImageService;
import com.apogee.product.services.ProductService;
import com.apogee.product.utilities.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.apogee.product.utilities.Utilities.transformCollection;

@Service
public class ProductsBackingService {

    @Autowired
    private ProductService productService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CurrencyService currencyService;

    public AllProductsResponseDto getAllProducts() throws Exception {

        AllProductsResponseDto response = new AllProductsResponseDto();

        List<ProductOutputDto> allProducts = Utilities.transformCollection(productService.findAllProducts(), ProductOutputDto.class);
        response.setProducts(allProducts);

        return response;
    }

    public AddProductResponseDto addProduct(ProductDto productDto) throws Exception {

        AddProductResponseDto response = new AddProductResponseDto();

        Product product = Mapper.map(productDto, Product.class);
        Product savedProduct = productService.addProduct(product);
        response.setProduct(Mapper.map(savedProduct, ProductOutputDto.class));

        return response;
    }

    public FindProductResponseDto getProductById(Long productId) throws Exception {

        FindProductResponseDto response = new FindProductResponseDto();

        Product product = productService.findProductById(productId);
        response.setProduct(Mapper.map(product, ProductOutputDto.class));

        return response;
    }

    public void deleteProduct(Long productId) throws Exception {

        this.productService.deleteProductById(productId);
    }

    public AddProductResponseDto updateProduct(ProductDto product) throws Exception {

        AddProductResponseDto response = new AddProductResponseDto();

        Product updatedProduct = this.productService.updateProduct(Mapper.map(product, Product.class));
        response.setProduct(Mapper.map(updatedProduct, ProductOutputDto.class));

        return response;
    }

    public AddProductResponseDto assignTag(Long productId, Long tagId) throws Exception {

        AddProductResponseDto response = new AddProductResponseDto();

        Product product = this.productService.assignTagToProduct(productId, tagId);

        response.setProduct(Mapper.map(product, ProductOutputDto.class));

        return response;
    }

    public SuccessfulResponse removeTag(Long productId, Long tagId) throws Exception {

        SuccessfulResponse response = new SuccessfulResponse();

        this.productService.removeTagFromProduct(productId, tagId);

        return response;
    }

    public AllTagsResponseDto fetchProductTags(Long productId) throws Exception {

        AllTagsResponseDto response = new AllTagsResponseDto();

        List<Tag> categoryTags = this.productService.getTagsForProduct(productId);

        response.setTags(transformCollection(categoryTags, TagDto.class));

        return response;
    }


}
