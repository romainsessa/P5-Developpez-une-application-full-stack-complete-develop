package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.PostDTO;
import com.openclassrooms.mddapi.entity.Post;

import java.util.List;

import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import org.mapstruct.Mapper;

@Component
@Mapper(componentModel = "spring", uses = { CommentMapper.class })
public interface PostMapper {
    @Mapping(source = "topic.name", target = "topic")
    @Mapping(source = "author.username", target = "author")

    PostDTO toDto(Post post);
    List<PostDTO> toDto(List<Post> posts);

}
