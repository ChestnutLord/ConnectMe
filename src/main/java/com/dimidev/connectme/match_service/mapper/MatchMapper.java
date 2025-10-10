package com.dimidev.connectme.match_service.mapper;

import com.dimidev.connectme.match_service.dto.match.MatchDto;
import com.dimidev.connectme.match_service.model.Match;
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
