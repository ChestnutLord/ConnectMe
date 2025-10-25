package com.dimidev.matchservice.client;

import com.dimidev.matchservice.dto.user.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "user-service", url = "http://user-service:8080")
public interface UserClient {

    @GetMapping(path="api/v1/users/{id}")
    UserDto getById(@PathVariable Long id);
}
