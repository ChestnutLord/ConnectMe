package com.dimidev.matchservice.mapper;

import com.dimidev.matchservice.dto.match.MatchDto;
import com.dimidev.matchservice.model.Match;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MatchMapper {

    Match toModel(MatchDto matchDto);

    MatchDto toDto(Match match);

    List<Match> toListModel(List<MatchDto> matchDtos);

    List<MatchDto> toListDto(List<Match> matches);
}
