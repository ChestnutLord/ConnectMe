package com.dimidev.connectme.match_service.controller;

import com.dimidev.connectme.match_service.controller.api.IMatchController;
import com.dimidev.connectme.match_service.dto.match.MatchDto;
import com.dimidev.connectme.match_service.mapper.MatchMapper;
import com.dimidev.connectme.match_service.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/users/{userId}/matches")
@RequiredArgsConstructor
public class MatchController implements IMatchController {

    private final MatchService service;
    private final MatchMapper mapper;

    @GetMapping
    @Override
    public List<MatchDto> getAllByUserId(@PathVariable Long userId) {
        return mapper.toListDto(service.getAllByUserId(userId));
    }

    @GetMapping("/{id}")
    @Override
    public MatchDto getByIdAndUserId(@PathVariable Long id, @PathVariable Long userId) {
        return mapper.toDto(service.getByIdAndUserId(id, userId));
    }
}
