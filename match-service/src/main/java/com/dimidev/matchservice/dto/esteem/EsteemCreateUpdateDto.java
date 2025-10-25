package com.dimidev.matchservice.dto.esteem;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class  EsteemCreateUpdateDto {

    private Long likedId;

    private boolean esteem;
}
