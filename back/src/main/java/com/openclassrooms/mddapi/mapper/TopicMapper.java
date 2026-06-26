package com.openclassrooms.mddapi.mapper;

import org.springframework.stereotype.Component;

import com.openclassrooms.mddapi.dto.TopicDTO;
import com.openclassrooms.mddapi.entity.Topic;

import org.mapstruct.Mapper;

@Component
@Mapper(componentModel = "spring")
public interface TopicMapper extends EntityMapper<TopicDTO, Topic> {
}
