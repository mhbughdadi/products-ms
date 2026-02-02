package com.apogee.product.controllers;

import com.apogee.product.backingservice.ProductsBackingService;
import com.apogee.product.dtos.inputs.ProductDto;
import com.apogee.product.dtos.output.*;
import com.apogee.product.exceptions.MapperException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.exceptions.DBException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    @Autowired
    private ProductsBackingService productsBackingService;

    @GetMapping("/products")
    @Operation(summary = "Get all products", description = "This endpoint retrieves all products available in the system.", tags = {"products"})
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AllProductsResponseDto.class)))
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<Response> allProducts() throws MapperException {

        AllProductsResponseDto response = productsBackingService.getAllProducts();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/products/{productId}")
    @Operation(summary = "Find product by ID", description = "This endpoint retrieves a product by its ID.", tags = {"products"})
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FindProductResponseDto.class)))
    @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse")
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<Response> findProduct(@PathVariable("productId") Long productId) throws MapperException, RecordNotFoundException {

        FindProductResponseDto response = productsBackingService.getProductById(productId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/products")
    @Operation(summary = "Add a new product", description = "This endpoint allows you to add a new product to the system.", tags = {"products"})
    @ApiResponse(responseCode = "200", description = "Product added successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddProductResponseDto.class)))
    @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse")
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<Response> addProduct(@RequestBody ProductDto product) throws MapperException {

        AddProductResponseDto response = productsBackingService.addProduct(product);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/products")
    @Operation(summary = "Update an existing product", description = "This endpoint allows you to update an existing product in the system.", tags = {"products"})
    @ApiResponse(responseCode = "200", description = "Product updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddProductResponseDto.class)))
    @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse")
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<Response> updateProduct(@RequestBody ProductDto product) throws MapperException, RecordNotFoundException {

        Response response = this.productsBackingService.updateProduct(product);

        return new ResponseEntity<>(response, HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/products/{productId}")
    @Operation(summary = "Delete a product", description = "This endpoint allows you to delete a product from the system by its ID.", tags = {"products"})
    @ApiResponse(responseCode = "200", description = "Product deleted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessfulResponse.class)))
    @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse")
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<Response> deleteProduct(@PathVariable("productId") Long productId) throws MapperException, RecordNotFoundException {

        this.productsBackingService.deleteProduct(productId);

        return new ResponseEntity<>(new SuccessfulResponse(), HttpStatus.OK);
    }

    @PostMapping("/products/{productId}/tags/{tagId}")
    @Operation(summary = "Assign Tag to Product", description = "This endpoint assigns Tag with tagId to Product with productId.")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddProductResponseDto.class)))
    @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse")
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<Response> assignTag(@PathVariable("productId") Long productId, @PathVariable("tagId") Long tagId) throws MapperException, RecordNotFoundException, DBException {

        AddProductResponseDto response = productsBackingService.assignTag(productId, tagId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/products/{productId}/tags/{tagId}")
    @Operation(summary = "Remove Tag From Product", description = "This endpoint deletes Tag with tagId from Product with productId.")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessfulResponse.class)))
    @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse")
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<Response> removeTag(@PathVariable("productId") Long productId, @PathVariable("tagId") Long tagId) throws MapperException, RecordNotFoundException {

        SuccessfulResponse response = productsBackingService.removeTag(productId, tagId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/products/{productId}/tags")
    @Operation(summary = "Fetch Product Tags", description = "This endpoint fetches Tags assigned to Product with productId.")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AllTagsResponseDto.class)))
    @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse")
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<Response> assignTag(@PathVariable("productId") Long productId) throws MapperException {

        AllTagsResponseDto response = productsBackingService.fetchProductTags(productId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
