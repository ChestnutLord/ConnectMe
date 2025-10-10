package com.dimidev.connectme.match_service.controller.api;

import com.dimidev.connectme.match_service.dto.match.MatchCreateUpdateDto;
import com.dimidev.connectme.match_service.dto.match.MatchDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IMatchController {

    @Operation(summary = "Получить все матчи пользователя")
    List<MatchDto> getAllByUserId(@PathVariable Long userId);

    @Operation(summary = "Получить матч по id")
    MatchDto getByIdAndUserId(@PathVariable Long id, @PathVariable Long userId);
}
