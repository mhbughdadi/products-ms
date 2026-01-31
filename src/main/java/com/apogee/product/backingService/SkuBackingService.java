package com.apogee.product.backingService;

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
import com.apogee.product.services.CurrencyService;
import com.apogee.product.services.ImageService;
import com.apogee.product.services.SkuService;
import com.apogee.product.utilities.Mapper;
import com.apogee.product.utilities.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.apogee.product.utilities.Utilities.transformCollection;

@Service
public class SkuBackingService {

    @Autowired
    private SkuService skuService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CurrencyService currencyService;

    public AllSkusResponseDto getAllSkus() throws Exception {

        AllSkusResponseDto response = new AllSkusResponseDto();

        List<SkuOutputDto> allSkus = Utilities.transformCollection(skuService.findAllSkus(), SkuOutputDto.class);
        response.setSkus(allSkus);

        return response;
    }

    public SkuResponseDto addSku(SkuDto productDto) throws Exception {

        SkuResponseDto response = new SkuResponseDto();

        Sku product = Mapper.map(productDto, Sku.class);
        Sku savedSku = skuService.addSku(product);
        response.setSku(Mapper.map(savedSku, SkuOutputDto.class));

        return response;
    }

    public SkuResponseDto getSkuById(Long productId) throws Exception {

        SkuResponseDto response = new SkuResponseDto();

        Sku product = skuService.findSkuById(productId);
        response.setSku(Mapper.map(product, SkuOutputDto.class));

        return response;
    }

    public void deleteSku(Long productId) throws Exception {

        this.skuService.deleteSkuById(productId);
    }

    public SkuResponseDto updateSku(SkuDto product) throws Exception {

        SkuResponseDto response = new SkuResponseDto();

        Sku updatedSku = this.skuService.updateSku(Mapper.map(product, Sku.class));
        response.setSku(Mapper.map(updatedSku, SkuOutputDto.class));

        return response;
    }

    public SkuResponseDto assignTag(Long productId, Long tagId) throws Exception {

        SkuResponseDto response = new SkuResponseDto();

        Sku product = this.skuService.assignTagToSku(productId, tagId);

        response.setSku(Mapper.map(product, SkuOutputDto.class));

        return response;
    }

    public SuccessfulResponse removeTag(Long productId, Long tagId) throws Exception {

        SuccessfulResponse response = new SuccessfulResponse();

        this.skuService.removeTagFromSku(productId, tagId);

        return response;
    }

    public AllTagsResponseDto fetchSkuTags(Long productId) throws Exception {

        AllTagsResponseDto response = new AllTagsResponseDto();

        List<Tag> categoryTags = this.skuService.getTagsForSku(productId);

        response.setTags(transformCollection(categoryTags, TagDto.class));

        return response;
    }

    public BenefitResponseDto addSkuBenefit(Long skuId, BenefitDto benefitDto) throws Exception {

        BenefitResponseDto response = new BenefitResponseDto();

        Benefit benefitModel = Mapper.map(benefitDto, Benefit.class);

        benefitModel = skuService.addBenefitToSku(skuId, benefitModel);

        response.setBenefit(Mapper.map(benefitModel, BenefitDto.class));

        return response;
    }

    public AllBenefitResponseDto getSkuBenefits(Long skuId) throws Exception {

        AllBenefitResponseDto response = new AllBenefitResponseDto();

        List<Benefit> benefits = skuService.getSkuBenefits(skuId);

        response.setBenefits(transformCollection(benefits, BenefitDto.class));

        return response;
    }

    public SuccessfulResponse removeSkuBenefit(Long skuId, Long benefitId) throws Exception {

        SuccessfulResponse response = new SuccessfulResponse();

        this.skuService.removeBenefitFromSku(skuId, benefitId);

        return response;
    }


}
