package com.dimidev.connectme.match_service.dto.match;

import com.dimidev.connectme.user_service.dto.user.UserDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MatchDto {

    private Long id;

    private UserDto user1;

    private UserDto user2;

    private LocalDateTime createdAt;
}
