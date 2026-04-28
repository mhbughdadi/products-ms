package com.apogee.product.backingservice;

import com.apogee.product.dtos.output.AllImagesResponseDto;
import com.apogee.product.dtos.inputs.ImageDto;
import com.apogee.product.dtos.output.ImageResponseDto;
import com.apogee.product.dtos.output.SuccessfulResponse;
import com.apogee.product.models.Image;
import com.apogee.product.services.ImageService;
import com.apogee.product.exceptions.MapperException;
import com.apogee.product.exceptions.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.apogee.common.mapper.ObjectMapper.transform;
import static com.apogee.common.mapper.ObjectMapper.transformCollection;

@Service
public class ImageBackingService {

    @Autowired
    private ImageService imageService;

    public ImageResponseDto addImage(ImageDto imageDto) throws MapperException, RecordNotFoundException {

        ImageResponseDto response = new ImageResponseDto();

        Image savedImage = this.imageService.saveImage(transform(imageDto, Image.class));

        response.setImage(transform(savedImage, ImageDto.class));

        return response;
    }

    public ImageResponseDto updateImage(ImageDto imageDto) throws MapperException, RecordNotFoundException {

        ImageResponseDto response = new ImageResponseDto();

        Image updatedImage = this.imageService.updateImage(transform(imageDto, Image.class));

        response.setImage(transform(updatedImage, ImageDto.class));

        return response;
    }

    public SuccessfulResponse deleteImageById(Long imageId) throws MapperException, RecordNotFoundException {

        SuccessfulResponse response = new SuccessfulResponse();

        this.imageService.deleteImageById(imageId);

        return response;
    }

    public AllImagesResponseDto getAllImages() throws MapperException {

        AllImagesResponseDto response = new AllImagesResponseDto();

        List<Image> allImages = this.imageService.findAllImages();

        List<ImageDto> imageDtoList = transformCollection(allImages, ImageDto.class);

        response.setImages(imageDtoList);

        return response;
    }

    public ImageResponseDto getImageById(Long imageId) throws MapperException, RecordNotFoundException {

        ImageResponseDto response = new ImageResponseDto();

        Image image = this.imageService.findImageById(imageId);

        response.setImage(transform(image, ImageDto.class));

        return response;

    }

    public AllImagesResponseDto addImages(List<ImageDto> imageDtos) throws MapperException {

        AllImagesResponseDto response = new AllImagesResponseDto();

        List<Image> savedImages = this.imageService.saveImages(
                transformCollection(imageDtos, Image.class)
        );

        List<ImageDto> savedImagesDtos = transformCollection(savedImages, ImageDto.class);
        response.setImages(savedImagesDtos);

        return response;
    }

    public AllImagesResponseDto getImagesByParentItemId(Long productId) throws MapperException, RecordNotFoundException {

        AllImagesResponseDto response = new AllImagesResponseDto();

        List<Image> images = this.imageService.findImagesByParentItemId(productId);

        List<ImageDto> imageDtos = transformCollection(images, ImageDto.class);
        response.setImages(imageDtos);

        return response;
    }

    public SuccessfulResponse deleteImagesByParentItemId(Long productId) throws MapperException {

        SuccessfulResponse response = new SuccessfulResponse();

        this.imageService.deleteImagesByParentItemId(productId);

        return response;
    }

    public ImageResponseDto addImageToParentItem(Long parentItemId, ImageDto imageDto) throws MapperException, RecordNotFoundException {

        ImageResponseDto response = new ImageResponseDto();

        Image savedImage = this.imageService.addImageToParentItem(parentItemId, transform(imageDto, Image.class));

        response.setImage(transform(savedImage, ImageDto.class));

        return response;
    }

    public SuccessfulResponse removeImageFromParentItem(Long parentItemId, Long imageId) throws MapperException, RecordNotFoundException {

        SuccessfulResponse response = new SuccessfulResponse();

        this.imageService.removeImageFromParentItem(parentItemId, imageId);

        return response;
    }
}
