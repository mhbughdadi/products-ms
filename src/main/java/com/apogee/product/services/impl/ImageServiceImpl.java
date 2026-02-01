package com.apogee.product.services.impl;

import com.apogee.product.entities.ImageEntity;
import com.apogee.product.entities.ParentImageEntity;
import com.apogee.product.entities.ParentImageId;
import com.apogee.product.entities.ParentItemEntity;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.models.Image;
import com.apogee.product.repositories.ImageRepository;
import com.apogee.product.repositories.ParentImageRepository;
import com.apogee.product.repositories.ParentItemRepository;
import com.apogee.product.services.ImageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.apogee.product.constants.ProductsConstant.PRODUCT_IMAGE_NOT_FOUND;
import static com.apogee.product.constants.ProductsConstant.RECORD_NOT_FOUND;
import static com.apogee.product.utilities.Mapper.map;
import static com.apogee.product.utilities.Utilities.transform;
import static com.apogee.product.utilities.Utilities.transformCollection;

@Service
@Transactional
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ParentImageRepository parentImageRepository;

    @Autowired
    private ParentItemRepository parentItemRepository;

    @Override
    public List<Image> findAllImages() throws Exception {

        List<ImageEntity> imageEntities = imageRepository.findAll();

        if (!imageEntities.isEmpty()) {
            return transformCollection(imageEntities, Image.class);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Image> saveImages(List<Image> images) throws Exception {

        List<ImageEntity> savedEntities = imageRepository.saveAll(transformCollection(images, ImageEntity.class));

        return transformCollection(savedEntities, Image.class);
    }

    @Override
    public Image findImageById(Long imageId) throws Exception {

        ImageEntity imageEntity = imageRepository.findById(imageId).orElseThrow(() -> new RecordNotFoundException(RECORD_NOT_FOUND, imageId));

        return transform(imageEntity, Image.class, this::getImage);
    }

    @Override
    public Image updateImage(Image image) throws Exception {

        if (!imageRepository.existsById(image.getId())) {
            throw new RecordNotFoundException(RECORD_NOT_FOUND, image.getId());
        }

        ImageEntity updatedImage = imageRepository.save(map(image, ImageEntity.class));

        return transform(updatedImage, Image.class, this::getImage);
    }

    @Override
    public void deleteImageById(Long imageId) throws Exception {

        if (!imageRepository.existsById(imageId)) {
            throw new RecordNotFoundException(RECORD_NOT_FOUND, imageId);
        }

        imageRepository.deleteById(imageId);
    }

    @Override
    public Image saveImage(Image image) throws Exception {

        if (imageRepository.existsById(image.getId())) {
            throw new RecordNotFoundException(RECORD_NOT_FOUND, image.getId());
        }

        ImageEntity updatedImage = imageRepository.save(map(image, ImageEntity.class));

        return transform(updatedImage, Image.class, this::getImage);
    }

    @Override
    public List<Image> findImagesByParentItemId(Long parentItemId) throws Exception {

        ParentItemEntity parentItem = parentItemRepository.findById(parentItemId).orElseThrow(() -> new RecordNotFoundException(RECORD_NOT_FOUND, parentItemId));

        return transformCollection(parentItem.getParentImages(), Image.class, this::getParentImageDetails);
    }

    @Override
    public void deleteImagesByParentItemId(Long parentItemId) throws Exception {

        ParentItemEntity parentItem = parentItemRepository.findById(parentItemId).orElseThrow(() -> new RecordNotFoundException(RECORD_NOT_FOUND, parentItemId));

        if (parentItem.getParentImages() != null && !parentItem.getParentImages().isEmpty()) {
            parentImageRepository.deleteAll(parentItem.getParentImages());
        } else {
            throw new RecordNotFoundException(RECORD_NOT_FOUND, parentItemId);
        }

    }

    @Override
    public Image addImageToParentItem(Long parentItemId, Image image) throws Exception {

        ParentItemEntity parentItemEntity = parentItemRepository.findById(parentItemId).orElseThrow(() -> new RecordNotFoundException(RECORD_NOT_FOUND, parentItemId));
        ImageEntity imageEntity = imageRepository.findById(image.getId()).orElseThrow(() -> new RecordNotFoundException(RECORD_NOT_FOUND, parentItemId));

        ParentImageEntity parentImageEntity = new ParentImageEntity();
        parentImageEntity.setParentItem(parentItemEntity);
        parentImageEntity.setImage(imageEntity);
        parentImageEntity.setIsActive(image.isActive());
        parentImageEntity.setSortOrder(image.getSortOrder());
        parentImageEntity.setType(image.getType());

        ParentImageEntity savedParentImage = parentImageRepository.save(parentImageEntity);


        return transform(savedParentImage, Image.class, this::getParentImageDetails);
    }

    @Override
    public void removeImageFromParentItem(Long parentItemId, Long imageId) throws Exception {

        ParentImageId parentImageId = new ParentImageId(parentItemId, imageId);

        if (parentImageRepository.existsById(parentImageId)) {
            parentImageRepository.deleteById(parentImageId);
        } else {
            throw new RecordNotFoundException(PRODUCT_IMAGE_NOT_FOUND, parentItemId, imageId);
        }

    }

    private Image getImage(ImageEntity imageEntity, Image image) {

        image.setId(imageEntity.getId());
        image.setAssigned(imageEntity.getParentImages() != null && !imageEntity.getParentImages().isEmpty());

        return image;
    }

    private Image getParentImageDetails(ParentImageEntity parentImageEntity, Image image) {

        image.setActive(parentImageEntity.getIsActive());
        image.setSortOrder(parentImageEntity.getSortOrder());
        image.setType(parentImageEntity.getType());

        return getImage(parentImageEntity.getImage(), image);
    }


}
