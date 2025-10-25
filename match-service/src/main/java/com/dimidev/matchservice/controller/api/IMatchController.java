package com.dimidev.matchservice.controller.api;

import com.dimidev.matchservice.dto.match.MatchDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface IMatchController {

    @Operation(summary = "Получить все матчи пользователя")
    List<MatchDto> getAllByUserId(@PathVariable Long userId);

    @Operation(summary = "Получить матч по id")
    MatchDto getByIdAndUserId(@PathVariable Long id, @PathVariable Long userId);
}
