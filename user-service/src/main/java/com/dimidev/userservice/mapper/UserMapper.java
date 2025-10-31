package com.dimidev.userservice.mapper;

import com.dimidev.userservice.dto.user.UserCreateUpdateDto;
import  com.dimidev.userservice.dto.user.UserDto;
import com.dimidev.userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toModel(UserDto userDto);

    User toModel(UserCreateUpdateDto userCreateUpdateDto); //TODO do not need it

    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    User toCrateUpdateModel(UserCreateUpdateDto userCreateUpdateDto); //TODO do not need it


    List<User> toListModel(List<UserDto> userDto);

    List<UserDto> toListDto(List<User> user);
}
