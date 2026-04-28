package com.apogee.product.backingservice;

import com.apogee.product.dtos.inputs.BenefitDto;
import com.apogee.product.dtos.inputs.SkuDto;
import com.apogee.product.dtos.inputs.TagDto;
import com.apogee.product.dtos.output.AllBenefitResponseDto;
import com.apogee.product.dtos.output.BenefitResponseDto;
import com.apogee.product.dtos.output.SkuResponseDto;
import com.apogee.product.dtos.output.AllSkusResponseDto;
import com.apogee.product.dtos.output.AllTagsResponseDto;
import com.apogee.product.dtos.output.SkuOutputDto;
import com.apogee.product.dtos.output.SuccessfulResponse;
import com.apogee.product.models.Benefit;
import com.apogee.product.models.Sku;
import com.apogee.product.models.Tag;
import com.apogee.product.services.ImageService;
import com.apogee.product.services.SkuService;
import com.apogee.product.exceptions.MapperException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.exceptions.DBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.apogee.common.mapper.ObjectMapper.transform;
import static com.apogee.common.mapper.ObjectMapper.transformCollection;

@Service
public class SkuBackingService {

    @Autowired
    private SkuService skuService;

    @Autowired
    private ImageService imageService;

    public AllSkusResponseDto getAllSkus() throws MapperException {

        AllSkusResponseDto response = new AllSkusResponseDto();

        List<SkuOutputDto> allSkus = transformCollection(skuService.findAllSkus(), SkuOutputDto.class);
        response.setSkus(allSkus);

        return response;
    }

    public SkuResponseDto addSku(SkuDto productDto) throws MapperException, RecordNotFoundException {

        SkuResponseDto response = new SkuResponseDto();

        Sku product = transform(productDto, Sku.class);
        Sku savedSku = skuService.addSku(product);
        response.setSku(transform(savedSku, SkuOutputDto.class));

        return response;
    }

    public SkuResponseDto getSkuById(Long productId) throws MapperException, RecordNotFoundException {

        SkuResponseDto response = new SkuResponseDto();

        Sku product = skuService.findSkuById(productId);
        response.setSku(transform(product, SkuOutputDto.class));

        return response;
    }

    public void deleteSku(Long productId) throws MapperException, RecordNotFoundException {

        this.skuService.deleteSkuById(productId);
    }

    public SkuResponseDto updateSku(SkuDto product) throws MapperException, RecordNotFoundException {

        SkuResponseDto response = new SkuResponseDto();

        Sku updatedSku = this.skuService.updateSku(transform(product, Sku.class));
        response.setSku(transform(updatedSku, SkuOutputDto.class));

        return response;
    }

    public SkuResponseDto assignTag(Long productId, Long tagId) throws MapperException, RecordNotFoundException, DBException {

        SkuResponseDto response = new SkuResponseDto();

        Sku product = this.skuService.assignTagToSku(productId, tagId);

        response.setSku(transform(product, SkuOutputDto.class));

        return response;
    }

    public SuccessfulResponse removeTag(Long productId, Long tagId) throws MapperException, DBException {

        SuccessfulResponse response = new SuccessfulResponse();

        this.skuService.removeTagFromSku(productId, tagId);

        return response;
    }

    public AllTagsResponseDto fetchSkuTags(Long productId) throws MapperException {

        AllTagsResponseDto response = new AllTagsResponseDto();

        List<Tag> categoryTags = this.skuService.getTagsForSku(productId);

        response.setTags(transformCollection(categoryTags, TagDto.class));

        return response;
    }

    public BenefitResponseDto addSkuBenefit(Long skuId, BenefitDto benefitDto) throws MapperException, RecordNotFoundException {

        BenefitResponseDto response = new BenefitResponseDto();

        Benefit benefitModel = transform(benefitDto, Benefit.class);

        benefitModel = skuService.addBenefitToSku(skuId, benefitModel);

        response.setBenefit(transform(benefitModel, BenefitDto.class));

        return response;
    }

    public AllBenefitResponseDto getSkuBenefits(Long skuId) throws MapperException {

        AllBenefitResponseDto response = new AllBenefitResponseDto();

        List<Benefit> benefits = skuService.getSkuBenefits(skuId);

        response.setBenefits(transformCollection(benefits, BenefitDto.class));

        return response;
    }

    public SuccessfulResponse removeSkuBenefit(Long skuId, Long benefitId) throws MapperException, RecordNotFoundException {

        SuccessfulResponse response = new SuccessfulResponse();

        this.skuService.removeBenefitFromSku(skuId, benefitId);

        return response;
    }


}
