package com.dimidev.matchservice.dto.esteem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EsteemDto {

    private Long id;

    private Long likerId;

    private Long likedId;

    private boolean esteem;

    private LocalDateTime createdAt;
}
