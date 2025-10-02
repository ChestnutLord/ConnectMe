package com.dimidev.connectme.user_service.mapper;

import com.dimidev.connectme.user_service.dto.user.UserCreateUpdateDto;
import com.dimidev.connectme.user_service.dto.user.UserDto;
import com.dimidev.connectme.user_service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toModel(UserDto userDto);

    User toModel(UserCreateUpdateDto userCreateUpdateDto);

    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    User toCrateUpdateModel(UserCreateUpdateDto userCreateUpdateDto);


    List<User> toListModel(List<UserDto> userDto);

    List<UserDto> toListDto(List<User> user);
}
