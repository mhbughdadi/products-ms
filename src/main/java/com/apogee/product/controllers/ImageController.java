package com.apogee.product.controllers;

import com.apogee.product.backingservice.ImageBackingService;
import com.apogee.product.dtos.inputs.ImageDto;
import com.apogee.product.dtos.output.AllImagesResponseDto;
import com.apogee.product.dtos.output.Response;
import com.apogee.product.dtos.output.ImageResponseDto;
import com.apogee.product.dtos.output.SuccessfulResponse;
import com.apogee.product.exceptions.MapperException;
import com.apogee.product.exceptions.RecordNotFoundException;
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

import java.util.List;

@RestController
public class ImageController {

    @Autowired
    private ImageBackingService imageBackingService;

    @PostMapping("/images")
    @Operation(summary = "Add a new image", description = "This endpoint allows you to add a new image to the system.", tags = {"images"})
    @ApiResponse(responseCode = "200", description = "Image added successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ImageResponseDto.class)))
    @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse")
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<Response> addImage(@RequestBody ImageDto imageDto) throws MapperException, RecordNotFoundException {

        ImageResponseDto response = imageBackingService.addImage(imageDto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PutMapping("/images")
    @Operation(summary = "Update an existing image", description = "This endpoint allows you to update an existing image in the system.", tags = {"images"})
    @ApiResponse(responseCode = "200", description = "Image updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ImageResponseDto.class)))
    @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse")
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<Response> updateImage(@RequestBody ImageDto imageDto) throws MapperException, RecordNotFoundException {

        ImageResponseDto response = imageBackingService.updateImage(imageDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/images")
    @Operation(summary = "Get all images", description = "This endpoint retrieves all images available in the system.", tags = {"images"})
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AllImagesResponseDto.class)))
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<AllImagesResponseDto> getAllImages() throws MapperException {

        AllImagesResponseDto response = imageBackingService.getAllImages();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/images/{imageId}")
    @Operation(summary = "Delete a image", description = "This endpoint allows you to delete a image by its ID.")
    @ApiResponse(responseCode = "200", description = "Image deleted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessfulResponse.class)))
    @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse")
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<Response> deleteImage(@PathVariable("imageId") Long imageId) throws MapperException, RecordNotFoundException {

        SuccessfulResponse response = imageBackingService.deleteImageById(imageId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/images/{imageId}")
    @Operation(summary = "Get a image by ID", description = "This endpoint retrieves a specific image by its ID.")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ImageResponseDto.class)))
    @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse")
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<Response> getImage(@PathVariable("imageId") Long imageId) throws MapperException, RecordNotFoundException {

        ImageResponseDto response = imageBackingService.getImageById(imageId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/images/batch")
    @Operation(summary = "Add multiple images", description = "Add a list of images to the system.", tags = {"images"})
    @ApiResponse(responseCode = "200", description = "Images added successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AllImagesResponseDto.class)))
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<AllImagesResponseDto> addImages(@RequestBody List<ImageDto> imageDtos) throws MapperException {

        AllImagesResponseDto response = imageBackingService.addImages(imageDtos);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/images/parent/{parentItemId}")
    @Operation(summary = "Get images by parent item ID", description = "Retrieve all images for a given parent item.", tags = {"images"})
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AllImagesResponseDto.class)))
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<AllImagesResponseDto> getImagesByParentItemId(@PathVariable("parentItemId") Long parentItemId) throws MapperException, RecordNotFoundException {

        AllImagesResponseDto response = imageBackingService.getImagesByParentItemId(parentItemId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/images/parent/{parentItemId}")
    @Operation(summary = "Delete images by parent item ID", description = "Delete all images for a given parent item.", tags = {"images"})
    @ApiResponse(responseCode = "200", description = "Images deleted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessfulResponse.class)))
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<SuccessfulResponse> deleteImagesByParentItemId(@PathVariable("parentItemId") Long parentItemId) throws MapperException {

        SuccessfulResponse response = imageBackingService.deleteImagesByParentItemId(parentItemId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/images/parent/{parentItemId}")
    @Operation(summary = "Add image to parent item", description = "Add a single image to a parent item.", tags = {"images"})
    @ApiResponse(responseCode = "200", description = "Image added successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ImageResponseDto.class)))
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<ImageResponseDto> addImageToParentItem(@PathVariable("parentItemId") Long parentItemId, @RequestBody ImageDto imageDto) throws MapperException, RecordNotFoundException {

        ImageResponseDto response = imageBackingService.addImageToParentItem(parentItemId, imageDto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/images/{imageId}/parent/{parentItemId}")
    @Operation(summary = "Remove image from parent item", description = "Remove a specific image from a parent item.", tags = {"images"})
    @ApiResponse(responseCode = "200", description = "Image removed successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessfulResponse.class)))
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<SuccessfulResponse> removeImageFromParentItem(@PathVariable("parentItemId") Long parentItemId, @PathVariable("imageId") Long imageId) throws MapperException, RecordNotFoundException {

        SuccessfulResponse response = imageBackingService.removeImageFromParentItem(parentItemId, imageId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
