package com.apogee.product.services;

import com.apogee.product.exceptions.MapperException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.models.Image;
import java.util.List;

public interface ImageService {

    /**
     * Return all images
     * @return list of images
     * @throws MapperException mapping failure
     */
    List<Image> findAllImages() throws MapperException;

    /**
     * Save multiple images
     * @param images list of image models
     * @return saved image models
     * @throws MapperException mapping failure
     */
    List<Image> saveImages(List<Image> images) throws MapperException;

    /**
     * Find image by id
     * @param imageId id
     * @return image model
     * @throws MapperException mapping failure
     * @throws RecordNotFoundException when image not found
     */
    Image findImageById(Long imageId) throws MapperException, RecordNotFoundException;

    /**
     * Save a single image
     * @param image model
     * @return saved image
     * @throws MapperException mapping failure
     * @throws RecordNotFoundException when conflict or referenced data not found
     */
    Image saveImage(Image image) throws MapperException, RecordNotFoundException;

    /**
     * Update an image
     * @param image model
     * @return updated image
     * @throws MapperException mapping failure
     * @throws RecordNotFoundException when image not found
     */
    Image updateImage(Image image) throws MapperException, RecordNotFoundException;

    /**
     * Delete an image by id
     * @param imageId id
     * @throws MapperException mapping failure
     * @throws RecordNotFoundException when image not found
     */
    void deleteImageById(Long imageId) throws MapperException, RecordNotFoundException;

    /**
     * Find images associated with a parent item
     * @param productId parent id
     * @return list of images
     * @throws MapperException mapping failure
     * @throws RecordNotFoundException when parent item not found
     */
    List<Image> findImagesByParentItemId(Long productId) throws MapperException, RecordNotFoundException;

    /**
     * Delete images for a parent item
     * @param productId parent id
     * @throws MapperException mapping failure
     */
    void deleteImagesByParentItemId(Long productId) throws MapperException;

    /**
     * Associate an image with a parent item
     * @param parentItemId id
     * @param image model
     * @return association details as Image
     * @throws MapperException mapping failure
     * @throws RecordNotFoundException when parent or image not found
     */
    Image addImageToParentItem(Long parentItemId, Image image) throws MapperException, RecordNotFoundException;

    /**
     * Remove an image association from parent
     * @param parentItemId id
     * @param imageId id
     * @throws MapperException mapping failure
     * @throws RecordNotFoundException when association not found
     */
    void removeImageFromParentItem(Long parentItemId, Long imageId) throws MapperException, RecordNotFoundException;
}
