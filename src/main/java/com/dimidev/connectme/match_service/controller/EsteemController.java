package com.dimidev.connectme.match_service.controller;

import com.dimidev.connectme.match_service.controller.api.IEsteemController;
import com.dimidev.connectme.match_service.dto.esteem.EsteemCreateUpdateDto;
import com.dimidev.connectme.match_service.dto.esteem.EsteemDto;
import com.dimidev.connectme.match_service.dto.esteem.EsteemUpdateDto;
import com.dimidev.connectme.match_service.mapper.EsteemMapper;
import com.dimidev.connectme.match_service.service.EsteemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/users/{userId}/esteems")
@RequiredArgsConstructor
public class EsteemController implements IEsteemController {

    private final EsteemService service;
    private final EsteemMapper mapper;

    @GetMapping
    @Override
    public List<EsteemDto> getAllByUserId(@PathVariable Long userId) {
        return mapper.toListDto(service.getAllByUserId(userId));
    }

    @GetMapping("/{id}")
    @Override
    public EsteemDto getByIdAndUserId(@PathVariable Long id, @PathVariable Long userId) {
        return mapper.toDto(service.getByIdAndUserId(id, userId));
    }

    @PostMapping
    @Override
    public EsteemDto create(@PathVariable Long userId,
                            @RequestBody EsteemCreateUpdateDto esteemCreateUpdateDto) {
        return mapper.toDto(service.create(userId, esteemCreateUpdateDto.getLikedId(), esteemCreateUpdateDto.isEsteem()));
    }

    @PutMapping("/{id}")
    @Override
    public EsteemDto update(@PathVariable Long id, @PathVariable Long userId, @RequestBody EsteemUpdateDto esteemUpdateDto) {
        return mapper.toDto(service.update(id, userId, esteemUpdateDto.isEsteem()));
    }

    @DeleteMapping("/{id}")
    @Override
    public void deleteByIdAndUserId(@PathVariable Long id, @PathVariable Long userId) {
        service.deleteByIdAndUserId(id, userId);
    }
}
