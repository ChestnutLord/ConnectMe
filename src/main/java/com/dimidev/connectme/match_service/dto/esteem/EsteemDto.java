package com.dimidev.connectme.match_service.dto.esteem;

import com.dimidev.connectme.user_service.dto.user.UserDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EsteemDto {

    private Long id;

    private UserDto liker;

    private UserDto liked;

    private boolean esteem;

    private LocalDateTime createdAt;
}
