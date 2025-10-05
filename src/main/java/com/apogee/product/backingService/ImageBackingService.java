package com.apogee.product.backingService;

import com.apogee.product.dtos.output.AllImagesResponseDto;
import com.apogee.product.dtos.inputs.ImageDto;
import com.apogee.product.dtos.output.ImageResponseDto;
import com.apogee.product.dtos.output.SuccessfulResponse;
import com.apogee.product.models.Image;
import com.apogee.product.services.ImageService;
import com.apogee.product.utilities.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.apogee.product.utilities.Utilities.transformCollection;

@Service
public class ImageBackingService {

    @Autowired
    private ImageService imageService;

    public ImageResponseDto addImage(ImageDto imageDto) throws Exception {

        ImageResponseDto response = new ImageResponseDto();

        Image savedImage = this.imageService.saveImage(Mapper.map(imageDto, Image.class));

        response.setImage(Mapper.map(savedImage, ImageDto.class));

        return response;
    }

    public ImageResponseDto updateImage(ImageDto imageDto) throws Exception {

        ImageResponseDto response = new ImageResponseDto();

        Image updatedImage = this.imageService.updateImage(Mapper.map(imageDto, Image.class));

        response.setImage(Mapper.map(updatedImage, ImageDto.class));

        return response;
    }

    public SuccessfulResponse deleteImageById(Long imageId) throws Exception {

        SuccessfulResponse response = new SuccessfulResponse();

        this.imageService.deleteImageById(imageId);

        return response;
    }

    public AllImagesResponseDto getAllImages() throws Exception {

        AllImagesResponseDto response = new AllImagesResponseDto();

        List<Image> allImages = this.imageService.findAllImages();

        List<ImageDto> imageDtoList = transformCollection(allImages, ImageDto.class);

        response.setImages(imageDtoList);

        return response;
    }

    public ImageResponseDto getImageById(Long imageId) throws Exception {

        ImageResponseDto response = new ImageResponseDto();

        Image image = this.imageService.findImageById(imageId);

        response.setImage(Mapper.map(image, ImageDto.class));

        return response;

    }

    public AllImagesResponseDto addImages(List<ImageDto> imageDtos) throws Exception {

        AllImagesResponseDto response = new AllImagesResponseDto();

        List<Image> savedImages = this.imageService.saveImages(
                transformCollection(imageDtos, Image.class)
        );

        List<ImageDto> savedImagesDtos = transformCollection(savedImages, ImageDto.class);
        response.setImages(savedImagesDtos);

        return response;
    }

    public AllImagesResponseDto getImagesByParentItemId(Long productId) throws Exception {

        AllImagesResponseDto response = new AllImagesResponseDto();

        List<Image> images = this.imageService.findImagesByParentItemId(productId);

        List<ImageDto> imageDtos = transformCollection(images, ImageDto.class);
        response.setImages(imageDtos);

        return response;
    }

    public SuccessfulResponse deleteImagesByParentItemId(Long productId) throws Exception {

        SuccessfulResponse response = new SuccessfulResponse();

        this.imageService.deleteImagesByParentItemId(productId);

        return response;
    }

    public ImageResponseDto addImageToParentItem(Long parentItemId, ImageDto imageDto) throws Exception {

        ImageResponseDto response = new ImageResponseDto();

        Image savedImage = this.imageService.addImageToParentItem(parentItemId, Mapper.map(imageDto, Image.class));

        response.setImage(Mapper.map(savedImage, ImageDto.class));

        return response;
    }

    public SuccessfulResponse removeImageFromParentItem(Long parentItemId, Long imageId) throws Exception {

        SuccessfulResponse response = new SuccessfulResponse();

        this.imageService.removeImageFromParentItem(parentItemId, imageId);

        return response;
    }
}
