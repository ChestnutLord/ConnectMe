package com.dimidev.matchservice.dto.esteem;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EsteemDto {

    private Long id;

    private Long likerId;

    private Long likedId;

    private boolean esteem;

    private LocalDateTime createdAt;
}
