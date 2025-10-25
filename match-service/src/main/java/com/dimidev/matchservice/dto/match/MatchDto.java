package com.dimidev.matchservice.dto.match;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MatchDto {

    private Long id;

    private Long user1Id;

    private Long user2Id;

    private LocalDateTime createdAt;
}
