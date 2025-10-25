package com.dimidev.matchservice.controller;

import com.dimidev.matchservice.controller.api.IMatchController;
import com.dimidev.matchservice.dto.match.MatchDto;
import com.dimidev.matchservice.mapper.MatchMapper;
import com.dimidev.matchservice.service.MatchService;
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
