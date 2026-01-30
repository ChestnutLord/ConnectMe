package com.dimidev.userservice.controller;

import com.dimidev.userservice.controller.api.IUserController;
import com.dimidev.userservice.dto.user.UserCreateUpdateDto;
import com.dimidev.userservice.dto.user.UserDto;
import com.dimidev.userservice.mapper.UserMapper;
import com.dimidev.userservice.model.User;
import com.dimidev.userservice.service.UserService;
import jakarta.validation.Valid;
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
    public UserDto create(@RequestBody @Valid UserCreateUpdateDto userCreateUpdateDto) {
        User user = mapper.toModel(userCreateUpdateDto);
        return mapper.toDto(service.create(user));
    }

    @Override
    @PutMapping("/{id}")
    public UserDto update(@RequestBody @Valid UserCreateUpdateDto userCreateUpdateDto, @PathVariable Long id) {
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
