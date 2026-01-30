package com.dimidev.matchservice.dto.esteem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EsteemCreateUpdateDto {

    private Long likedId;

    private boolean esteem;
}
