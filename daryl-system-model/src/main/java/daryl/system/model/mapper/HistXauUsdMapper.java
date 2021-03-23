package daryl.system.model.mapper;

import org.modelmapper.ModelMapper;

import daryl.system.model.historicos.HistXauUsd;
import daryl.system.model.historicos.dto.HistXauUsdDto;

public class HistXauUsdMapper {

	public static HistXauUsdDto getDto(HistXauUsd entity) {
		
		ModelMapper modelMapper = new ModelMapper();

		HistXauUsdDto dto = modelMapper.map(entity, HistXauUsdDto.class);
	
		
		return dto;

	}

	
	
}
