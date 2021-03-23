package daryl.system.model.mapper;

import org.modelmapper.ModelMapper;

import daryl.system.model.historicos.HistEurUsd;
import daryl.system.model.historicos.dto.HistEurUsdDto;

public class HistEurUsdMapper {

	public static HistEurUsdDto getDto(HistEurUsd entity) {
		
		ModelMapper modelMapper = new ModelMapper();

		HistEurUsdDto dto = modelMapper.map(entity, HistEurUsdDto.class);
	
		
		return dto;

	}

	
	
}
