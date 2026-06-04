package com.openclassrooms.mddapi.mapper;

import org.springframework.stereotype.Component;

import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.entity.User;

import org.mapstruct.Mapper;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDTO, User> {
}