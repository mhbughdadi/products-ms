package com.apogee.product.services;

import com.apogee.product.models.Image;

import java.util.List;

public interface ImageService {

    List<Image> findAllImages() throws Exception;

    List<Image> saveImages(List<Image> images) throws Exception;
}
