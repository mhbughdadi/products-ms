package com.apogee.product.backingService;

import com.apogee.product.dtos.output.*;
import com.apogee.product.mappings.Mapper;
import com.apogee.product.dtos.inputs.ProductDto;
import com.apogee.product.models.Image;
import com.apogee.product.models.Product;
import com.apogee.product.services.ImageService;
import com.apogee.product.services.ProductService;
import com.apogee.product.utilities.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;


@Service
public class ProductsBackingService {

    @Autowired
    private ProductService productService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private Mapper mapper;

    @Autowired
    ResourceBundleMessageSource messageSource;


    public AllProductsResponseDto getAllProducts() throws Exception {

        AllProductsResponseDto response = new AllProductsResponseDto();

        List<ProductOutputDto> allProducts = Utilities.transformCollection(productService.findAllProducts(), (product) -> mapper.map(product, ProductOutputDto.class));
        response.setProducts(allProducts);

        return response;
    }

    public AddProductResponseDto addProduct(ProductDto productDto) throws Exception {

        AddProductResponseDto response = new AddProductResponseDto();

        Product product = mapper.map(productDto, Product.class);
        Product savedProduct = productService.addProduct(product);

        if (savedProduct != null && product.getImages() != null && !product.getImages().isEmpty()) {

            product.getImages().forEach(image -> image.setProduct(savedProduct));
            List<Image> savedImages = this.imageService.saveImages(product.getImages());
            savedProduct.setImages(savedImages);
        }

        response.setProduct(mapper.map(savedProduct, ProductOutputDto.class));

        return response;
    }

    public FindProductResponseDto getProductById(Long productId) throws Exception {

        FindProductResponseDto response = new FindProductResponseDto();

        Product product = productService.findProductById(productId);
        response.setProduct(mapper.map(product, ProductOutputDto.class));

        return response;
    }

    public void deleteProduct(Long productId) throws Exception {

        this.productService.deleteProductById(productId);
    }

    public Response updateProduct(Object product) {

        return new FailureResponse(
                messageSource.getMessage("product.error.method.not.implemented", null, Locale.getDefault()),
                messageSource.getMessage("product.error.method.not.implemented", null, Locale.of("ar")));
    }
}
