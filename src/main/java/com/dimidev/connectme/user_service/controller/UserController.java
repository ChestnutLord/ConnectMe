package com.dimidev.connectme.user_service.controller;

import com.dimidev.connectme.user_service.controller.api.IUserController;
import com.dimidev.connectme.user_service.dto.user.UserCreateUpdateDto;
import com.dimidev.connectme.user_service.dto.user.UserDto;
import com.dimidev.connectme.user_service.mapper.UserMapper;
import com.dimidev.connectme.user_service.model.User;
import com.dimidev.connectme.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/users")
@RequiredArgsConstructor
public class UserController implements IUserController {

    private final UserService service;
    private final UserMapper mapper;

    @Override
    @GetMapping
    public List<UserDto> getAll() {
        return mapper.toListDto(service.getAll());
    }

    @Override
    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        return mapper.toDto(service.getById(id));
    }

    @Override
    @PostMapping
    public UserDto create(@RequestBody UserCreateUpdateDto userCreateUpdateDto) {
        User user = mapper.toModel(userCreateUpdateDto);
        return mapper.toDto(service.create(user));
    }

    @Override
    @PutMapping("/{id}")
    public UserDto update(@RequestBody UserCreateUpdateDto userCreateUpdateDto, @PathVariable Long id) {
        User user = mapper.toModel(userCreateUpdateDto);
        user.setId(id);
        return mapper.toDto(service.update(user));
    }

    @Override
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }
}
