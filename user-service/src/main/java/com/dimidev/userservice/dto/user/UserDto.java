package com.dimidev.userservice.dto.user;

import lombok.Data;

@Data
public class UserDto {

    private Long id;

    private String name;

    private String email;

    private Integer age;
}