package com.apogee.product.controllers;

import com.apogee.product.backingservice.SkuBackingService;
import com.apogee.product.dtos.inputs.BenefitDto;
import com.apogee.product.dtos.inputs.SkuDto;
import com.apogee.product.dtos.output.AllBenefitResponseDto;
import com.apogee.product.dtos.output.AllSkusResponseDto;
import com.apogee.product.dtos.output.AllTagsResponseDto;
import com.apogee.product.dtos.output.BenefitResponseDto;
import com.apogee.product.dtos.output.Response;
import com.apogee.product.dtos.output.SkuResponseDto;
import com.apogee.product.dtos.output.SuccessfulResponse;
import com.apogee.product.exceptions.DBException;
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

@RestController
public class SkuController {

    @Autowired
    private SkuBackingService skuBackingService;

    @GetMapping("/skus")
    @Operation(summary = "Get all skus", description = "This endpoint retrieves all skus available in the system.", tags = {"skus"})
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AllSkusResponseDto.class)))
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<Response> allSkus() throws MapperException {

        AllSkusResponseDto response = skuBackingService.getAllSkus();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/skus/{skuId}")
    @Operation(summary = "Find sku by ID", description = "This endpoint retrieves a sku by its ID.", tags = {"skus"})
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SkuResponseDto.class)))
    @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse")
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<Response> findSku(@PathVariable("skuId") Long skuId) throws MapperException, RecordNotFoundException {

        SkuResponseDto response = skuBackingService.getSkuById(skuId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/skus")
    @Operation(summary = "Add a new sku", description = "This endpoint allows you to add a new sku to the system.", tags = {"skus"})
    @ApiResponse(responseCode = "200", description = "Sku added successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SkuResponseDto.class)))
    @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse")
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<Response> addSku(@RequestBody SkuDto sku) throws MapperException, RecordNotFoundException {

        SkuResponseDto response = skuBackingService.addSku(sku);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/skus")
    @Operation(summary = "Update an existing sku", description = "This endpoint allows you to update an existing sku in the system.", tags = {"skus"})
    @ApiResponse(responseCode = "200", description = "Sku updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SkuResponseDto.class)))
    @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse")
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<Response> updateSku(@RequestBody SkuDto sku) throws MapperException, RecordNotFoundException {

        Response response = this.skuBackingService.updateSku(sku);

        return new ResponseEntity<>(response, HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping( "/skus/{skuId}")
    @Operation(summary = "Delete a sku", description = "This endpoint allows you to delete a sku from the system by its ID.", tags = {"skus"})
    @ApiResponse(responseCode = "200", description = "Sku deleted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessfulResponse.class)))
    @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse")
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<Response> deleteSku(@PathVariable("skuId") Long skuId) throws MapperException, RecordNotFoundException {

        this.skuBackingService.deleteSku(skuId);

        return new ResponseEntity<>(new SuccessfulResponse(), HttpStatus.OK);
    }

    @PostMapping("/skus/{skuId}/tags/{tagId}")
    @Operation(summary = "Assign Tag to Sku", description = "This endpoint assigns Tag with tagId to Sku with skuId.")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SkuResponseDto.class)))
    @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse")
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<Response> assignTag(@PathVariable("skuId") Long skuId, @PathVariable("tagId") Long tagId) throws MapperException, RecordNotFoundException, DBException {

        SkuResponseDto response = skuBackingService.assignTag(skuId, tagId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/skus/{skuId}/tags/{tagId}")
    @Operation(summary = "Remove Tag From Sku", description = "This endpoint deletes Tag with tagId from Sku with skuId.")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessfulResponse.class)))
    @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse")
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<Response> removeTag(@PathVariable("skuId") Long skuId, @PathVariable("tagId") Long tagId) throws MapperException, DBException {

        SuccessfulResponse response = skuBackingService.removeTag(skuId, tagId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/skus/{skuId}/tags")
    @Operation(summary = "Fetch Sku Tags", description = "This endpoint fetches Tags assigned to Sku with skuId.")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AllTagsResponseDto.class)))
    @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse")
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<Response> assignTag(@PathVariable("skuId") Long skuId) throws MapperException {

        AllTagsResponseDto response = skuBackingService.fetchSkuTags(skuId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/skus/{skuId}/benefits")
    @Operation(summary = "Fetch Sku benefits", description = "This endpoint fetches benefits assigned to Sku with skuId.")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AllBenefitResponseDto.class)))
    @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse")
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<Response> fetchSkuBenefits(@PathVariable("skuId") Long skuId) throws MapperException {

        AllBenefitResponseDto response = skuBackingService.getSkuBenefits(skuId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/skus/{skuId}/benefits")
    @Operation(summary = "Add Sku benefits", description = "This endpoint adds Benefits to Sku with skuId.")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BenefitResponseDto.class)))
    @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse")
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<Response> addSkuBenefit(@PathVariable("skuId") Long skuId, @RequestBody BenefitDto benefit) throws MapperException, RecordNotFoundException {

        BenefitResponseDto response = skuBackingService.addSkuBenefit(skuId, benefit);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("/skus/{skuId}/benefits/{benefitId}")
    @Operation(summary = "Remove Sku Benefit", description = "This endpoint deletes Benefit with benefitId from Sku with skuId.")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessfulResponse.class)))
    @ApiResponse(responseCode = "404", ref = "#/components/schemas/FailureResponse")
    @ApiResponse(responseCode = "500", ref = "#/components/schemas/FailureResponse")
    public ResponseEntity<Response> removeSkuBenefit(@PathVariable("skuId") Long skuId, @PathVariable("benefitId") Long benefitId) throws MapperException, RecordNotFoundException {

        SuccessfulResponse response = skuBackingService.removeSkuBenefit(skuId, benefitId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
