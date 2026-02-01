package com.apogee.product.controllers;

import com.apogee.product.backingservice.TagBackingService;
import com.apogee.product.dtos.inputs.TagDto;
import com.apogee.product.dtos.output.AllTagsResponseDto;
import com.apogee.product.dtos.output.TagResponseDto;
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
public class TagController {

    @Autowired
    private TagBackingService tagBackingService;

    @PostMapping("/tags")
    @Operation(summary = "Add a new tag", description = "This endpoint allows you to add a new tag to the system.", tags = {"tags"})
    @ApiResponse(responseCode = "200", description = "Tag added successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TagResponseDto.class)))
    @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse")
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<Response> addTag(@RequestBody TagDto tagDto) throws Exception {

        TagResponseDto response = tagBackingService.addTag(tagDto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PutMapping("/tags")
    @Operation(summary = "Update an existing tag", description = "This endpoint allows you to update an existing tag in the system.", tags = {"tags"})
    @ApiResponse(responseCode = "200", description = "Tag updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TagResponseDto.class)))
    @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse")
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<Response> updateTag(@RequestBody TagDto tagDto) throws Exception {

        TagResponseDto response = tagBackingService.updateTag(tagDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/tags")
    @Operation(summary = "Get all tags", description = "This endpoint retrieves all tags available in the system.", tags = {"tags"})
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AllTagsResponseDto.class)))
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<AllTagsResponseDto> getAllCategories() throws Exception {

        AllTagsResponseDto response = tagBackingService.getAllTags();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/tags/{tagId}")
    @Operation(summary = "Delete a tag", description = "This endpoint allows you to delete a tag by its ID.")
    @ApiResponse(responseCode = "200", description = "Tag deleted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TagResponseDto.class)))
    @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse")
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<Response> deleteTag(@PathVariable("tagId") Long tagId) throws Exception {

        TagResponseDto response = tagBackingService.deleteTagById(tagId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/tags/{tagId}")
    @Operation(summary = "Get a tag by ID", description = "This endpoint retrieves a specific tag by its ID.")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TagResponseDto.class)))
    @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse")
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<Response> getTag(@PathVariable("tagId") Long tagId) throws Exception {

        TagResponseDto response = tagBackingService.getTagById(tagId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
