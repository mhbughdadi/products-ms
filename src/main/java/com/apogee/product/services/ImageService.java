package com.apogee.product.services;

import com.apogee.product.models.Image;
import java.util.List;

public interface ImageService {

    List<Image> findAllImages() throws Exception;

    List<Image> saveImages(List<Image> images) throws Exception;

    Image findImageById(Long imageId) throws Exception;

    Image saveImage(Image image) throws Exception;

    Image updateImage(Image image) throws Exception;

    void deleteImageById(Long imageId) throws Exception;

    List<Image> findImagesByParentItemId(Long productId) throws Exception;

    void deleteImagesByParentItemId(Long productId) throws Exception;

    Image addImageToParentItem(Long parentItemId, Image image) throws Exception;

    void removeImageFromParentItem(Long parentItemId, Long imageId) throws Exception;
}
