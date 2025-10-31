package com.dimidev.userservice.controller.api;

import com.dimidev.userservice.dto.user.UserCreateUpdateDto;
import  com.dimidev.userservice.dto.user.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IUserController {

    @Operation(summary = "Получить список пользователей")
    List<UserDto> getAll();

    @Operation(summary = "Получить пользователя по id")
    UserDto getById(@PathVariable Long id);

    @Operation(summary = "Создать пользователя")
    UserDto create(@RequestBody UserCreateUpdateDto userCreateUpdateDto);

    @Operation(summary = "Изменить пользователя")
    UserDto update(@RequestBody UserCreateUpdateDto userCreateUpdateDto, @PathVariable Long id);

    @Operation(summary = "Удалить пользователя")
    void deleteById(@PathVariable Long id);
}
