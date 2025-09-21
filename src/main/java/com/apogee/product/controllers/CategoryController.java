package com.apogee.product.controllers;

import com.apogee.product.backingService.CategoryBackingService;
import com.apogee.product.dtos.inputs.CategoryDto;
import com.apogee.product.dtos.output.AllCategoriesResponseDto;
import com.apogee.product.dtos.output.AllTagsResponseDto;
import com.apogee.product.dtos.output.CategoryResponseDto;
import com.apogee.product.dtos.output.FailureResponse;
import com.apogee.product.dtos.output.Response;
import com.apogee.product.dtos.output.SuccessfulResponse;
import com.apogee.product.dtos.output.TagResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Add a new category", description = "This endpoint allows you to add a new category to the system.", tags = {"categories"})
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Category added successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponseDto.class))), @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse"), @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")})
    public ResponseEntity<Response> addCategory(@RequestBody CategoryDto categoryDto) throws Exception {

        CategoryResponseDto response = categoryBackingService.addCategory(categoryDto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PutMapping("/categories")
    @Operation(summary = "Update an existing category", description = "This endpoint allows you to update an existing category in the system.", tags = {"categories"})
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Category updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponseDto.class))), @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse"), @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")})
    public ResponseEntity<Response> updateCategory(@RequestBody CategoryDto category) throws Exception {

        CategoryResponseDto response = categoryBackingService.updateCategory(category);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/categories")
    @Operation(summary = "Get all categories", description = "This endpoint retrieves all categories available in the system.", tags = {"categories"})
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AllCategoriesResponseDto.class))), @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")})
    public ResponseEntity<AllCategoriesResponseDto> getAllCategories() throws Exception {

        AllCategoriesResponseDto response = categoryBackingService.getAllCategories();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/categories/{categoryId}")
    @Operation(summary = "Delete a category", description = "This endpoint allows you to delete a category by its ID.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Category deleted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponseDto.class))), @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse"), @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")})
    public ResponseEntity<Response> deleteCategory(@PathVariable("categoryId") Long categoryId) throws Exception {

        CategoryResponseDto response = categoryBackingService.deleteCategoryById(categoryId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/categories/{categoryId}")
    @Operation(summary = "Get a category by ID", description = "This endpoint retrieves a specific category by its ID.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponseDto.class))), @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse"), @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")})
    public ResponseEntity<Response> getCategory(@PathVariable("categoryId") Long categoryId) throws Exception {

        CategoryResponseDto response = categoryBackingService.getCategoryById(categoryId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/categories/{categoryId}/tags/{tagId}")
    @Operation(summary = "Assign Tag to Category", description = "This endpoint assigns Tag with tagId to Category with categoryId.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TagResponseDto.class))), @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse"), @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")})
    public ResponseEntity<Response> assignTag(@PathVariable("categoryId") Long categoryId, @PathVariable("tagId") Long tagId) throws Exception {

        TagResponseDto response = categoryBackingService.assignTag(categoryId, tagId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/categories/{categoryId}/tags/{tagId}")
    @Operation(summary = "Remove Tag From Category", description = "This endpoint deletes Tag with tagId from Category with categoryId.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessfulResponse.class))), @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse"), @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")})
    public ResponseEntity<Response> removeTag(@PathVariable("categoryId") Long categoryId, @PathVariable("tagId") Long tagId) throws Exception {

        SuccessfulResponse response = categoryBackingService.removeTag(categoryId, tagId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/categories/{categoryId}/tags")
    @Operation(summary = "Fetch Category Tags", description = "This endpoint fetches Tags assigned to Category with categoryId.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AllTagsResponseDto.class))), @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse"), @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")})
    public ResponseEntity<Response> assignTag(@PathVariable("categoryId") Long categoryId) throws Exception {

        AllTagsResponseDto response = categoryBackingService.fetchCategoryTags(categoryId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }




}
