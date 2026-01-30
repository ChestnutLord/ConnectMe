package com.dimidev.userservice.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateUpdateDto {

    private String name;

    private String email;

    private Integer age;
}