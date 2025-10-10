package com.dimidev.connectme.match_service.controller.api;

import com.dimidev.connectme.match_service.dto.esteem.EsteemDto;
import com.dimidev.connectme.match_service.dto.esteem.EsteemCreateUpdateDto;
import com.dimidev.connectme.match_service.dto.esteem.EsteemUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IEsteemController {

    @Operation(summary = "Получить все оценки пользователя")
    List<EsteemDto> getAllByUserId(@PathVariable Long userId);

    @Operation(summary = "Получить оценку по id")
    EsteemDto getByIdAndUserId(@PathVariable Long userId, @PathVariable Long id);

    @Operation(summary = "Создать оценку")
    EsteemDto create(@PathVariable Long userId, @RequestBody EsteemCreateUpdateDto esteemCreateUpdateDto);

    @Operation(summary = "Изменить оценку")
    EsteemDto update(@PathVariable Long likeId, @PathVariable Long userId, @RequestBody EsteemUpdateDto esteemUpdateDto);

    @Operation(summary = "Удалить оценку")
    void deleteByIdAndUserId(@PathVariable Long userId, @PathVariable Long id);
}
