package com.apogee.product.backingservice;

import com.apogee.product.dtos.inputs.TagDto;
import com.apogee.product.dtos.output.AllTagsResponseDto;
import com.apogee.product.dtos.output.TagResponseDto;
import com.apogee.product.models.Tag;
import com.apogee.product.services.TagService;
import com.apogee.product.exceptions.MapperException;
import com.apogee.product.exceptions.RecordNotFoundException;
import com.apogee.product.exceptions.DBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.apogee.common.mapper.ObjectMapper.transform;
import static com.apogee.common.mapper.ObjectMapper.transformCollection;

@Service
public class TagBackingService {

    @Autowired
    private TagService tagService;

    public TagResponseDto addTag(TagDto tagDto) throws MapperException, RecordNotFoundException, DBException {

        TagResponseDto response = new TagResponseDto();

        Tag savedTag = this.tagService.saveTag(transform(tagDto, Tag.class));

        response.setTag(transform(savedTag, TagDto.class));

        return response;
    }

    public TagResponseDto updateTag(TagDto tagDto) throws MapperException, RecordNotFoundException, DBException {

        TagResponseDto response = new TagResponseDto();

        Tag updatedTag = this.tagService.updateTag(transform(tagDto, Tag.class));

        response.setTag(transform(updatedTag, TagDto.class));

        return response;
    }

    public TagResponseDto deleteTagById(Long tagId) throws MapperException, RecordNotFoundException, DBException {

        TagResponseDto response = new TagResponseDto();

        Tag deletedTag = this.tagService.deleteTag(tagId);

        response.setTag(transform(deletedTag, TagDto.class));

        return response;
    }

    public AllTagsResponseDto getAllTags() throws MapperException {

        AllTagsResponseDto response = new AllTagsResponseDto();

        List<Tag> allTags = this.tagService.findAllTags();
        List<TagDto> tagDtoList = transformCollection(allTags, TagDto.class);

        response.setTags(tagDtoList);

        return response;
    }

    public TagResponseDto getTagById(Long tagId) throws MapperException, RecordNotFoundException {

        TagResponseDto response = new TagResponseDto();

        Tag tag = this.tagService.findTag(tagId);

        response.setTag(transform(tag, TagDto.class));

        return response;

    }


}
