package com.dimidev.matchservice.client;

import com.dimidev.matchservice.dto.user.UserDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface UserClient {

    @GetExchange("api/v1/users/{id}")
    UserDto getById(@PathVariable Long id);
}
