package com.dimidev.userservice.dto.user;

import lombok.Data;

@Data
public class UserCreateUpdateDto {

    private String name;

    private String email;

    private Integer age;
}