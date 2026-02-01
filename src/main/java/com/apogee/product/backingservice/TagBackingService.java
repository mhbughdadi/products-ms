package com.apogee.product.backingservice;

import com.apogee.product.dtos.inputs.TagDto;
import com.apogee.product.dtos.output.AllTagsResponseDto;
import com.apogee.product.dtos.output.TagResponseDto;
import com.apogee.product.models.Tag;
import com.apogee.product.services.TagService;
import com.apogee.product.utilities.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.apogee.product.utilities.Utilities.transformCollection;

@Service
public class TagBackingService {

    @Autowired
    private TagService tagService;

    public TagResponseDto addTag(TagDto tagDto) throws Exception {

        TagResponseDto response = new TagResponseDto();

        Tag savedTag = this.tagService.saveTag(Mapper.map(tagDto, Tag.class));

        response.setTag(Mapper.map(savedTag, TagDto.class));

        return response;
    }

    public TagResponseDto updateTag(TagDto tagDto) throws Exception {

        TagResponseDto response = new TagResponseDto();

        Tag updatedTag = this.tagService.updateTag(Mapper.map(tagDto, Tag.class));

        response.setTag(Mapper.map(updatedTag, TagDto.class));

        return response;
    }

    public TagResponseDto deleteTagById(Long tagId) throws Exception {

        TagResponseDto response = new TagResponseDto();

        Tag deletedTag = this.tagService.deleteTag(tagId);

        response.setTag(Mapper.map(deletedTag, TagDto.class));

        return response;
    }

    public AllTagsResponseDto getAllTags() throws Exception {

        AllTagsResponseDto response = new AllTagsResponseDto();

        List<Tag> allTags = this.tagService.findAllTags();
        List<TagDto> tagDtoList = transformCollection(allTags, TagDto.class);

        response.setTags(tagDtoList);

        return response;
    }

    public TagResponseDto getTagById(Long tagId) throws Exception {

        TagResponseDto response = new TagResponseDto();

        Tag tag = this.tagService.findTag(tagId);

        response.setTag(Mapper.map(tag, TagDto.class));

        return response;

    }


}
