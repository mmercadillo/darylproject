package daryl.system.model.mapper;

import org.modelmapper.ModelMapper;

import daryl.system.model.historicos.HistWti;
import daryl.system.model.historicos.dto.HistWtiDto;

public class HistWtiMapper {

	public static HistWtiDto getDto(HistWti entity) {
		
		ModelMapper modelMapper = new ModelMapper();

		HistWtiDto dto = modelMapper.map(entity, HistWtiDto.class);
	
		
		return dto;

	}

	
	
}
