package com.dimidev.userservice.dto.user;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateUpdateDto {

    private String name;

    private String email;

    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 105, message = "Age must not exceed 105")
    private Integer age;
}