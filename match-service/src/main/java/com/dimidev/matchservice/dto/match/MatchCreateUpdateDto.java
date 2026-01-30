package com.dimidev.matchservice.dto.match;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchCreateUpdateDto {

    private Long user1Id;

    private Long user2Id;
}
