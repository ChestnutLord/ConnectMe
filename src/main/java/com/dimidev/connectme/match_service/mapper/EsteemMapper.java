package com.dimidev.connectme.match_service.mapper;

import com.dimidev.connectme.match_service.dto.esteem.EsteemDto;
import com.dimidev.connectme.match_service.model.Esteem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EsteemMapper {

    Esteem toModel(EsteemDto esteemDto);

    EsteemDto toDto(Esteem esteem);

    List<Esteem> toListModel(List<EsteemDto> esteemDtos);

    List<EsteemDto> toListDto(List<Esteem> esteems);
}
