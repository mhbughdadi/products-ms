package com.apogee.product.services;

import com.apogee.product.exceptions.MapperException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.models.Image;
import java.util.List;

public interface ImageService {

    List<Image> findAllImages() throws MapperException;

    List<Image> saveImages(List<Image> images) throws MapperException;

    Image findImageById(Long imageId) throws MapperException, RecordNotFoundException;

    Image saveImage(Image image) throws MapperException, RecordNotFoundException;

    Image updateImage(Image image) throws MapperException, RecordNotFoundException;

    void deleteImageById(Long imageId) throws MapperException, RecordNotFoundException;

    List<Image> findImagesByParentItemId(Long productId) throws MapperException, RecordNotFoundException;

    void deleteImagesByParentItemId(Long productId) throws MapperException;

    Image addImageToParentItem(Long parentItemId, Image image) throws MapperException, RecordNotFoundException;

    void removeImageFromParentItem(Long parentItemId, Long imageId) throws MapperException, RecordNotFoundException;
}
