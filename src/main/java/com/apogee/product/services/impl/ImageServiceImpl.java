package com.apogee.product.services.impl;

import com.apogee.product.entities.ImageEntity;
import com.apogee.product.mappings.Mapper;
import com.apogee.product.models.Image;
import com.apogee.product.repositories.ImageRepository;
import com.apogee.product.services.ImageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.apogee.product.utilities.Utilities.transformCollection;

@Service
@Transactional
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private Mapper mapper;

    @Override
    public List<Image> findAllImages() throws Exception{

        List<ImageEntity> imageEntities = imageRepository.findAll();

        if (!imageEntities.isEmpty()) {
            return transformCollection(imageEntities, entity -> mapper.map(entity, Image.class));
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Image> saveImages(List<Image> images) throws Exception {

        List<ImageEntity> savedEntities = imageRepository.saveAll(transformCollection(images, (image) -> mapper.map(image, ImageEntity.class)));

        return transformCollection(savedEntities, (savedEntity) -> mapper.map(savedEntity, Image.class));
    }

}
