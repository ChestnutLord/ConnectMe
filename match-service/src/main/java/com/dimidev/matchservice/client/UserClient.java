package com.dimidev.matchservice.client;

import com.dimidev.matchservice.dto.user.UserDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface UserClient {

    Logger log = LoggerFactory.getLogger(UserClient.class);

    @GetExchange("api/v1/users/{id}")
    @CircuitBreaker(name = "user", fallbackMethod = "getByIdFallback")
    @Retry(name = "user")
    UserDto getById(@PathVariable Long id);

    default UserDto getByIdFallback(Long id, Throwable throwable) {
        log.info("Fallback triggered for user id = {}. Reason: {}", id, throwable.getMessage());
        UserDto fallback = new UserDto();
        fallback.setId(id);
        fallback.setName("unknown");
        fallback.setEmail("unknown@example.com");
        fallback.setAge(0);
        return fallback;
    }
}
