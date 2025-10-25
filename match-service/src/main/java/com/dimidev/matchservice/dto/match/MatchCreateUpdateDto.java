package com.dimidev.matchservice.dto.match;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchCreateUpdateDto {

    private Long user1Id;

    private Long user2Id;
}
