package com.apogee.product.controllers;

import com.apogee.product.backingService.CategoryBackingService;
import com.apogee.product.dtos.inputs.CategoryDto;
import com.apogee.product.dtos.output.AllCategoriesResponseDto;
import com.apogee.product.dtos.output.CategoryResponseDto;
import com.apogee.product.dtos.output.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController {

    @Autowired
    private CategoryBackingService categoryBackingService;

    @PostMapping("/categories")
    @Operation(summary = "Add a new category", description = "This endpoint allows you to add a new category to the system.")
    public ResponseEntity<Response> addCategory(@RequestBody CategoryDto categoryDto) throws Exception {

        CategoryResponseDto response = categoryBackingService.addCategory(categoryDto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/categories")
    @Operation(summary = "Update an existing category", description = "This endpoint allows you to update an existing category in the system.")
    public ResponseEntity<Response> updateCategory(@RequestBody CategoryDto category) throws Exception {

        CategoryResponseDto response = categoryBackingService.updateCategory(category);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/categories")
    @Operation(summary = "Get all categories", description = "This endpoint retrieves all categories available in the system.", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AllCategoriesResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Response.class)
                    )
            )})
    public ResponseEntity<AllCategoriesResponseDto> getAllCategories() throws Exception {

        AllCategoriesResponseDto response = categoryBackingService.getAllCategories();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/categories/{categoryId}")
    @Operation(summary = "Delete a category", description = "This endpoint allows you to delete a category by its ID.")
    public ResponseEntity<Response> deleteCategory(@PathVariable("categoryId") Long categoryId) throws Exception {

        CategoryResponseDto response = categoryBackingService.deleteCategoryById(categoryId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/categories/{categoryId}")
    @Operation(summary = "Get a category by ID", description = "This endpoint retrieves a specific category by its ID.")
    public ResponseEntity<Response> getCategory(@PathVariable("categoryId") Long categoryId) throws Exception {

        CategoryResponseDto response = categoryBackingService.getCategoryById(categoryId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
